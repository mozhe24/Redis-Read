package com.iflytek.jrshi.ViewRedis.redis.proxy;


import com.iflytek.jrshi.ViewRedis.ExceptionUtil;
import com.iflytek.jrshi.ViewRedis.ThreadLocalVariables;
import com.iflytek.jrshi.ViewRedis.UtilOper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 带负载均衡的redis客户端 </br>
 *
 * @author xdlv
 */
public class RedisClientProxy {
    private static final Logger log = LoggerFactory.getLogger(RedisClientProxy.class);

    private Map<String, RedisClientWrapper> clients = new HashMap<String, RedisClientWrapper>();
    private int checkperiod = 2000; // 默认2秒检查一次
    private Timer timer;
    private AtomicLong numError = new AtomicLong(0);
    private AtomicLong numErrorTest = new AtomicLong(0);// 定时任务测试节点是否可用时出现的异常数，后续其他审计程序可以减去这个值

    public boolean init(RedisClientProxyConfig config) {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(config.getMaxActive());
            poolConfig.setMaxIdle(config.getMaxIdle());
            poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
            poolConfig.setTestOnBorrow(config.isTestOnBorrow());
            poolConfig.setTestWhileIdle(config.isTestWhileIdle());
            poolConfig.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEviction());

            if (StringUtils.isBlank(config.getPassword())) {
                config.setPassword(null); // 如果密码为空，则置为null，jedis接口特点
            }

            for (String node : config.getNodes()) {
                HostAndPortAndWeight hostAndPortAndWeight = UtilOper.makeHostAndPortAndWeight(node);
                if (hostAndPortAndWeight == null) {
                    log.error("invalid node<{}>", node);
                    return false;
                }
                if (hostAndPortAndWeight.getWeight() < 0) {
                    log.error("node<{}>'s weight < 0", node);
                    return false;
                }
                RedisClientWrapper wrapper = new RedisClientWrapper();
                if (!wrapper.init(poolConfig, hostAndPortAndWeight.getHost(), hostAndPortAndWeight.getPort(), config.getConnectionTimeout(), config.getSocketTimeout(),
                        config.getPassword(), null)) {
                    log.error("{} init failed", node);
                    return false;
                }
                wrapper.setOnline(true);
                wrapper.setWeight(hostAndPortAndWeight.getWeight());
                wrapper.setTotalRequestLastSample(0);
                wrapper.setTotalExceptionLastSample(0);
                wrapper.setTimestampLastSample(System.currentTimeMillis());

                clients.put(node, wrapper);
            }

            timer = new Timer();
            timer.schedule(new RedisClientProxyCheckUpTimerTask(this), checkperiod, checkperiod);

            return true;
        } catch (Exception e) {
            log.error("{} -> {}", e.getMessage(), ExceptionUtil.getAllTraceInfo(e));
            return false;
        }
    }

    public void finish() {
        try {
            timer.cancel();
            for (RedisClientWrapper wrapper : clients.values()) {
                wrapper.finish();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 权重随机选择JedisPoolWrapper </br>
     * <p>
     * 已经下线的节点不参与选择 </br>
     * 优先按照权重概率选择权重不为0的节点 </br>
     * 如果没有选中，则选择一个权重为0的节点，选择顺序依赖程序 </br>
     *
     * @return
     * @throws Exception
     */
    private RedisClientWrapper getJedisPoolWrapper() throws ResourceNotFoundException {
        List<String> candidates = ThreadLocalVariables.arrayListString.get();
        candidates.clear();
        String backupKey = null;

        Iterator<Entry<String, RedisClientWrapper>> itor = clients.entrySet().iterator();
        while (itor.hasNext()) {
            Entry<String, RedisClientWrapper> entry = itor.next();
            String key = entry.getKey();
            RedisClientWrapper value = entry.getValue();
            if (!value.isOnline()) continue;

            if (value.getWeight() == 0) {
                if (backupKey == null) {
                    backupKey = key;
                }
                continue;
            }
            for (int i = 0; i < value.getWeight(); i++) {
                candidates.add(key);
            }
        }

        String selectedKey;
        if (!candidates.isEmpty()) {
            Random r = ThreadLocalVariables.random.get();
            int rand = r.nextInt(candidates.size());
            selectedKey = candidates.get(rand);
        } else {
            selectedKey = backupKey;
        }

        if (selectedKey == null) {
            numError.incrementAndGet();
            throw new ResourceNotFoundException("RedisClientWrapper not found");
        }
        log.debug("selected redis {}", selectedKey);
        return clients.get(selectedKey);
    }

    public void set(String key, String value) throws Exception {
        try {
            if (key == null) return;
            RedisClientWrapper wrapper = getJedisPoolWrapper();
            wrapper.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String get(String key) {
        try {
            if (key == null) return null;
            RedisClientWrapper wrapper = getJedisPoolWrapper();
            return wrapper.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    public String hget(String key, String field) {
        try {
            if (key == null || field == null) return null;
            RedisClientWrapper wrapper = getJedisPoolWrapper();
           return wrapper.hget(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<String> mget(String... keys) {
        try {
            if (keys == null || keys.length == 0) return null;
            RedisClientWrapper wrapper = getJedisPoolWrapper();
           return wrapper.mget(keys);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public Long hincrBy(String key, String field, long num) throws Exception {
        if (key == null || field == null) return null;
        try {
            RedisClientWrapper wrapper = getJedisPoolWrapper();
            return wrapper.hincrBy(key, field, num);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0L;
    }

    public Map<String, RedisClientWrapper> getClients() {
        return clients;
    }

    public void setClients(Map<String, RedisClientWrapper> clients) {
        this.clients = clients;
    }

    public long getTotalExceptionNum() {
        long num = numError.get();
        for (RedisClientWrapper wrapper : clients.values()) {
            num += wrapper.getNumConnectTimeout();
            num += wrapper.getNumOtherExceptions();
            num += wrapper.getNumSocketTimeout();
            num += wrapper.getNumWaitTimeout();
        }
        num -= numErrorTest.get();
        return num;
    }

    public AtomicLong getNumError() {
        return numError;
    }

    public AtomicLong getNumErrorTest() {
        return numErrorTest;
    }

    public static void main(String[] args) throws Exception {
        Set<String> nodes = new HashSet<String>();
        nodes.add("60.166.12.158:6379:100");
        nodes.add("60.166.12.158:22124:100");
        nodes.add("60.166.12.158:22125:100");
        nodes.add("60.166.12.158:22126:100");

        RedisClientProxyConfig config = new RedisClientProxyConfig();
        config.setNodes(nodes);
        config.setPassword("xylxredis123!@#mima");
        // 其他参数酌情设置

        RedisClientProxy proxy = new RedisClientProxy();
        if (!proxy.init(config)) {
            System.out.println("init failed");
        }
        try {
            proxy.set("test", "sjrlovelrq");
            for (int i = 0; i < 10; i++) {
                String str = proxy.get("test");
                System.out.println(str);
            }
        } catch (Exception e) {
            log.info("Cannot get Resource!");
        }

    }

}
