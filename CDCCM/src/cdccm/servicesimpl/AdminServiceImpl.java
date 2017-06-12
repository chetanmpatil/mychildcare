package cdccm.servicesimpl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildIdAgeGroupId;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ChildReportPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;
import cdccm.pojo.SchedulePOJO;
import cdccm.serviceApi.AdminService;
import cdccm.utilities.CdccmUtilities;
import cdccm.utilities.FileNameGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class AdminServiceImpl implements AdminService {

	private MySQLDBConnector dbConnector;
	Scanner inputChoice = new Scanner(System.in);
	public AdminServiceImpl() {
		dbConnector = MySQLDBConnector.getInstance();
	}
   @Override
	public void insertChildDetails(ChildPOJO childPOJO) throws SQLException {
		int age = CdccmUtilities.getAge(childPOJO.getDob());

		// on the basis of age_calculartor utility we can decide age group, no
		// additional query to get fk_age_group
		int ageGroup = 2;
		int resultCountChild = dbConnector
				.insert("INSERT INTO CHILD_INFO(name,surname,dob,age,fk_age_group,fk_idparent) VALUES('"
						+ childPOJO.getFirst_name() + "','" + childPOJO.getLast_name() + "','" + childPOJO.getDob()
						+ "','" + age + "','" + ageGroup + "'," + "(SELECT MAX(IDPARENT) from PARENT)" + ")");

		if (resultCountChild > 0)
			System.out.println("Child Record Inserted Successfully");
		else
			System.out.println("Error Inserting Record Please Try Again");
	}

	public void insertParentDetails(ParentPOJO parentPOJO, ContactPOJO contactPOJO) throws SQLException {

		int resultCountParent = dbConnector.insert("INSERT INTO PARENT(name, surname) VALUES('"
				+ parentPOJO.getParentFirst_name() + "','" + parentPOJO.getParentLast_name() + "')");

		int resultCountContact = dbConnector
				.insert("INSERT INTO CONTACT(street,city,pincode,phone_number,emailid,fk_idparent) VALUES('"
						+ contactPOJO.getStreet() + "','" + contactPOJO.getCity() + "','" + contactPOJO.getPincode()
						+ "','" + contactPOJO.getPhoneNumber() + "','" + contactPOJO.getEmail() + "',"
						+ "(SELECT MAX(IDPARENT) from PARENT)" + ")");

		if ((resultCountParent > 0) && (resultCountContact > 0))
			System.out.println("Parent Record Inserted Successfully");
		else
			System.out.println("Error Inserting Record Please Try Again");
	}

	@Override
	public ResultSet listAllChild() throws SQLException {
		ResultSet childrenList;
		childrenList = dbConnector.query("SELECT * FROM CHILD_INFO");
		return childrenList;
	}

	@Override
	public void insertCareProvider(CareProviderPOJO careProviderPOJO) {

		int resultCountProvider;
		try {
			resultCountProvider = dbConnector.insert(
					"INSERT INTO CARE_PROVIDER(name, emailid, phone_number) VALUES('" + careProviderPOJO.getName()
							+ "','" + careProviderPOJO.getEmail() + "','" + careProviderPOJO.getPhoneNumber() + "')");
			if ((resultCountProvider > 0))
				System.out.println("Care Provider Record Inserted Successfully");
			else
				System.out.println("Error Inserting Record Please Try Again");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateActivityToChild(AssignActivityPOJO assignActivityPOJO) {
		String updateQuery = "";
		try {
			ResultSet resultSet = dbConnector.query("select * from activity where fk_age_group ="
					+ assignActivityPOJO.getAgeGroup() + " AND fk_session = " + assignActivityPOJO.getSession());
			System.out.println("\n------------Activities and Care Provider Available For Your Child----------");
			while (resultSet.next()) {
				System.out.println("Activity ID: " + resultSet.getInt("idactivity"));
				System.out.println("Activity Name: " + resultSet.getString("activity_name"));
				System.out.println("Care Provider ID: " + resultSet.getInt("fk_idcareprovider"));
				System.out.println("Date Of Birth: " + resultSet.getString("activity_description"));
			}

			System.out.println("\nSelect Activity ID available for your child");
			assignActivityPOJO.setActivityID(inputChoice.nextInt());
			System.out.println("\nSelect Care Provider ID available for your child");
			assignActivityPOJO.setCareProviderID(inputChoice.nextInt());
			updateQuery = "UPDATE REPORT SET fk_idactivity = " + assignActivityPOJO.getActivityID()
					+ ", fk_idprovider= " + assignActivityPOJO.getCareProviderID() + " WHERE fk_idchild = "
					+ assignActivityPOJO.getChildID() + " AND fk_idsession = " + assignActivityPOJO.getSession();
			dbConnector.insert(updateQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void assignActivityToChild(AssignActivityPOJO assignActivityPOJO) {

		int recordInsert;
		try {
			ResultSet resultSet = dbConnector
					.query("select idactivity,activity_name, fk_idcareprovider,activity_description from activity where fk_age_group ="
							+ assignActivityPOJO.getAgeGroup() + " AND fk_session = "
							+ assignActivityPOJO.getSession());
			System.out.println("\n------------Activities and Care Provider Available For Child----------\n");
			while (resultSet.next()) {
				System.out.println("Activity ID: " + resultSet.getInt("idactivity"));
				System.out.println("Activity Name: " + resultSet.getString("activity_name"));
				System.out.println("Care Provider ID: " + resultSet.getInt("fk_idcareprovider"));
				System.out.println("Date Of Birth: " + resultSet.getString("activity_description"));
				System.out.println("");
			}
			System.out.println("\nSelect Activity ID available for your child");
			assignActivityPOJO.setActivityID(inputChoice.nextInt());
			System.out.println("\nSelect Care Provider ID available for your child");
			assignActivityPOJO.setCareProviderID(inputChoice.nextInt());
			recordInsert = dbConnector
					.insert("Insert INTO REPORT (fk_idchild,fk_idagegroup,fk_idactivity,fk_idprovider,fk_idsession) VALUES('"
							+ assignActivityPOJO.getSession() + "','" + assignActivityPOJO.getAgeGroup() + "','"
							+ assignActivityPOJO.getActivityID() + "','" + assignActivityPOJO.getCareProviderID()
							+ "','" + assignActivityPOJO.getSession() + "')");
			if (recordInsert > 0) {
				System.out.println("Record Inserted In Report Table\n");
			} else {
				System.out.println("Record Not Inserted\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectSchedule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectNewsEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateReport(int childid) {
		Collection<ChildReportPOJO> listofscore = new ArrayList<>();
		System.out.println("gerenating report for Child " + childid);
		ResultSet childresult;
//		String sql = "select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name,ds.session_name as sessionName,"
//				+ "ifnull(r.MON,0) as MON,ifnull(r.TUE,0) as TUE,ifnull(r.WEN,0)as WEN,ifnull(r.THU,0) as THU,ifnull(r.FRI,0) as FRI,"
//				+ "(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total,"
//				+ "cast(((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as decimal(5,2)) as Percentage1"
//				+ " from report r join child_info ci join day_session ds join age_group ag join activity a on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group and r.fk_idactivity=a.idactivity) "
//				+ "where r.fk_idchild=? " + "group by ci.idchild,ds.session_name;";

		String sql="select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name,ds.session_name as sessionName,cp.name,r.care_provider_feedback "
				  +"from report r join child_info ci join day_session ds join age_group ag join activity a join care_provider cp "
				  +"on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group and r.fk_idactivity=a.idactivity and r.fk_idprovider=cp.idcare_provider) "
				  +"where r.fk_idchild=? " + "group by ci.idchild,ds.session_name;";
		try {
			ResultSet resultset = dbConnector.getReport(sql, childid);
			while (resultset.next()) {
//				listofscore.add(new ChildReportPOJO(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
//						resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7),
//						resultset.getInt(8), resultset.getInt(9), resultset.getInt(10), resultset.getInt(11),
//						resultset.getInt(12), resultset.getInt(13), (float) resultset.getDouble(14)));
				
				listofscore.add(new ChildReportPOJO(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
						resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7),
						resultset.getString(8), resultset.getString(9)));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		/* call for printing the */
		printPerformanceReport(listofscore, 0);
	}

	@Override
	public void generateBulckPerformanceReport() {
		Collection<ChildReportPOJO> listofscore = new ArrayList<>();
		Collection<ChildReportPOJO> subsetoflistofscore = new ArrayList<>();
		System.out.println("gerenating report for Children ");
		ResultSet childresult;
		String sql="select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name,ds.session_name as sessionName,cp.name,r.care_provider_feedback "
				  +"from report r join child_info ci join day_session ds join age_group ag join activity a join care_provider cp "
				  +"on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group and r.fk_idactivity=a.idactivity and r.fk_idprovider=cp.idcare_provider) "
				  + "group by ci.idchild,ds.session_name;";
		try {
			ResultSet resultset = dbConnector.query(sql);

			while (resultset.next()) {
//				listofscore.add(new ChildReportPOJO(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
//						resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7),
//						resultset.getInt(8), resultset.getInt(9), resultset.getInt(10), resultset.getInt(11),
//						resultset.getInt(12), resultset.getInt(13), (float) resultset.getDouble(14)));
				
				listofscore.add(new ChildReportPOJO(resultset.getInt(1), resultset.getString(2), resultset.getString(3),
						resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7),
						resultset.getString(8), resultset.getString(9)));
				System.out.println(resultset.getInt(1) + "" + resultset.getString(2));
			}
			Iterator<ChildReportPOJO> it = listofscore.iterator();
			ChildReportPOJO temp = (ChildReportPOJO) it.next();

			int tempp = temp.getChildid();
			int sizeof_total_result = listofscore.size();
			int counter = 0;

			for (ChildReportPOJO p : listofscore) {
				counter += 1;
				if (p.getChildid() == tempp) {
					subsetoflistofscore.add(p);

					if (counter == sizeof_total_result) {
						// check whether it is a last batch and then make call
						// to printing process,without this checking loop will
						// never reach to last batch
						printPerformanceReport(subsetoflistofscore, 0);
						subsetoflistofscore.clear();
						subsetoflistofscore.add(p);
					}
				} else {
					tempp = p.getChildid();
					System.out.println(p.getChildid());
					// take the batch and send for printing
					printPerformanceReport(subsetoflistofscore, 0);
					// clear the list for next batch and put the first element
					// from new batch which has already taken out for comparison
					// in "tempp"
					subsetoflistofscore.clear();
					subsetoflistofscore.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void assignActivityToChild(int childId) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void assignActivitiesToChildren() throws SQLException {
		int ageGroup, session;
		String childId = "";
		ResultSet chilIdList;
		try {
			for (ageGroup = 1; ageGroup <= 3; ageGroup++) {
				chilIdList = dbConnector
						.query("SELECT idchild,fk_age_group FROM CHILD_INFO where fk_age_group=" + ageGroup);
				while (chilIdList.next()) {
					if (ageGroup == 1) {
						for (session = 1; session < 4; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + chilIdList.getString("IDCHILD")
									+ " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 1, fk_idactivity = 1 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 3, fk_idactivity = 3 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 5, fk_idactivity = 5 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
					if (ageGroup == 2) {
						for (session = 1; session <= 3; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 2, fk_idactivity = 8 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 4, fk_idactivity = 10 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 6, fk_idactivity = 12 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
					if (ageGroup == 3) {
						for (session = 1; session <= 3; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 2, fk_idactivity = 14 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 4, fk_idactivity = 16 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 6, fk_idactivity = 18 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	@Override
	public boolean displayChild(int id) {
		ResultSet resultSetChild = null;
		boolean recordExists = false;
		try {
			resultSetChild = dbConnector.query("SELECT * FROM CHILD_INFO WHERE IDCHILD = " + id);
			if (resultSetChild.next()) {
				System.out.println("Child ID: " + resultSetChild.getString("idchild"));
				System.out.println("First Name: " + resultSetChild.getString("name"));
				System.out.println("Last Name: " + resultSetChild.getString("surname"));
				System.out.println("Date Of Birth: " + resultSetChild.getString("dob"));
				System.out.println("Age:" + resultSetChild.getString("age"));
				System.out.println("Age:" + resultSetChild.getString("fk_age_group"));
				recordExists = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}
	@Override
	public boolean displayParent(int id) {

		boolean recordExists = false;
		try {
			ResultSet resultSetParent = dbConnector.query("SELECT * FROM PARENT WHERE IDPARENT = " + id);

			if (resultSetParent.next()) {
				System.out.println("Parent First Name: " + resultSetParent.getString("name"));
				System.out.println("Parent Last Name: " + resultSetParent.getString("surname"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}
	@Override
	public void updateParentInfo(int parentId, ParentPOJO parentPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE PARENT SET ";
			updateQuery = updateQuery + "name = '" + parentPOJO.getParentFirst_name() + "', surname = '"
					+ parentPOJO.getParentLast_name() + "' WHERE IDPARENT = " + parentId;
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Parent Record Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateContactInfo(int parentId, ContactPOJO contactPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE CONTACT SET ";
			updateQuery = updateQuery + "street = '" + contactPOJO.getStreet() + "', city = '" + contactPOJO.getCity()
					+ "', pincode = " + contactPOJO.getPincode() + ", phone_number = '" + contactPOJO.getPhoneNumber()
					+ "', emailid = '" + contactPOJO.getEmail() + "'";
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Contact Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean displayContact(int id) {
		boolean recordExists = false;
		try {
			ResultSet resultSetContact = dbConnector.query("SELECT * FROM CONTACT WHERE FK_IDPARENT = " + id);
			if (resultSetContact.next()) {
				System.out.println("Street Name: " + resultSetContact.getString("street"));
				System.out.println("City Name: " + resultSetContact.getString("city"));
				System.out.println("Pincode: " + resultSetContact.getString("pincode"));
				System.out.println("Phone Number: " + resultSetContact.getString("phone_number"));
				System.out.println("Email Id: " + resultSetContact.getString("emailid"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}
	@Override
	public void updateCareProviderInfo(int careProviderId, CareProviderPOJO careProviderPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE CARE_PROVIDER SET ";
			updateQuery = updateQuery + "name = '" + careProviderPOJO.getName() + "' , emailid = '"
					+ careProviderPOJO.getEmail() + "', phone_number = '" + careProviderPOJO.getPhoneNumber()
					+ "' WHERE idcare_provider = " + careProviderId;
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Care Provider Record Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean displayCareProvider(int id) {

		boolean recordExists = false;
		try {
			ResultSet resultSetProvider = dbConnector
					.query("SELECT * FROM CARE_PROVIDER WHERE IDCARE_PROVIDER = " + id);
			if (resultSetProvider.next()) {
				System.out.println("Care Provider ID: " + resultSetProvider.getString("idcare_provider"));
				System.out.println("Name: " + resultSetProvider.getString("name"));
				System.out.println("Email Id: " + resultSetProvider.getString("emailid"));
				System.out.println("Phone number: " + resultSetProvider.getString("phone_number"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}
	@Override
	public void provideFeedback(ProviderFeedbackPOJO providerFeedbackPOJO) {
		try {
			int feedback = dbConnector
					.insert("INSERT INTO FEEDBACK(FK_IDPARENT,FK_IDCARE_PROVIDER,PARENT_FEEDBACK) VALUES('"
							+ providerFeedbackPOJO.getCareProviderId() + "','" + providerFeedbackPOJO.getParentId()
							+ "','" + providerFeedbackPOJO.getFeedback() + "')");
			if (feedback > 0) {
				System.out.println("Suggestions/Feedback Added Succesfully!!\n");
			} else {
				System.out.println("Suggestion/Feedback Not Added\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void insertMealDetails(FoodPOJO foodPOJO) {

		int resultCountFood;
		try {
			resultCountFood = dbConnector.insert(
					"INSERT INTO FOOD(day, breakfast, lunch,snak) VALUES('" + foodPOJO.getDay()
							+ "','" + foodPOJO.getBreakfast()+ "','" + foodPOJO.getLunch() + "','" + foodPOJO.getSnack() + "')");
			if ((resultCountFood > 0))
				System.out.println("Food Record Inserted Successfully\n");
			else
				System.out.println("Error Inserting Record Please Try Again\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	@Override
	public void updateFood(FoodPOJO foodPOJO) {
	int resultUpdate = 0;
		
		String column_to_set,query_aux;
		
		if(foodPOJO.getBreakfast()!=null){
			column_to_set="breakfast";
			query_aux=foodPOJO.getBreakfast();
		}else if(foodPOJO.getLunch()!=null){
			column_to_set="lunch";
			query_aux=foodPOJO.getLunch();
		}else{
			query_aux=foodPOJO.getSnack();
			column_to_set="snak";
			}

		String updateQuery = "UPDATE food SET " +column_to_set;
		updateQuery = updateQuery  +" = '" + query_aux+ "' WHERE day = '" + foodPOJO.getDay()+"'";
				
		
				
		try {
			resultUpdate = dbConnector.insert(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultUpdate > 0) {
			System.out.println("Care Food Record Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");

		
	}

	@Override
	public void deleteMealDay(FoodPOJO foodPOJO) {
		int resultUpdate = 0;
		String updateQuery = "DELETE FROM FOOD ";
		updateQuery = updateQuery  + " WHERE day = '" + foodPOJO.getDay()+"'";
		
				
		try {
			resultUpdate = dbConnector.delete(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultUpdate > 0) {
			System.out.println("Food Deleted correctly!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");

		
	}

	@Override
	public void updateChildInfo(int id, ChildPOJO childPOJO) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateParentInfo(int parentID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCareProviderInfo(int careProviderID) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResultSet displayInfo(int id, String tableName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GenerateScheduleReport() {
		Set<ChildIdAgeGroupId> chid_ageid = getAvailableChilden();
		Collection<SchedulePOJO> schedule = null;
		ResultSet resultset = null;
		/* call to prcedure where it will compose the schedule for child */
		String sql = "{call child_care.update_plan_for_astudent(?, ?)}";
		Iterator<ChildIdAgeGroupId> it = chid_ageid.iterator();
		while (it.hasNext()) {
			ChildIdAgeGroupId cag = it.next();

			resultset = dbConnector.callProcedure(sql, cag.getChildid(), cag.getAgegroupid());
			schedule = new ArrayList<>();
			try {
				while (resultset.next()) {
					schedule.add(new SchedulePOJO(resultset.getString(1), resultset.getString(2)));
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			printScheduleReport(schedule, cag.getChildid());
		}
	}

	private void printScheduleReport(Collection<SchedulePOJO> schedule, int childid) {
		System.out.println("Inside Printreport");
		ReportFiller reportfiller = new ReportFiller(schedule);
		try {
			JasperPrint jp = reportfiller.getReport("schedulereport".toString(), childid);
			JasperViewer jasperViewer = new JasperViewer(jp);
			jasperViewer.setVisible(true);

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));

			/* util method for creating file with date and rollno name */
			FileNameGenerator filenamegererator = new FileNameGenerator(schedule, childid);
			File file = filenamegererator.generateUniqueFileName("schedulereport".toString());

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setMetadataAuthor("chetan"); // set some
														// config as we like
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void printPerformanceReport(Collection<ChildReportPOJO> subsetoflistofscore, int childid) {

		ReportFiller reportfiller = new ReportFiller(subsetoflistofscore);

		try {
			// the argument zero has no meaning just to satisfy the signature
			JasperPrint jp = reportfiller.getReport("performancereport".toString(), 0);

			JasperViewer jasperViewer = new JasperViewer(jp);
			jasperViewer.setVisible(true);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));
			// the second field (0) here has no meaning just to satisfy
			// signatuere
			FileNameGenerator filenamegererator = new FileNameGenerator(subsetoflistofscore, 0);

			// util method for creating file with date name
			File file = filenamegererator.generateUniqueFileName("performancereport".toString());

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			// set some config as we like
			configuration.setMetadataAuthor("chetan");

			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	private Set<ChildIdAgeGroupId> getAvailableChilden() {
		// This supportive method returns the availlable childId and GroupId
		// from report(where schedule is already made) table
		Set<ChildIdAgeGroupId> chid_ageid = new HashSet<>();

		final String sql = "select distinct fk_idchild,fk_idagegroup from report group by fk_idchild;";

		try {
			ResultSet resultset = dbConnector.query(sql);
			// ChildIdAgeGroupId chid_ageid
			while (resultset.next()) {
				chid_ageid.add(new ChildIdAgeGroupId(resultset.getInt(1), resultset.getInt(2)));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return chid_ageid;
	}

}
