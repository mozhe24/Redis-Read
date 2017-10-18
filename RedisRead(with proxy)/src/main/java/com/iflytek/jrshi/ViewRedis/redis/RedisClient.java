package com.iflytek.jrshi.ViewRedis.redis;


import com.iflytek.jrshi.ViewRedis.ExceptionUtil;
import com.iflytek.jrshi.ViewRedis.ThreadLocalVariables;
import com.iflytek.jrshi.ViewRedis.UtilOper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 提供连接池化的Redis客户端
 *
 * @author xdlv
 */
public class RedisClient {
    private static final Logger log = LoggerFactory.getLogger(RedisClient.class);

    private String redisClientName; /* 此RedisClient的名称，用以区分别的RedisClient实例 */
    private JedisPool jedisPool;
    /* 配置信息 */
    private String host = "127.0.0.1";
    private int port = 6379;
    private String password = null;
    private int maxActive = 8;
    private int maxIdle = 8;
    private long maxWaitMillis = 1000;
    private boolean testOnBorrow = false;
    private int connectionTimeout = 1000;
    private int socketTimeout = 1000;

    /* 统计信息 */
    private AtomicLong numConnectTimeout = new AtomicLong(0); // 连接超时
    private AtomicLong numSocketTimeout = new AtomicLong(0); // 网络超时
    private AtomicLong numWaitTimeout = new AtomicLong(0); // 从池中获取连接超时
    private AtomicLong numOtherException = new AtomicLong(0); // 其他异常
    private AtomicLong numRequest = new AtomicLong(0);// 接口总访问数

    /* ====================== 初始化 ======================= */
    public RedisClient(String redisClientName) {
        this.redisClientName = redisClientName;
    }

    public RedisClient() {
    }

    public boolean init() {
        return doInit();
    }

    public boolean init(RedisClientConfig config) {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(config.getMaxActive());
            poolConfig.setMaxIdle(config.getMaxIdle());
            poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
            poolConfig.setTestOnBorrow(config.isTestOnBorrow());
            poolConfig.setTestWhileIdle(config.isTestWhileIdle());
            poolConfig.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEviction());

            if (StringUtils.isBlank(config.getPassword())) config.setPassword(null); // 如果密码为空，则置为null，jedis接口特点
            HostAndPort hostAndPort = UtilOper.makeHostAndPort(config.getNode());
            if (hostAndPort == null) {
                log.error("invalid node<{}>", config.getNode());
                return false;
            }

            jedisPool = new JedisPool(poolConfig, hostAndPort.getHost(), hostAndPort.getPort(), config.getConnectionTimeout(), config.getSocketTimeout(),
                    config.getPassword(), Protocol.DEFAULT_DATABASE, null);
            get("pmp_redis_client_test_key_2017_02_06"); // 测试连接

