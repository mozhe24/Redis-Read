package com.iflytek.jrshi.ViewRedis.redis.proxy;


import com.iflytek.jrshi.ViewRedis.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

public class RedisClientProxyCheckUpTimerTask extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(RedisClientProxyCheckUpTimerTask.class);

    private RedisClientProxy proxy;

    public RedisClientProxyCheckUpTimerTask(RedisClientProxy proxy) {
        this.proxy = proxy;
    }

    private void checkUp(RedisClientProxy proxy) {
        if (proxy == null) return;
        Map<String, RedisClientWrapper> clients = proxy.getClients();
        if (clients == null || clients.isEmpty()) return;

        long now = System.currentTimeMillis();
        Iterator<Entry<String, RedisClientWrapper>> itor = clients.entrySet().iterator();
        while (itor.hasNext()) {
            Entry<String, RedisClientWrapper> entry = itor.next();
            String key = entry.getKey();
            RedisClientWrapper client = entry.getValue();

            long totalRequestLastSample = client.getTotalRequestLastSample();
            long totalExceptionLastSample = client.getTotalExceptionLastSample();

            long totalRequestNow = client.getNumTotalRequest();
            long totalExceptionNow = getTotalExceptionNum(client);

            long incrementalRequest = totalRequestNow - totalRequestLastSample;
            long incrementalException = totalExceptionNow - totalExceptionLastSample;

            if (client.isOnline()) {
                if (incrementalRequest == 0) {
                    if (incrementalException == 0) {
                        continue;
                    } else {
                        client.setOnline(false);
                        log.error("redis node<{}> turns offline", key);
                    }
                } else {
                    double ratio = (double) incrementalException / (double) incrementalRequest;
                    if (ratio > 0.66) {
                        client.setOnline(false);
                        log.error("redis node<{}> turns offline", key);
                    }
                }
            } else {
                try {
                    client.get("key_for_test_redis_client_proxy");
                    client.setOnline(true);
                    log.debug("redis node<{}> turns online", key);
                } catch (Exception e) {
                    proxy.getNumErrorTest().incrementAndGet();
                    log.error("redis node<{}> keeps offline: {}", key, e.getMessage());
                }
            }

            client.setTimestampLastSample(now);
            client.setTotalRequestLastSample(totalRequestNow);
            client.setTotalExceptionLastSample(totalExceptionNow);
        }
    }

    private long getTotalExceptionNum(RedisClient client) {
        if (client == null) return 0L;

        return client.getNumConnectTimeout() + client.getNumSocketTimeout() + client.getNumWaitTimeout()
                + client.getNumOtherExceptions();
    }
    @Override
    public void run() {
        try {
            checkUp(proxy);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
