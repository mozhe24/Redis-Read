package ReadRedis;



import java.util.List;
import java.util.Map;

/**
 * Created by w7 on 2017/6/22.
 */
public class AssignInfoCache {
    //private static final Logger logger = LoggerFactory.getLogger(AssignInfoCache.class);
    private volatile static Map<String,List<String>> maps = null;

    public static Map<String,List<String>> getMaps(){
       return maps;
    }

    public synchronized static void setMaps(Map<String,List<String>> maps){
        if(maps == null){
            System.out.println("Cached fail when setted Assigninfo maps cause a error!");
        }

        AssignInfoCache.maps = maps;
    }

}
