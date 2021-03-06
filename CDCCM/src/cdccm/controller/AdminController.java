package cdccm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import cdccm.pojo.ActivityPOJO;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.servicesimpl.AdminServiceImpl;
import cdccm.servicesimpl.EmailService;
import cdccm.utilities.CdccmUtilities;
import cdccm.utilities.EmailValidator;;

public class AdminController {

	private Scanner inputScanner;
	private boolean choiceFlag = true;
	private AdminService adminService;
	private EmailService emailservice;

	public AdminController(Scanner inputScanner) {
		this.inputScanner = inputScanner;
		adminService = new AdminServiceImpl();
	}

	public void startOperations() throws SQLException, ParseException {
		do {
			System.out.println("User Logged In As Admin. \nNow Select An Operation To Perform");
			System.out.println(
					"1. Register A Child \n2. Register Care Provider \n3. Register Activity To All Children \n4. Register Activity For A Single Child \n5. Update Child, Prent or Care Provider Info \n6. List All Children \n7. Update Activity and Care Provider   \n8. Generate  Reports Of Child "
							+ "\n9. Send News/Events/report To Parent \n10. Provide Feedback  Of Care Provider \n11. Maintain Meal Program \n12. Main Menu.");
			int choice = 0;
			choice = inputScanner.nextInt();
			inputScanner.nextLine();
			switch (choice) {
			case 1:// done
				try {
					AddParent();
					System.out.println("Congratulations!! ");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:// done
				AddCareProvider();
				break;
			case 3:
				AddActivitiesToAllChildren();
				break;
			case 4:
				AddActivityToChild();
				break;
			case 5:
				UpdateRegistrationInfo();
				break;
			case 6:// done
				ShowAllChildren();
				break;
			case 7:
				UpdateChildActivity();
				break;
			case 8:
				GenerateReport();// chetan
				System.out.println("Number taken");
				break;
			case 9:// chetan
				SendNewsEventsReports();
				break;
			case 10:
				ProvideFeedback();
				break;
			case 11:
				CreateFoodProgram();
				break;
			case 12:
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}

		} while (choiceFlag);
	}

	private void UpdateChildActivity() {
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		boolean moreEntry = true;

		System.out.println("\nEnter Child ID To Update Activity ");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
		System.out.println("Enter Child's AgeGroup");
		assignActivityPOJO.setAgeGroup(inputScanner.nextInt());
		do {
			System.out.println(
					"Select For Which Session You Want To Update (Select 1 For Morning 2 For Afternoon 3 For Evening)");
			assignActivityPOJO.setSession(inputScanner.nextInt());
			adminService.updateActivityToChild(assignActivityPOJO);

			System.out.println("Do You Want To Update More Activity? Press Yes");
			String choice = inputScanner.nextLine().toUpperCase();
			if (choice.equals("YES")) {

			} else
				moreEntry = false;
		} while (moreEntry);

	}

	private void AddParent() throws SQLException, ParseException {

		// ParentPOJO parentPOJO = new ParentPOJO();
		// ContactPOJO contactPOJO = new ContactPOJO();
		//
		// System.out.println("++++++++++ Welcome To Parent Registration Portal
		// ++++++++++\n");
		// System.out.println("++++++++++ Please Enter Details Of
		// Parent++++++++++\n ");
		// System.out.println("Enter The First Name: ");
		// parentPOJO.setParentFirst_name(inputScanner.nextLine());
		// System.out.println("Enter The Last Name: ");
		// parentPOJO.setParentLast_name(inputScanner.nextLine());
		// System.out.println("Enter The Street Name: ");
		// contactPOJO.setStreet(inputScanner.nextLine());
		// System.out.println("Enter The City: ");
		// contactPOJO.setCity(inputScanner.nextLine());
		// System.out.println("Enter The Pincode: ");
		// contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
		// System.out.println("Enter The Email: ");
		// String emailString = inputScanner.nextLine();
		// EmailValidator email = new EmailValidator();
		// boolean isEmail = (email.validate(emailString));
		// if (isEmail == true) {
		// contactPOJO.setEmail(emailString);
		// } else
		// System.out.println("Not Valid Email Id");
		// System.out.println("Enter The Phone Number: ");
		// contactPOJO.setPhoneNumber(inputScanner.nextLine());
		// adminService.insertParentDetails(parentPOJO, contactPOJO);
		ParentPOJO parentPOJO = new ParentPOJO();

		List<ContactPOJO> contact = new ArrayList<>();

		System.out.println("++++++++++ Welcome To Parent Registration Portal ++++++++++\n");
		System.out.println("++++++++++ Please Enter Details Of Parent++++++++++\n ");
		System.out.println("Enter The First Name: ");
		parentPOJO.setParentFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		parentPOJO.setParentLast_name(inputScanner.nextLine());

		// System.out.println("Enter The Street Name: ");
		// contactPOJO.setStreet(inputScanner.nextLine());
		// System.out.println("Enter The City: ");
		// contactPOJO.setCity(inputScanner.nextLine());
		// System.out.println("Enter The Pincode: ");
		// contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
		// System.out.println("Enter The Email: ");
		// String emailString = inputScanner.nextLine();

		boolean nextContact = true;
		do {// get multiple contacts if any
			ContactPOJO contactPOJO = new ContactPOJO();
			System.out.println("Enter The Street Name: ");
			contactPOJO.setStreet(inputScanner.nextLine());
			System.out.println("Enter The City: ");
			contactPOJO.setCity(inputScanner.nextLine());
			System.out.println("Enter The Pincode: ");
			contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
			boolean emailflag = true;
			do {
				System.out.println("Enter The Email: ");
				String emailString = inputScanner.nextLine();
				EmailValidator email = new EmailValidator();
				boolean isEmail = (email.validate(emailString));
				if (isEmail == true) {
					contactPOJO.setEmail(emailString);
					emailflag = false;
				} else {
					System.out.println("Not Valid Email Id");
					emailflag = true;
				}
			} while (emailflag);
			System.out.println("Enter The Phone Number: ");
			contactPOJO.setPhoneNumber(inputScanner.nextLine());
			contact.add(contactPOJO);
			System.out.println("Want To Provide secondary Contact (Y/N)");
			String input = inputScanner.nextLine();
			if (input.equals("Y") || input.equals("y")) {
				nextContact = true;
			} else {
				nextContact = false;
			}
		} while (nextContact);
		// add list of contacts to parent object
		parentPOJO.setContact(contact);
		boolean isparentinserted = adminService.insertParentDetails(parentPOJO);
		if (isparentinserted) {
			System.out.println("Parent Info Inserted Successfuly....!!");
			this.AddChild(parentPOJO);
		} else {
			System.out.println("Parent Info NOT Inserted ");

		}
	}

	private void AddChild(ParentPOJO parentPOJO) throws SQLException, ParseException {
		//
		// ChildPOJO childPOJO = new ChildPOJO();
		// System.out.println("++++++++++ Please Enter Details Of Child
		// ++++++++++\n ");
		// System.out.println("Enter The First Name: ");
		// childPOJO.setFirst_name(inputScanner.nextLine());
		// System.out.println("Enter The Last Name: ");
		// childPOJO.setLast_name(inputScanner.nextLine());
		// System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd):
		// ");
		// childPOJO.setDob(inputScanner.nextLine());
		// adminService.insertChildDetails(childPOJO);
		List<ChildPOJO> children = new ArrayList<>();

		boolean nextchild = true;
		do {// get multiple children if any
			ChildPOJO child = new ChildPOJO();
			System.out.println("++++++++++ Please Enter Details Of Child ++++++++++\n ");
			System.out.println("Enter The First Name: ");
			child.setFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			child.setLast_name(inputScanner.nextLine());

			boolean dateflag = true;
			do {
				System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
				String date = inputScanner.nextLine();
				if (CdccmUtilities.isValidFormat(date)) {
					child.setDob(date);
					dateflag = false;
				}
			} while (dateflag);

			System.out.println("Do you have second child (Y/N)");
			String input = inputScanner.nextLine();

			children.add(child);// add child to list of children of this parent
			if (input.equals("Y") || input.equals("y")) {
				nextchild = true;
			} else {
				nextchild = false;
			}
		} while (nextchild);
		parentPOJO.setChild(children);// associate children to parent
		boolean isChildInserted = adminService.insertChildDetails(parentPOJO);
		if (isChildInserted) {
			System.out.println("Child-parent-contact  Inserted Successfully");
		} else {
			System.out.println("No Record Inserted");

		}

	}

	private void AddCareProvider() {

		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		System.out.println(
				"++++++++++ Welcome To Care Provider Registration Portal, Please Enter Details Of Care Provider ++++++++++\n");
		System.out.println("Enter The Complete Name: ");
		careProviderPOJO.setName(inputScanner.nextLine());
		System.out.println("Enter The Email: ");
		String emailString = inputScanner.nextLine();
		EmailValidator email = new EmailValidator();
		boolean isEmail = (email.validate(emailString));
		if (isEmail == true) {
			careProviderPOJO.setEmail(emailString);
		} else
			System.out.println("Not Valid Email Id");
		System.out.println("Enter The Phone Number: ");
		careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertCareProvider(careProviderPOJO);
	}

	private void ShowAllChildren() throws SQLException {
		try {
			System.out.println("Here Is List Of All Children");
			ResultSet childList = adminService.listAllChild();
			int numberOfChild = 1;

			while (childList.next()) {
				System.out.println("Details of Child: " + numberOfChild);
				System.out.println("First Name:" + childList.getString("name"));
				System.out.println("Last Name:" + childList.getString("surname"));
				System.out.println("Date Of Birth:" + childList.getString("dob"));
				System.out.println("Age:" + childList.getString("age"));
				numberOfChild++;
				System.out.println("---------");
			}
			System.out.printf("Above Is The List Of %d Children", numberOfChild);
			System.out.println("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void SendNewsEventsReports() {
		boolean choiceFlag = true;
		String messageHeadading = null;
		String messageBody = null;
		String date = null;
		int choice = 0;

		System.out.println("-------------------------Welcome To Mailing Service-------------------");
		System.out.println(
				"Now Select An Operation To Perform\n1. Send Performance Reports to Everyone\n2. Send Schedule Everyone\n3. Send Event/News Information\n4. Send Email to Specific Person ");
		choice = inputScanner.nextInt();
		inputScanner.nextLine();
		do {
			switch (choice) {
			case 1:
				System.out.println("Enter The Report date in(yyyy-mm-dd) format");
				date = inputScanner.nextLine();
				if (CdccmUtilities.isValidFormat(date)) {
					System.out.println("valid date");
					System.out.println("Enter The Message Heading");
					messageHeadading = inputScanner.nextLine();
					System.out.println("Enter The Message Body");
					messageBody = inputScanner.nextLine();
					// make call to emailservice with date,head,body
					EmailService emailService = new EmailService(date, messageHeadading, messageBody);
					// send the report
					emailService.send("performance".toString());
				}

				choiceFlag = false;
				break;
			case 2:
				System.out.println("Enter The Report date in(yyyy-mm-dd) format");
				date = inputScanner.nextLine();
				if (CdccmUtilities.isValidFormat(date)) {
					System.out.println("Enter The Message Heading");
					messageHeadading = inputScanner.nextLine();

					System.out.println("Enter The Message Body");
					messageBody = inputScanner.nextLine();
					System.out.println(date + " " + messageHeadading + " " + messageBody);
					EmailService emailService1 = new EmailService(date, messageHeadading, messageBody);

					emailService1.send("schedule".toString());
				}
				choiceFlag = false;
				break;
			case 3:
				System.out.println("-----------------Send Event/News Information-----------------");

				System.out.println("Enter The Message Heading");
				messageHeadading = inputScanner.nextLine();

				System.out.println("Enter The Message Body");
				messageBody = inputScanner.nextLine();

				EmailService emailService2 = new EmailService(null, messageHeadading, messageBody);
				emailService2.send("news".toString());
				choiceFlag = false;
				break;
			case 4:
				System.out.println("-----------------Send Mail to Specific person-----------------");
				System.out.println("Enter Email Id of the Person You want to send Email to");
				String emailid = inputScanner.nextLine();
				EmailValidator vatidator = new EmailValidator();
				if (vatidator.validate(emailid)) {
					System.out.println("Enter The Message Heading");
					messageHeadading = inputScanner.nextLine();

					System.out.println("Enter The Message Body");
					messageBody = inputScanner.nextLine();

					EmailService emailService3 = new EmailService();
					System.out.println("HEad " + messageHeadading + "Body " + messageBody);
					emailService3.sendSpecificMail(emailid, messageHeadading, messageBody);
				} else {
					System.out.println("**Invalid email Id**");
				}
				choiceFlag = false;
				break;

			default:
				System.out.println("Wrong Choice");
				choiceFlag = false;
			}
		} while (choiceFlag);
	}
 
	private void AddActivityToChild() throws SQLException {
		boolean moreEntry = true;
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		System.out.println("Enter Child ID To Assign Activity and Care Provider");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
		adminService.displayInfo(assignActivityPOJO.getChildID(), "CHILD_INFO");
		System.out.println("Enter Child's AgeGroup");
		assignActivityPOJO.setAgeGroup(inputScanner.nextInt());
		do {

			System.out.println("Enter The Session (For Morning 1, For Afternoon 2, For Evening 3)");
			assignActivityPOJO.setSession(inputScanner.nextInt());
			adminService.assignActivityToChild(assignActivityPOJO);

			System.out.println("Do You Want To Assign more Activity? Press Yes");
			String choice = inputScanner.nextLine().toUpperCase();
			if (choice.equals("YES")) {

			} else
				moreEntry = false;
		} while (moreEntry);
	}

	/* Takes ageGroup from user shows all available activities for that group */
	/* user has to select from the displyed values to update report table */
	private void AddActivitiesToAllChildren() throws NumberFormatException, SQLException  {

		System.out.println("Welcome To Assiging All Children Acivities Based On Their Age Group");
		adminService.loadChildren();
		System.out.println("Children Loaded Successfuly for Activity Assignment");
		System.out.println("Now Assign The Activity To Children");
		boolean flag = true;
		do {
			System.out.println("Select the age Group For Asiginig Activity, 1.Infant/ 2.Toddler / 3.Kindergarten");
			int ageGroup = inputScanner.nextInt();
			// inputScanner.nextLine();
			List<ActivityPOJO> listOfActivity = adminService.getAvailableActivitieForThisAgeGroup(ageGroup);
			Iterator it = listOfActivity.iterator();
			while (it.hasNext()) {
				ActivityPOJO activitypojo = new ActivityPOJO();
				activitypojo = (ActivityPOJO) it.next();
				System.out.println("ActivityId: " + activitypojo.getActivityId() + " ProviderId: "
						+ activitypojo.getProviderId() + " SessionId: " + activitypojo.getSession());
			}
			boolean nextSession = true;
			do {

				System.out.println("Choose AgeGroup,ActivityID,ProviderId,SessioID(Each Value Seperated By Comma(,)");
				String input = inputScanner.next();
				String inputArray[] = input.split(",");
				/* passing ActivityId,ProviderId,SessionID */
				int rowsupdated = adminService.assignActivitiesToChildren(Integer.parseInt(inputArray[0]),
						Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[2]),
						Integer.parseInt(inputArray[3]));
				System.out.println("Rows Updated " + rowsupdated);
				System.out.println("Want To Set Activity for another Session?, Y/N");
				String yesOrNo = inputScanner.next().toUpperCase();
				if (yesOrNo.equals("N")) {
					nextSession = false;
				}
			} while (nextSession);
			System.out.println("Want To Set Activity For Another AgeGroup?, Y/N");
			String yesOrNo = inputScanner.next().toUpperCase();
			if (yesOrNo.equals("N")) {
				flag = false;
			}
		} while (flag);
	}

	private void UpdateRegistrationInfo() throws SQLException {

		boolean choiceFlag = true;
		int choice = 0;
		System.out.println("Welcome To Update Registration Information");

		do {
			System.out.println(
					"\nNow Select An Operation To Perform\n1. Update Child Information \n2. Update Parent Information \n3. Update Care Provider Information \n4. Exit Update Process ");
			choice = Integer.parseInt(inputScanner.nextLine());
			switch (choice) {

			case 1:
				UpdateChildInfo();
				break;
			case 2:
				UpdateParentInfo();
				break;
			case 3:
				UpdateCareProviderInfo();
				break;
			case 4:
				choiceFlag = false;
				break;
			default:
				System.out.println("Wrong Choice");
			}
		} while (choiceFlag);
	}

	private void UpdateCareProviderInfo() throws SQLException {
		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		System.out.println("Enter Care Provider ID To Update Data");
		int careProviderID = (Integer.parseInt(inputScanner.nextLine()));
		boolean showProvider = adminService.displayCareProvider(careProviderID);
		if (showProvider) {
			System.out.println("Enter The Complete Name: ");
			careProviderPOJO.setName(inputScanner.nextLine());
			System.out.println("Enter The Email: ");
			String emailString = inputScanner.nextLine();
			EmailValidator email = new EmailValidator();
			boolean isEmail = (email.validate(emailString));
			if (isEmail == true) {
				careProviderPOJO.setEmail(emailString);
			} else
				System.out.println("Not Valid Email Id");
			System.out.println("Enter The Phone Number: ");
			careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
			adminService.updateCareProviderInfo(careProviderID, careProviderPOJO);
		} else {
			System.out.println("No Record Found !!");
		}

	}

	private void UpdateParentInfo() throws SQLException {
		ParentPOJO parentPOJO = new ParentPOJO();
		ContactPOJO contactPOJO = new ContactPOJO();
		System.out.println("Enter Parent ID To Update Data");
		int parentID = (Integer.parseInt(inputScanner.nextLine()));
		boolean showParent = adminService.displayParent(parentID);
		adminService.displayContact(parentID);
		System.out.println("");
		if (showParent) {
			System.out.println("Enter The First Name: ");
			parentPOJO.setParentFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			parentPOJO.setParentLast_name(inputScanner.nextLine());
			System.out.println("Enter The Street Name: ");
			contactPOJO.setStreet(inputScanner.nextLine());
			System.out.println("Enter The City: ");
			contactPOJO.setCity(inputScanner.nextLine());
			System.out.println("Enter The Pincode: ");
			contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
			System.out.println("Enter The Phone Number: ");
			contactPOJO.setPhoneNumber(inputScanner.nextLine());
			System.out.println("Enter The Email: ");
			String emailString = inputScanner.nextLine();
			EmailValidator email = new EmailValidator();
			boolean isEmail = (email.validate(emailString));
			if (isEmail == true) {
				contactPOJO.setEmail(emailString);
			} else
				System.out.println("Not Valid Email Id");
			adminService.updateParentInfo(parentID, parentPOJO);
			adminService.updateContactInfo(parentID, contactPOJO);
		} else {
			System.out.println("No Record Found !!");
		}
	}

	private void UpdateChildInfo() throws SQLException {
		System.out.println("Enter Child ID To Update Data");
		int id = (Integer.parseInt(inputScanner.nextLine()));
		ChildPOJO childPOJO = new ChildPOJO();
		boolean showChild = adminService.displayChild(id);
		if (showChild) {
			System.out.println("++++++++++ To Update the Data Please Enter Details Of Child ++++++++++\n ");
			System.out.println("Enter The First Name: ");
			childPOJO.setFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			childPOJO.setLast_name(inputScanner.nextLine());
			System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
			childPOJO.setDob(inputScanner.nextLine());
			adminService.updateChildInfo(id, childPOJO);
		} else
			System.out.println("No Record Found !!");
	}

	private void GenerateReport() throws SQLException {

		boolean choiceFlag = true;
		// String tableName="";
		int choice = 0;
		System.out.println("Welcome To Report Generation!!");
		System.out.println(
				"Now Select An Operation To Perform\n1. Generate performance Report For One Children\n2. Generate performance Report for all \n3. Generate Schedule Copies\n4. Dump Report to Archive");
		choice = inputScanner.nextInt();
		inputScanner.nextLine();
		if (choice == 4) {
			System.out.println("Are you sure you want to Clear REPORT Table and Put into Archieve ?; Y or N");
			String confirm = inputScanner.nextLine().toUpperCase();
			if (confirm.equals("N")) {
				choice = 0;
			}
		}
		do {
			switch (choice) {
			case 1:
				System.out.println("Enter Childid to create the report");
				int childid = this.inputScanner.nextInt();
				adminService.generateReport(childid);
				choiceFlag = false;
				break;
			case 2:
				adminService.generateBulckPerformanceReport();
				choiceFlag = false;
				break;
			case 3:
				adminService.GenerateScheduleReport();
				choiceFlag = false;
				break;
			case 4:
				adminService.dumpReportToArchive();
				choiceFlag = false;
				break;
			default:
				System.out.println("Wrong Choice");
				choiceFlag = false;
			}
		} while (choiceFlag);

	}

	private void ProvideFeedback() {

		ProviderFeedbackPOJO providerFeedbackPOJO = new ProviderFeedbackPOJO();
		System.out.println("Enter Parent ID");
		providerFeedbackPOJO.setParentId(Integer.parseInt(inputScanner.nextLine()));
		System.out.println("Enter Care Provider ID To Add Feedback/Suggestions");
		providerFeedbackPOJO.setCareProviderId(Integer.parseInt(inputScanner.nextLine()));
		boolean careProviderExists = adminService.displayCareProvider(providerFeedbackPOJO.getCareProviderId());
		if (careProviderExists) {
			System.out.println("Please Enter Feedback/Suggestions For Care Provider");
			providerFeedbackPOJO.setFeedback(inputScanner.nextLine());
			adminService.provideFeedback(providerFeedbackPOJO);
		} else {
			System.out.println("No Record Found !!");
		}

	}

	private void CreateFoodProgram() {
		FoodPOJO foodobject = null;
		FoodPOJO FoodPOJO = null;
		List<FoodPOJO> foodlist = new ArrayList<>();
		System.out.println("++++++++++ Welcome To Meal Program Registration Portal ++++++++++\n");
		System.out.println("++++++++++ Please Enter Details for the Meals for all the week  ++++++++++\n ");

		System.out.println("Please Select An Operation To Perform");
		System.out.println("1. Create Meal Program \n2. Update Meal Program \n3. Delete Meal Pogram for day ");
		int operation = 0;
		operation = Integer.parseInt(inputScanner.nextLine());
		int daycounter = 1;
		boolean moreEntry = true;
		switch (operation) {
		case 1:
			do {
				foodobject = new FoodPOJO();
				System.out.println(
						"Enter The day to complete the Meal program: Select Please   Mon / Tue / Wed / Thu / Fri ");
				foodobject.setDay(inputScanner.nextLine());
				System.out.println("Enter The Breakfast for " + foodobject.getDay());

				foodobject.setBreakfast(inputScanner.nextLine());
				System.out.println("Enter The Lunch for " + foodobject.getDay());
				foodobject.setLunch(inputScanner.nextLine());
				System.out.println("Enter The Snack for " + foodobject.getDay());
				foodobject.setSnack(inputScanner.nextLine());

				foodlist.add(foodobject);
				System.out.println("Do You Want To Create More Meals? Press Yes");
				String choice = inputScanner.nextLine().toUpperCase();
				daycounter++;

				if (!choice.equals("YES") || daycounter > 5) {
					moreEntry = false;
					adminService.insertMealDetails(foodlist);
				}
			} while (moreEntry);
			break;

		// update
		case 2:
			int option;
			moreEntry = true;
			do {

				System.out.println("\nEnter Day To Update Meal ");
				FoodPOJO.setDay(inputScanner.nextLine());
				System.out.println("Enter which Meal will be updated : 1.  Breakfast / 2. Lunch / 3. Snak");
				option = Integer.parseInt(inputScanner.nextLine());

				if (option == 1) {
					System.out.println("Enter the new BreakFast for " + FoodPOJO.getDay());
					FoodPOJO.setBreakfast(inputScanner.nextLine());
				} else if (option == 2) {
					System.out.println("Enter the new Lunch for " + FoodPOJO.getDay());
					FoodPOJO.setLunch(inputScanner.nextLine());
				} else {
					System.out.println("Enter the new Meal for " + FoodPOJO.getDay());
					FoodPOJO.setSnack(inputScanner.nextLine());
				}

				adminService.updateFood(FoodPOJO);

				System.out.println("Do You Want To Update More Meals? Press Yes");
				String choice1 = inputScanner.nextLine().toUpperCase();
				if (choice1.equalsIgnoreCase("YES")) {

				} else
					moreEntry = false;
			} while (moreEntry);

			break;

		case 3:
			moreEntry = true;
			do {
				System.out.println(
						"Enter The day to delete from the Meal program: Select Please   Mon / Tue / Wed / Thu / Fri ");
				FoodPOJO.setDay(inputScanner.nextLine());
				adminService.deleteMealDay(FoodPOJO);

				System.out.println("Do You Want To Delete More Meals? Press Yes");
				String choice1 = inputScanner.nextLine().toUpperCase();
				if (choice1.equals("YES")) {

				} else
					moreEntry = false;
			} while (moreEntry);

		default:
			System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
			break;
		}

	}

}
