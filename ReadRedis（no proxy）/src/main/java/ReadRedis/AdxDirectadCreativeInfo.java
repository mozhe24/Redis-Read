package ReadRedis;


import java.util.Date;

public class AdxDirectadCreativeInfo {

     private Integer id;
     private String name;
     private int type;
     private String content;
     private String size;
     private int action;
     private int count;
     private String landingUrl;
     private int auditStatus;
     private String failedReson;
     private Date createdAt;
     private Date updatedAt;

    public AdxDirectadCreativeInfo() {
    }

	
    public AdxDirectadCreativeInfo(int type, int action) {
        this.type = type;
        this.action = action;
    }
    public AdxDirectadCreativeInfo(int type, String content, int action, String landingUrl, Date createdAt, Date updatedAt) {
       this.type = type;
       this.content = content;
       this.action = action;
       this.landingUrl = landingUrl;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAction() {
        return this.action;
    }
    
    public void setAction(int action) {
        this.action = action;
    }
    
    public String getLandingUrl() {
        return this.landingUrl;
    }
    
    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getFailedReson() {
		return failedReson;
	}

	public void setFailedReson(String failedReson) {
		this.failedReson = failedReson;
	}

}


