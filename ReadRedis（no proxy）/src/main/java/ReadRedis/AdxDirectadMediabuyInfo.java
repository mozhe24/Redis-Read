package ReadRedis;


import java.math.BigDecimal;
import java.util.Date;

public class AdxDirectadMediabuyInfo {
  private Integer id;
  private int activityId;
  private String name;
  private Date startDate;
  private Date endDate;
  private String adunitIds;
  private int buyType;
  private String monitorUrls;
  private int frequencyTimes;
  private int frequencyPeriod;
  private byte deliverType;
  private byte deliverSpeed;
  private byte deliverPriority;
  private byte deliverWeight;
  private BigDecimal costPrice;
  private Boolean status;
  private Integer flowStatus;
  private String timeSlot;
  private short mbhPriority;
  private String additional;
  // private List<DeliverInfo> deliverInfos;
  private Date createdAt;
  private Date updatedAt;
  private int deliverInterval;

  public AdxDirectadMediabuyInfo() {
  }

  public AdxDirectadMediabuyInfo(int activityId,
                                 String name,
                                 String adunitIds,
                                 String monitorUrls,
                                 int frequencyTimes,
                                 int frequencyPeriod,
                                 byte deliverType,
                                 byte deliverSpeed,
                                 byte deliverPriority,
                                 byte deliverWeight,
                                 BigDecimal costPrice,
                                 Date updatedAt) {
    this.activityId = activityId;
    this.name = name;
    this.adunitIds = adunitIds;
    this.monitorUrls = monitorUrls;
    this.frequencyTimes = frequencyTimes;
    this.frequencyPeriod = frequencyPeriod;
    this.deliverType = deliverType;
    this.deliverSpeed = deliverSpeed;
    this.deliverPriority = deliverPriority;
    this.deliverWeight = deliverWeight;
    this.costPrice = costPrice;
    this.updatedAt = updatedAt;
  }

  public AdxDirectadMediabuyInfo(int activityId,
                                 String name,
                                 Date startDate,
                                 Date endDate,
                                 String adunitIds,
                                 String monitorUrls,
                                 int frequencyTimes,
                                 int frequencyPeriod,
                                 byte deliverType,
                                 byte deliverSpeed,
                                 byte deliverPriority,
                                 byte deliverWeight,
                                 BigDecimal costPrice,
                                 Boolean status,
                                 Integer flowStatus,
                                 String timeSlot,
                                 Date createdAt,
                                 Date updatedAt) {
    this.activityId = activityId;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.adunitIds = adunitIds;
    this.monitorUrls = monitorUrls;
    this.frequencyTimes = frequencyTimes;
    this.frequencyPeriod = frequencyPeriod;
    this.deliverType = deliverType;
    this.deliverSpeed = deliverSpeed;
    this.deliverPriority = deliverPriority;
    this.deliverWeight = deliverWeight;
    this.costPrice = costPrice;
    this.status = status;
    this.flowStatus = flowStatus;
    this.timeSlot = timeSlot;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getActivityId() {
    return this.activityId;
  }

  public void setActivityId(int activityId) {
    this.activityId = activityId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getAdunitIds() {
    return this.adunitIds;
  }

  public void setAdunitIds(String adunitIds) {
    this.adunitIds = adunitIds;
  }

  public String getMonitorUrls() {
    return this.monitorUrls;
  }

  public void setMonitorUrls(String monitorUrls) {
    this.monitorUrls = monitorUrls;
  }

  public int getFrequencyTimes() {
    return this.frequencyTimes;
  }

  public void setFrequencyTimes(int frequencyTimes) {
    this.frequencyTimes = frequencyTimes;
  }

  public int getFrequencyPeriod() {
    return this.frequencyPeriod;
  }

  public void setFrequencyPeriod(int frequencyPeriod) {
    this.frequencyPeriod = frequencyPeriod;
  }

  public byte getDeliverType() {
    return this.deliverType;
  }

  public void setDeliverType(byte deliverType) {
    this.deliverType = deliverType;
  }

  public byte getDeliverSpeed() {
    return this.deliverSpeed;
  }

  public void setDeliverSpeed(byte deliverSpeed) {
    this.deliverSpeed = deliverSpeed;
  }

  public byte getDeliverPriority() {
    return this.deliverPriority;
  }

  public void setDeliverPriority(byte deliverPriority) {
    this.deliverPriority = deliverPriority;
  }

  public byte getDeliverWeight() {
    return this.deliverWeight;
  }

  public void setDeliverWeight(byte deliverWeight) {
    this.deliverWeight = deliverWeight;
  }

  public BigDecimal getCostPrice() {
    return this.costPrice;
  }

  public void setCostPrice(BigDecimal costPrice) {
    this.costPrice = costPrice;
  }

  public Boolean getStatus() {
    return this.status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public Integer getFlowStatus() {
    return this.flowStatus;
  }

  public void setFlowStatus(Integer flowStatus) {
    this.flowStatus = flowStatus;
  }

  public String getTimeSlot() {
    return this.timeSlot;
  }

  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  /*
  public List<DeliverInfo> getDeliverInfos() {
    return deliverInfos;
  }
  */

  /*
  public void setDeliverInfos(List<DeliverInfo> deliverInfos) {
    this.deliverInfos = deliverInfos;
  }
  */

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public int getBuyType() {
    return buyType;
  }

  public void setBuyType(int buyType) {
    this.buyType = buyType;
  }

  public String getAdditional() {
    return additional;
  }

  public void setAdditional(String additional) {
    this.additional = additional;
  }

  public short getMbhPriority() {
    return mbhPriority;
  }

  public void setMbhPriority(short mbhPriority) {
    this.mbhPriority = mbhPriority;
  }

  public int getDeliverInterval () {
    return deliverInterval;
  }

  public void setDeliverInterval (int deliverInterval) {
    this.deliverInterval = deliverInterval;
  }
}
