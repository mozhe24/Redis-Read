package com.iflytek.jrshi.ViewRedis;


import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

public class RedisClient {


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
//    	  log.error("{} -> {}" ,"jedis getResource exception " , e);
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
//			log.error("{} -> {}" ,"jedis returnBrokenResource exception" , e);
		}
	}
  }

    public void put(String key, Object value, int seconds) throws Exception {
        Jedis jedisResource = null;
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(key) || value == null) {
//                log.debug("the key you put is null or obj you put is null");
                return;
            }
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            jedisResource.setex(key.getBytes(), seconds, SerializeUtil.serialize(value));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedisResource != null) {
                pool.returnResource(jedisResource);
            }

        }
    }

    public Object get(String key) throws Exception {
        Jedis jedisResource = null;
        try {
            if (StringUtils.isBlank(key)) {
//                log.debug("the key you put is null ");
                return null;
            }
            jedisResource = pool.getResource();
            jedisResource.auth(auth);
            byte[] o = jedisResource.get(key.getBytes());
            return SerializeUtil.unserialize(o);
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
//                log.debug("the key you put is null ");
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
//            log.error("{} -> {}" ,"incr() failed: " , sb.toString());
            return null;
        } finally {
            if (jedisResource != null) {
                jedisResource.close();
            }
        }
    }

//  public Jedis getJedis() {
//	  Jedis jedisSamp;
//    if (null != pool) {
//      try {
//        jedisSamp = pool.getResource();
//        return jedisSamp;
//      }  catch (JedisException e) {
//          broken = handleJedisException(e);
//          return null;
//      }  catch (Throwable t) {
//        log.error(Throwables.getStackTraceAsString(t));
//        return null;
//      }
//    } else {
//      log.debug("redis poll is null");
//      return null;
//    }
//  }
//  
//  /**
//   * Handle jedisException, write log and return whether the connection is broken.
//   */
//  protected boolean handleJedisException(JedisException jedisException) {
//      if (jedisException instanceof JedisConnectionException) {
//          log.error("{} -> {}" ,"Redis connection lost." , jedisException);
//      } else if (jedisException instanceof JedisDataException) {
//          if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
//              log.error("{} -> {}" ,"Redis connection  are read-only slave." , jedisException);
//          } else {
//              // dataException, isBroken=false
//              return false;
//          }
//      } else {
//          log.error("{} -> {}" ,"Jedis exception happen." , jedisException);
//      }
//      return true;
//  }
//  /**
//   * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
//   */
//  public void release(Jedis jedis) {
//    if (null != jedis && null != pool) 
//        try {
//            if (broken) {
//                pool.returnBrokenResource(jedis);
//            } else {
//                pool.returnResource(jedis);
//            }
//        } catch (Exception e) {
//            log.error("{} -> {}" ,"return back jedis failed, will fore close the jedis." , e);
//        }
//    	//pool.returnResource(jedis);
//  }
//  


  public static void main(String[] args) {

      List<Jedis> jediss = new ArrayList<Jedis>();

      RedisClient rc1 = new RedisClient();
      rc1.init("60.166.12.158", 6379, 10000, 100, 20, 10, false, "xylxredis123!@#mima");
      Jedis jedis1 = rc1.getJedis();
      jediss.add(jedis1);

      RedisClient rc2 = new RedisClient();
      rc2.init("60.166.12.158", 6380, 10000, 100, 20, 10, false, "xylxredis123!@#mima");
      Jedis jedis2 = rc2.getJedis();
      jediss.add(jedis2);

      ReadKey readKey = new ReadKey();
      List<String> list = readKey.readFileByLines("/Users/jrshi/Downloads/mobile_20170930_302.txt");
      for(String pnumber : list){
          int index = 0;
          for(Jedis jedis : jediss){

              String value = jedis.get(pnumber);
              if(value != null){
                  System.out.println(pnumber + "  :  " + value);
                  break;
              }else {
                  index++;
                  if(index == jediss.size()) {
                      System.out.println(pnumber + "  :  " + value);
                  }
                  continue;
              }
          }
      }
      rc1.release(jedis1);
      rc1.fini();
      rc2.release(jedis2);
      rc2.fini();
  }
}
