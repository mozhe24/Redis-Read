/**
 * 
 */
package ReadRedis;

import java.util.Map;
import java.util.Set;

/**
 * 直投数据缓存类
 * 
 * @author zlyan
 * @time 2017年3月3日 下午2:22:34
 */
public class MediaBuyInfoCache {

	//private static final Logger log = Logger.getLogger(MediaBuyInfoCache.class);

	private volatile static MediaBuyInfoData mediaBuyInfoData = new MediaBuyInfoData();

	public static MediaBuyInfoData getMediaBuyInfoData() {
		return mediaBuyInfoData;
	}

	public synchronized static void setMediaBuyInfoData(MediaBuyInfoData md) {
		if (md == null) {
			System.out.println("setMediaBuyInfoData(): mediaBuyInfoData == null");
			return;
		}
		MediaBuyInfoCache.mediaBuyInfoData = md;
	}

	public static Map<String, Set<Integer>> getMediaBuyDailyInfoMap() {
		return mediaBuyInfoData.getMediaBuyDailyInfoMap();
		//return mediaBuyDailyInfoMap;
	}

	public synchronized static void setMediaBuyDailyInfoMap(Map<String, Set<Integer>> mediaBuyDailyInfoMap) {
		if (mediaBuyDailyInfoMap == null) {
			System.out.println("setMediaBuyDailyInfoMap(): mediaBuyDailyInfoMap == null");
			return;
		}
	}

	public static Map<Integer, DeliverInfo> getMediaBuyDailyDeliverInfoMap() {
		return mediaBuyInfoData.getMediaBuyDailyDeliverInfoMap();
		//return mediaBuyDailyDeliverInfoMap;
	}

	public synchronized static void setMediaBuyDailyDeliverInfoMap(
			Map<Integer, DeliverInfo> mediaBuyDailyDeliverInfoMap) {
		if (mediaBuyDailyDeliverInfoMap == null) {
			System.out.println("setMediaBuyDailyDeliverInfoMap(): mediaBuyDailyDeliverInfoMap == null");
			return;
		}
	}

	public static Map<Integer, AdxDirectadMediabuyInfo> getMediaBuyInfoMap() {
		return mediaBuyInfoData.getMediaBuyInfoMap();
		//return mediaBuyInfoMap;
	}

	public synchronized static void setMediaBuyInfoMap(Map<Integer, AdxDirectadMediabuyInfo> mediaBuyInfoMap) {
		if (mediaBuyInfoMap == null) {
			System.out.println("setMediaBuyInfoMap(): mediaBuyInfoMap == null");
			return;
		}
	}

	public static Map<Integer, Set<Integer>> getAdunitToMediaBuyMap() {
		return mediaBuyInfoData.getAdunitToMediaBuyMap();
		//return adunitToMediaBuyMap;
	}

	public synchronized static void setAdunitToMediaBuyMap(Map<Integer, Set<Integer>> adunitToMediaBuyMap) {
		if (adunitToMediaBuyMap == null) {
			System.out.println("setAdunitToMediaBuyMap(): adunitToMediaBuyMap == null");
			return;
		}
	}

	public static Map<String, Set<Integer>> getDateToMediaBuyIdsMap() {
		return mediaBuyInfoData.getDateToMediaIdsMap();
		//return dateToMediaBuyIdsMap;
	}

	public synchronized static void setDateToMediaBuyIdsMap(Map<String, Set<Integer>> dateToMediaBuyIdsMap) {
		if (dateToMediaBuyIdsMap == null) {
			System.out.println("setDateToMediaBuyIdsMap(): dateToMediaBuyIdsMap == null");
			return;
		}
	}

	public static Map<Integer, Map<String, Set<String>>> getMediaBuyDirectForwardMaps() {
		return mediaBuyInfoData.getForward();
		//return mediaBuyDirectForwardMaps;
	}



	public static Map<Integer, Map<String, Set<String>>> getMediaBuyDirectReverseMaps() {
		return mediaBuyInfoData.getReverse();

	}

	public synchronized static void setMediaBuyDirectReverseMaps(
			Map<Integer, Map<String, Set<String>>> mediaBuyDirectReverseMaps) {
		if (mediaBuyDirectReverseMaps == null) {
			System.out.println("setMediaBuyDirectReverseMaps(): mediaBuyDirectReverseMaps == null");
			return;
		}
	}

}
