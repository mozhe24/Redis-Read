package ReadRedis;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by caodongj on 2017/10/13.
 */

public class RedisClient {
    //private static final GLog log = new GLog(RedisClient.class);

    private JedisPool pool;
    private boolean broken = false;
    private String auth;

    public boolean init(String redisIp, int redisPort, int timeWait, int timeout,
                        int maxActive, int maxIdle, boolean testOnBorrow,String auth) {
        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大等待时间，单位毫秒
        config.setMaxWaitMillis(timeWait);
        // 设置最大连接数
        config.setMaxTotal(maxActive);
        // 设置最大空余连接数
        config.setMaxIdle(maxIdle);

        // 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的
        config.setTestOnBorrow(testOnBorrow);

        // 创建连接池
        //pool = new JedisPool(config, redisIp, redisPort);
        pool = new JedisPool(config, redisIp, redisPort, timeout);
        this.auth = auth;

        return true;
    }

    public void fini() {
        pool.destroy();
    }

    public Jedis getJedis(){
        Jedis jedisResource = null;
        try
        {
            if (pool != null)
            {
                jedisResource = pool.getResource();
                jedisResource.auth(auth);
                return jedisResource;
            } else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            if (jedisResource != null) {
                pool.returnBrokenResource(jedisResource);
            }
            //log.error("{} -> {}" ,"jedis getResource exception " , e);
            e.printStackTrace();
            return null;
        }
    }

    public void release(Jedis jedis) {
        if (null != jedis) {
            try{
                pool.returnResource(jedis);
            } catch (Exception e) {
                releaseBroken(jedis);
//			if (null != jedis) {
//				pool.returnBrokenResource(jedis);
//			}
//			e.printStackTrace();
//			log.error("{} -> {}" ,"jedis returnResource exception" , e);
            }
        }
    }

    public void releaseBroken(Jedis jedis){
        if (null != jedis) {
            try{
                pool.returnBrokenResource(jedis);
            } catch (Exception e) {
                e.printStackTrace();
                //log.error("{} -> {}" ,"jedis returnBrokenResource exception" , e);
            }
        }
    }

    public void put(String key, String value, int seconds) throws Exception {
        Jedis jedisResource = null;
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(key) || value == null) {
                //log.debug("the key you put is null or obj you put is null");
                return;
            }
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            jedisResource.setex(key, seconds,value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedisResource != null) {
                pool.returnResource(jedisResource);
            }

        }
    }

    public String get(String key) throws Exception {
        Jedis jedisResource = null;
        try {
            if (StringUtils.isBlank(key)) {
                //log.debug("the key you put is null ");
                return null;
            }
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            return jedisResource.get(key);
        } catch (Exception e) {
            throw e;
            // return null;
        } finally {
            if (jedisResource != null) {
                pool.returnResource(jedisResource);
            }

        }
    }

    public String getWithoutDeserialization(String key) throws Exception {
        Jedis jedisResource = null;
        try {
            if (StringUtils.isBlank(key)) {
                //log.debug("the key you put is null ");
                return null;
            }
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            byte[] o = jedisResource.get(key.getBytes());
            return new String(o, "utf-8");
        } catch (Exception e) {
            throw e;
            // return null;
        } finally {
            if (jedisResource != null) {
                jedisResource.close();
            }

        }
    }

    public Long incr(String key) {
        Jedis jedisResource = null;
        try {
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            return jedisResource.incr(key);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder(100);
            sb.append(e.getClass().getName()).append("\n");
            for (StackTraceElement ele : e.getStackTrace()) {
                sb.append(ele.getClassName()).append("::")
                        .append(ele.getMethodName()).append("::")
                        .append(ele.getLineNumber()).append("\n");
            }
            //log.error("{} -> {}" ,"incr() failed: " , sb.toString());
            return null;
        } finally {
            if (jedisResource != null) {
                jedisResource.close();
            }
        }
    }


    public static void main(String[] args) {
        RedisClient rc = new RedisClient();
        rc.init("60.166.12.158", 6379, 10000, 100, 20, 10, false,"");
        while (true){
            long time1=System.nanoTime();
            Jedis j = rc.getJedis();
            long time3=System.nanoTime();
            System.out.println("time cost getJedis: " + (time3-time1)/1000000);
            if (null==j) {
                System.out.println("jedis failed get jedis");
            }
            if (StringUtils.equalsIgnoreCase("OK", j.set("b1", "", "nx", "EX", 20)))
                ;
            long time4=System.nanoTime();
            System.out.println("time cost set: " + (time4-time3)/1000000);
            System.out.println(j.set("b1", "bbb", "nx", "EX", 20));
            System.out.println(j.set("a1", "albert", "nx", "EX", 20));
            long time5=System.nanoTime();
            System.out.println("time cost set: " + (time5-time4)/1000000);
            String ret = j.get("a1");
            long time6 = System.nanoTime();
            System.out.println("time cost get: " + (time6-time5)/1000000);
            System.out.println(ret);
            if (null == ret) System.out.println(j.get("x1"));
            rc.release(j);
            long time2=System.nanoTime();
            System.out.println("time cost : " + (time2-time1)/1000000);
        }

    }
}
