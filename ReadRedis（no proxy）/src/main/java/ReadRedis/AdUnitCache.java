/**
 * 
 */
package ReadRedis;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 广告位的CPD轮播数
 * @author zlyan
 * @time 2017年3月13日 下午3:33:54
 */
public class AdUnitCache {
	
	private static final Logger log = Logger.getLogger(AdUnitCache.class);
	
	private volatile static Map<String, String> adCountMap = null;

	public static Map<String, String> getAdCountMap() {
		return adCountMap;
	}

	public synchronized static void setAdCountMap(Map<String, String> adCountMap) {
		if (adCountMap == null) {
			log.error("setAdCountMap(): adCountMap == null");
			return;
		}
		AdUnitCache.adCountMap = adCountMap;
	}
	
}
