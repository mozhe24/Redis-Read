package com.iflytek.jrshi.ViewRedis;

import com.iflytek.jrshi.ViewRedis.redis.*;
import com.iflytek.jrshi.ViewRedis.redis.proxy.RedisClientProxy;
import com.iflytek.jrshi.ViewRedis.redis.proxy.RedisClientProxyConfig;
import com.sun.deploy.util.StringUtils;
import redis.clients.jedis.Jedis;
import com.iflytek.jrshi.ViewRedis.redis.RedisClient;
import java.util.*;

/**
 * Created by jrshi on 17/10/9.
 */
public class Main {
    public static void main(String[] args) {
        Set<String> nodes = new HashSet<String>();
        nodes.add("60.166.12.158:6380:8");
        nodes.add("60.166.12.158:6379:100");
        nodes.add("60.166.12.158:22122:0");
        nodes.add("60.166.12.158:22126:1");

        RedisClientProxyConfig config = new RedisClientProxyConfig();
        config.setNodes(nodes);
        config.setPassword("xylxredis123!@#mima");
        // 其他参数酌情设置
        Map<String, List<String>> userTagMap = new HashMap<String, List<String>>();

        RedisClientProxy proxy = new RedisClientProxy();
        if (!proxy.init(config)) {
            System.out.println("init failed");
        }


        ReadKey readKey = new ReadKey();
        List<String> list = readKey.readFileByLines("E:/study/1.txt");
        String afterstr=null;
        for(String str : list){
            if (str.length()==11) {
                System.out.println("This key belongs to phone number!!!");
                afterstr=str;
            }
            else if (str.length()==32){
                System.out.println("This is an imei after MD5!!!");
                afterstr=str;
            }
            BitSet bitsetTag=BitSet.valueOf((proxy.get(afterstr).getBytes()));

            for (int sourceIndex=0;sourceIndex<480;sourceIndex++) {
                if (bitsetTag.get(sourceIndex)) {
                    int mod = sourceIndex % 8;
                    int targetIndex = (sourceIndex - mod - 1) + (8 - mod);


                    String tagcode = CacheDispatch.getTagsCodeMap().get(targetIndex);
                    String codeType = tagcode;

                    if ((!org.apache.commons.lang.StringUtils.isBlank(codeType) && codeType.length() > 8)) {
                        codeType = codeType.substring(0, 8);
                        System.out.println("    the index: " + targetIndex+" the codeType: "+codeType);
                    } else {
                        continue;
                    }

                    List<String> codeList = userTagMap.get(codeType);
                    if (null != codeList) {
                        codeList.add(tagcode);
                    } else {
                        codeList = new ArrayList<String>();
                        codeList.add(tagcode);
                        userTagMap.put(codeType, codeList);
                    }
                }
            }

            }

        proxy.finish();

    }
}
