/**
 * 
 */
package ReadRedis;


import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * 创意信息数据缓存类
 * @author zlyan
 * @time 2017年3月7日 下午1:47:28
 */
public class CreativeInfoCache {
	private static final Logger log = Logger.getLogger(CreativeInfoCache.class);
	
	/*
	 * 缓存创意信息
	 * key: 创意ID  value： 创意信息
	 */
	private volatile static Map<Integer, AdxDirectadCreativeInfo> creativeInfoMap = null;
	
	/*
	 * 缓存投放和创意之间的关系
	 * key: 投放ID  value： 创意ID集合（一个投放可能有多个创意）
	 */
	private volatile static Map<Integer, Set<Integer>> mediaBuyIdToCreativeIdsMap = null;

	public static Map<Integer, AdxDirectadCreativeInfo> getCreativeInfoMap() {
		return creativeInfoMap;
	}

	public synchronized static void setCreativeInfoMap(Map<Integer, AdxDirectadCreativeInfo> creativeInfoMap) {
		if (creativeInfoMap == null) {
			log.error("setCreativeInfoMap(): creativeInfoMap == null");
			return;
		}
		CreativeInfoCache.creativeInfoMap = creativeInfoMap;
	}

	public static Map<Integer, Set<Integer>> getMediaBuyIdToCreativeIdsMap() {
		return mediaBuyIdToCreativeIdsMap;
	}

	public synchronized static void setMediaBuyIdToCreativeIdsMap(Map<Integer, Set<Integer>> mediaBuyIdToCreativeIdsMap) {
		if (mediaBuyIdToCreativeIdsMap == null) {
			log.error("setMediaBuyIdToCreativeIdsMap(): mediaBuyIdToCreativeIdsMap == null");
			return;
		}
		CreativeInfoCache.mediaBuyIdToCreativeIdsMap = mediaBuyIdToCreativeIdsMap;
	}
	
}
