/**
 * 
 */
package ReadRedis;



import java.util.Map;

/**
 * ConfigCache.java
 * @author zlyan
 * @time 2017年3月6日 下午5:10:49
 */
public class ConfigCache {
	
	//private static final Logger log = Logger.getLogger(ConfigCache.class);
	
	private volatile static Map<String, String> configMap = null;

	public static Map<String, String> getConfigMap() {
		return configMap;
	}

	public synchronized static void setConfigMap(Map<String, String> configMap) {
		if (configMap == null) {
			//log.error("setConfigMap(): configMap == null");
			return;
		}
		ConfigCache.configMap = configMap;
	}
	
}
