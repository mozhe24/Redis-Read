/**
 * 
 */
package com.iflytek.jrshi.ViewRedis;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存调度
 * 
 * @author zlyan
 * @time 2017年3月13日-下午5:19:57
 */
public class CacheDispatch {
	
	/*
	 * 广告位缓存初始化标识
	 */
	public static Boolean adUnitInitFlag = false;
	
	/*
	 * 配置缓存初始化标识
	 */
	public static Boolean configInitFlag = false;
	
	/*
	 * 创意缓存初始化标识
	 */
	public static Boolean creativeInitFlag = false;
	
	/*
	 * 投放信息缓存初始化标识
	 */
	public static Boolean mediaBuyInitFlag = false;

	public static Boolean assignInfoInitFlag = false;

	public static Boolean overviewInfoInitFlag = false;

	public static Boolean areaInfoInitFlag = false;

	/**
	 * 获取配置数据缓存对象
	 * 
	 * @return
	 */
	public static Map<String, String> getConfigMap() {
		return ConfigCache.getConfigMap();
	}
	/**
	 * 获取ASSIGN INFO 数据缓存对象
	 *
	 * @return
	 */
	public static Map<String, List<String>> getAssignInfoMap() {
		return AssignInfoCache.getMaps();
	}
	/**
	 * 获取广告位数据缓存对象
	 * 
	 * @return
	 */
	public static Map<String, String> getAdCountMap() {
		return AdUnitCache.getAdCountMap();
	}

	/**
	 * 获取创意缓存信息对象 key: 创意ID value： 创意信息
	 * 
	 * @return
	 */
	public static Map<Integer, AdxDirectadCreativeInfo> getCreativeInfoMap() {
		return CreativeInfoCache.getCreativeInfoMap();
	}

	/**
	 * 获取投放和创意之间的关系缓存数据对象
	 * 
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getMediaBuyIdToCreativeIdsMap() {
		return CreativeInfoCache.getMediaBuyIdToCreativeIdsMap();
	}

	/**
	 * 获取每个投放的具体信息的缓存对象
	 * 
	 * @return
	 */
	public static Map<Integer, AdxDirectadMediabuyInfo> getMediaBuyInfoMap() {
		return MediaBuyInfoCache.getMediaBuyInfoMap();
	}

	/**
	 * 获取广告位和投放的关联缓存对象
	 * 
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getAdunitToMediaBuyMap() {
		return MediaBuyInfoCache.getAdunitToMediaBuyMap();
	}

	/**
	 * 获取某天某个投放对应的所有控量信息缓存对象
	 * 
	 * @return
	 */
	public static Map<String, Set<Integer>> getMediaBuyDailyInfoMap() {
		return MediaBuyInfoCache.getMediaBuyDailyInfoMap();
	}

	/**
	 * 获取记录每个投放信息缓存数据对象
	 * 
	 * @return
	 */
	public static Map<Integer, DeliverInfo> getMediaBuyDailyDeliverInfoMap() {
		return MediaBuyInfoCache.getMediaBuyDailyDeliverInfoMap();
	}

	/**
	 * 获取每天所有投放的缓存数据对象
	 * 
	 * @return
	 */
	public static Map<String, Set<Integer>> getDateToMediaBuyIdsMap() {
		return MediaBuyInfoCache.getDateToMediaBuyIdsMap();
	}
	
	
	/**
	 * 地域代码与地域信息关联map
	 * 
	 * @return
	 */
	public static Map<String, String[]> getCodeToAreaMap() {
		
		return FileInfoCache.getCodeToAreaMap();
	}
	
	
	/**
	 * IP段与地域代码关联map
	 * 
	 * @return
	 */
	public static Map<String, String> getIpRangeToCodeMap() {
		return FileInfoCache.getIpRangeToCodeMap();
	}
	
	/**
	 * 获取标签编码接口
	 * 
	 * @return
	 */
	public static Map<Integer,String> getTagsCodeMap(){
		return FileInfoCache.getTagsCodeMap();
	}
	
	/**
	 * 获取正向定向信息
	 * 
	 * @return
	 */
	public static Map<Integer, Map<String, Set<String>>> getMediaBuyDirectForwardMap(){
		return MediaBuyInfoCache.getMediaBuyDirectForwardMaps();
	}
	
	/**
	 * 获取逆向定向信息
	 * 
	 * @return
	 */
	public static Map<Integer, Map<String, Set<String>>> getMediaBuyDirectReverseMap(){
		return MediaBuyInfoCache.getMediaBuyDirectReverseMaps();
	}
}
