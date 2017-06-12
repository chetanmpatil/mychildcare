package cdccm.serviceApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildNamePlate;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;

public interface AdminService {
	//void insertChildDetails(ChildPOJO childPOJO) throws SQLException, ParseException;
	boolean insertParentDetails(ParentPOJO parentPOJO) throws SQLException;
	void insertCareProvider(CareProviderPOJO careProviderPOJO);
	void assignActivityToChild(int childId) throws SQLException;
	void assignActivitiesToChildren() throws SQLException;
	void updateChildInfo(int id, ChildPOJO childPOJO) throws SQLException;
	void updateParentInfo(int parentID);
	void updateCareProviderInfo(int careProviderID);
	void selectReport();
	void selectSchedule();
	void selectNewsEvents();
	void generateReport(int childid) throws SQLException;
	void GenerateScheduleReport() throws SQLException;
	
	ResultSet listAllChild() throws SQLException;
	ResultSet displayInfo(int id,String tableName) throws SQLException;
	void generateBulckPerformanceReport();
	void updateActivityToChild(AssignActivityPOJO assignActivityPOJO);
	void assignActivityToChild(AssignActivityPOJO assignActivityPOJO);
	boolean displayChild(int id);
	boolean displayParent(int id);
	boolean displayContact(int id);
	void updateParentInfo(int parentId, ParentPOJO parentPOJO);
	void updateContactInfo(int parentID, ContactPOJO contactPOJO);
	boolean displayCareProvider(int id);
	void updateCareProviderInfo(int careProviderId, CareProviderPOJO careProviderPOJO);
	void provideFeedback(ProviderFeedbackPOJO providerFeedbackPOJO);
	void insertMealDetails(FoodPOJO foodPOJO);
	void deleteMealDay(FoodPOJO foodPOJO);
	void updateFood(FoodPOJO foodPOJO);
	boolean insertChildDetails(ParentPOJO parentpojo) throws SQLException, ParseException;
	

}