            return true;
        } catch (Exception e) {
            log.error("{} -> {}", e.getMessage(), ExceptionUtil.getAllTraceInfo(e));
            return false;
        }
    }

    public boolean init(String host, int port) {
        this.host = host;
        this.port = port;
        return doInit();
    }

    public boolean init(String host, int port, String password, int maxActive, int maxIdle, long maxWaitMillis, boolean testOnBorrow, int connectionTimeout,
                        int socketTimeout) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.maxWaitMillis = maxWaitMillis;
        this.testOnBorrow = testOnBorrow;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
        return doInit();
    }

    private boolean doInit() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);

        config.setTestWhileIdle(false);
        config.setTimeBetweenEvictionRunsMillis(0);

        jedisPool = new JedisPool(config, host, port, connectionTimeout, socketTimeout, null, Protocol.DEFAULT_DATABASE, null);
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.get("key");
            jedis.close();
            return true;
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    public boolean init(JedisPoolConfig poolConfig, String host, int port, int connectionTimeout, int socketTimeout, String password, String clientName) {
        try {
            jedisPool = new JedisPool(poolConfig, host, port, connectionTimeout, socketTimeout, password, Protocol.DEFAULT_DATABASE, clientName);
            get("pmp_redis_client_test_key_2017_02_06"); // 测试连接
            return true;
        } catch (Exception e) {
            log.error("{} -> {}", e.getMessage(), ExceptionUtil.getAllTraceInfo(e));
            return false;
        }
    }

    public void finish() {
        try {
            jedisPool.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) jedis.close();
        }
    }

    public int getUsage() {
        return jedisPool.getNumActive();
    }

    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
            handleException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    public String hget(String key, String field) throws Exception {
        Jedis jedis = null;
        try{
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.hget(key, field);
        } catch (Exception e) {
            handleException(e);
            throw e;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String setex(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long incrBy(String key, long value) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.incrBy(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Double incrByFloat(String key, double value) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.incrByFloat(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long hincrBy(String key, String field, long num) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.hincrBy(key, field, num);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            if (StringUtils.isBlank(key)) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public List<String> mget(String... keys) {
        Jedis jedis = null;
        try {
            if (keys == null || keys.length == 0) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.mget(keys);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String mset(String... keysvalues) {
        Jedis jedis = null;
        try {
            if (keysvalues == null || keysvalues.length == 0) return null;
            jedis = jedisPool.getResource();
            numRequest.incrementAndGet();
            return jedis.mset(keysvalues);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public void releaseBroken(Jedis jedis) {
        if (null != jedis) {
            try {
                jedisPool.returnBrokenResource(jedis);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{} -> {}" ,"jedis returnBrokenResource exception" , e);
            }
        }
    }

    public void release(Jedis jedis) {
        if (null != jedis) {
            try {
                jedisPool.returnResource(jedis);
            } catch (Exception e) {
                releaseBroken(jedis);
            }
        }
    }

    public Pipeline getPipeline() {
        Jedis jedis = jedisPool.getResource();
        return jedis.pipelined();
    }

    public void sync(Pipeline pipeline) {
        numRequest.incrementAndGet();
        pipeline.sync();
    }

    /**
     * pipeline.sync()
     *
     * @param pipeline
     * @param needCounting 如果pipeline没有任何指令，则不会向redis-server发送数据，即没有网络交互，则可以不记请求数
     */
    public void sync(Pipeline pipeline, boolean needCounting) {
        if (needCounting) numRequest.incrementAndGet();
        pipeline.sync();
    }

    /* ================ 获取对象配置和统计信息 ================= */
    public String getRedisClientName() {
        return redisClientName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public long getNumConnectTimeout() {
        return numConnectTimeout.get();
    }

    public long getNumSocketTimeout() {
        return numSocketTimeout.get();
    }

    public long getNumWaitTimeout() {
        return numWaitTimeout.get();
    }

    public long getNumOtherExceptions() {
        return numOtherException.get();
    }

    public long getNumTotalRequest() {
        return numRequest.get();
    }

    public long getTotalExceptionNum() {
        return numConnectTimeout.get() + numSocketTimeout.get() + numWaitTimeout.get() + numOtherException.get();
    }

    /* ===================== 异常处理 ====================== */
    public void handleException(Exception e) {
    /* 打印异常栈第一条和最后一条信息 */
        StringBuilder sb = ThreadLocalVariables.stringBuilder64.get();
        sb.setLength(0);
        sb.append(e.getMessage());
        Throwable throwable = e;
        while (throwable.getCause() != null)
            throwable = throwable.getCause();
        if (throwable != e) sb.append(" -> ").append(throwable.getMessage());
        log.error(sb.toString());

        /* 记录相关统计数据 */
        Class<?> clazz = throwable.getClass();
        if (clazz == SocketTimeoutException.class) {
            numSocketTimeout.incrementAndGet();
        } else if (clazz == NoSuchElementException.class) {
            numWaitTimeout.incrementAndGet();
        } else if (clazz == ConnectException.class) {
            numConnectTimeout.incrementAndGet();
        } else {
            numOtherException.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RedisClientConfig config = new RedisClientConfig();
        config.setNode("127.0.0.1:6379:0");
        config.setPassword("");

        RedisClient rc = new RedisClient();
        if (!rc.init(config)) {
            System.out.println("init failed");
            return;
        }

        System.out.println("init success");
    }

}
