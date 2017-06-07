package cdccm.pojo;

public class ChildReportPOJO {
private int childid;
private int mon;
private int tue;
private int wen;
private int thu;
private int fri;
private String name;
private String surname;
private String dateOfBirth;
private String ageGroup;
private String activityName;
private String sessionname;
private int total;
private float percentage;

public int getMon() {
	return mon;
}
public void setMon(int mon) {
	this.mon = mon;
}
public int getTue() {
	return tue;
}
public void setTue(int tue) {
	this.tue = tue;
}
public int getWen() {
	return wen;
}
public void setWen(int wen) {
	this.wen = wen;
}
public int getThu() {
	return thu;
}
public void setThu(int thu) {
	this.thu = thu;
}
public int getFri() {
	return fri;
}
public void setFri(int fri) {
	this.fri = fri;
}

public String getSessionname() {
	return sessionname;
}
public void setSessionname(String sessionname) {
	this.sessionname = sessionname;
}


public ChildReportPOJO(int childid, String name, String surname, String dateOfBirth, String ageGroup,
		String activityName,String sessionname,int mon,int tue,int wen,int thu,int fri, int total, float percentage) {
	
	this.childid = childid;
	this.name = name;
	this.surname = surname;
	this.dateOfBirth = dateOfBirth;
	this.ageGroup = ageGroup;
	this.activityName = activityName;
	this.sessionname=sessionname;
	this.total = total;
	this.percentage = percentage;
	this.mon=mon;
	this.tue=tue;
	this.wen=wen;
	this.thu=thu;
	this.fri=fri;
}
public int getChildid() {
	return childid;
}
public void setChildid(int childid) {
	this.childid = childid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSurname() {
	return surname;
}
public void setSurname(String surname) {
	this.surname = surname;
}
public String getDateOfBirth() {
	return dateOfBirth;
}
public void setDateOfBirth(String dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
}
public String getAgeGroup() {
	return ageGroup;
}
public void setAgeGroup(String ageGroup) {
	this.ageGroup = ageGroup;
}
public String getActivityName() {
	return activityName;
}
public void setActivityName(String activityName) {
	this.activityName = activityName;
}
public int getTotal() {
	return total;
}
public void setTotal(int total) {
	this.total = total;
}
public float getPercentage() {
	return percentage;
}
public void setPercentage(float percentage) {
	this.percentage = percentage;
}

}
