package cdccm.pojo;

public class ActivityPOJO {
	
	private int activityId;
	private String name;
	private int ageGroupId;
	private int sessionId;
	private int providerId;
	private String description;
	
	
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge_group() {
		return ageGroupId;
	}
	public void setAge_group(int age_group) {
		this.ageGroupId = age_group;
	}
	public int getSession() {
		return sessionId;
	}
	public void setSession(int session) {
		this.sessionId = session;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

}