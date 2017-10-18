package ReadRedis;

import java.util.Map;
import java.util.Set;

/**
 * Created by w7 on 2017/9/5.
 */
public class MediaBuyInfoData {
    private Map<Integer, Map<String, Set<String>>> forward = null;
    private Map<Integer, Map<String, Set<String>>> reverse = null;
    private Map<Integer, AdxDirectadMediabuyInfo> mediaBuyInfoMap = null;
    Map<Integer, Set<Integer>> adunitToMediaBuyMap = null;
    Map<String, Set<Integer>> mediaBuyDailyInfoMap =null;
    Map<String, Set<Integer>> dateToMediaIdsMap = null;
    Map<Integer, DeliverInfo> mediaBuyDailyDeliverInfoMap = null;

    public Map<Integer, Map<String, Set<String>>> getForward() {
        return forward;
    }

    public void setForward(Map<Integer, Map<String, Set<String>>> forward) {
        this.forward = forward;
    }

    public Map<Integer, Map<String, Set<String>>> getReverse() {
        return reverse;
    }

    public void setReverse(Map<Integer, Map<String, Set<String>>> reverse) {
        this.reverse = reverse;
    }

    public Map<Integer, AdxDirectadMediabuyInfo> getMediaBuyInfoMap() {
        return mediaBuyInfoMap;
    }

    public void setMediaBuyInfoMap(Map<Integer, AdxDirectadMediabuyInfo> mediaBuyInfoMap) {
        this.mediaBuyInfoMap = mediaBuyInfoMap;
    }

    public Map<Integer, Set<Integer>> getAdunitToMediaBuyMap() {
        return adunitToMediaBuyMap;
    }

    public void setAdunitToMediaBuyMap(Map<Integer, Set<Integer>> adunitToMediaBuyMap) {
        this.adunitToMediaBuyMap = adunitToMediaBuyMap;
    }

    public Map<String, Set<Integer>> getMediaBuyDailyInfoMap() {
        return mediaBuyDailyInfoMap;
    }

    public void setMediaBuyDailyInfoMap(Map<String, Set<Integer>> mediaBuyDailyInfoMap) {
        this.mediaBuyDailyInfoMap = mediaBuyDailyInfoMap;
    }

    public Map<String, Set<Integer>> getDateToMediaIdsMap() {
        return dateToMediaIdsMap;
    }

    public void setDateToMediaIdsMap(Map<String, Set<Integer>> dateToMediaIdsMap) {
        this.dateToMediaIdsMap = dateToMediaIdsMap;
    }

    public Map<Integer, DeliverInfo> getMediaBuyDailyDeliverInfoMap() {
        return mediaBuyDailyDeliverInfoMap;
    }

    public void setMediaBuyDailyDeliverInfoMap(Map<Integer, DeliverInfo> mediaBuyDailyDeliverInfoMap) {
        this.mediaBuyDailyDeliverInfoMap = mediaBuyDailyDeliverInfoMap;
    }
}
