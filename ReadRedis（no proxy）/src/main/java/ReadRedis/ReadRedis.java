package ReadRedis;


import com.sun.org.apache.xpath.internal.SourceTree;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.JedisPool;


import java.util.*;


/**
 * Created by caodongj on 2017/10/13.
 */
public class ReadRedis {


    public static void main(String[] args) throws Exception{
        Map<String, List<String>> userTagMap = new HashMap<String, List<String>>();

        RedisClient rc = new RedisClient();
        rc.init("60.166.12.158", 6379, 1000000, 100000, 10, 50, false,"xylxredis123!@#mima");
        Jedis jedis = rc.getJedis();
        if (jedis == null) {
            System.out.println("jedis get failed!!!");
        }
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入字符串");
        String str=scanner.nextLine();
        String afterstr=null;
        if (str.length()==11){
            System.out.println("This key belongs to phone number!!!");
            afterstr=str;
        }
        else if (str.length()==32){
            System.out.println("This is a imel after MD5!!!");
            afterstr=str;
        }
        else if (str.length()<=16 && str.length()>=14){
            System.out.println("This is a imel before MD5!!! Please excute MD5 operation!!!");

            afterstr=MD5.Md5(str);
        }
        else{
            System.out.println("This is wrong input!!!,Please make sure your input is correct!!!");
        }
        BitSet bitSetTag = BitSet.valueOf(jedis.get(afterstr.getBytes()));


        for (int sourceIndex = 0; sourceIndex < 480; sourceIndex++) {
            if (bitSetTag.get(sourceIndex)) {
                int mod = sourceIndex % 8;
                int targetIndex = (sourceIndex - mod - 1) + (8 - mod);
                System.out.println("index:" + targetIndex);

                String tagcode=CacheDispatch.getTagsCodeMap().get(targetIndex);

                String codeType = tagcode;

                if ((!StringUtils.isBlank(codeType) && codeType.length()>8)){
                    codeType=codeType.substring(0,8);
                    System.out.println(codeType);
                } else{
                    continue;
                }

                List<String> codeList=userTagMap.get(codeType);
                if (null!=codeList){
                    codeList.add(tagcode);
                }
                else{
                    codeList=new ArrayList<String>();
                    codeList.add(tagcode);
                    userTagMap.put(codeType,codeList);
                }
            }
        }

        rc.release(jedis);
    }
}


