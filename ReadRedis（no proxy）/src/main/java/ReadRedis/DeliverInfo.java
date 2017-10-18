package ReadRedis;

/**
 * 控量信息
 * @author zlyan
 * @time 2017年3月8日 下午1:52:33
 */
public class DeliverInfo {
	
	/*
	 * 控量对应的投放IDs
	 */
	private String mediaBuyIds;
	
	/*
	 * 投放的模式（0-cpm 1-cpc 2-cpd） 冗余字段，目前以mediabuy_info 中deliver_type为准
	 */
	private int deliverType;
	
	/*
	 * 投放的数量上限，具体和投放模式相关（单位：次）
	 */
	private int deliverLimit;
	
	/*
	 * 投放点击量上限
	 */
    private int deliverClick;
    
    /*
     * 投放曝光量上限
     */
    private int deliverImpression;
    
	public DeliverInfo(String mediaBuyIds, int deliverType, int deliverLimit, int deliverClick, int deliverImpression) {
		super();
		this.mediaBuyIds = mediaBuyIds;
		this.deliverType = deliverType;
		this.deliverLimit = deliverLimit;
		this.deliverClick = deliverClick;
		this.deliverImpression = deliverImpression;
	}
	public String getMediaBuyIds() {
		return mediaBuyIds;
	}
	public void setMediaBuyIds(String mediaBuyIds) {
		this.mediaBuyIds = mediaBuyIds;
	}
	public int getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(int deliverType) {
		this.deliverType = deliverType;
	}
	public int getDeliverLimit() {
		return deliverLimit;
	}
	public void setDeliverLimit(int deliverLimit) {
		this.deliverLimit = deliverLimit;
	}
	public int getDeliverClick() {
		return deliverClick;
	}
	public void setDeliverClick(int deliverClick) {
		this.deliverClick = deliverClick;
	}
	public int getDeliverImpression() {
		return deliverImpression;
	}
	public void setDeliverImpression(int deliverImpression) {
		this.deliverImpression = deliverImpression;
	}
	
}
