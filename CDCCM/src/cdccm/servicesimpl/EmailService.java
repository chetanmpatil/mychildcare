package cdccm.servicesimpl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.ParentNamePlate;

public class EmailService {
	private MySQLDBConnector dbConnector = null;
    private String date;
    private String messageHeadading;
    private String messageBody;
    private  String directorylocation;
  
	public EmailService(String date,String messageHeadading,String messageBody) {
		dbConnector = MySQLDBConnector.getInstance();
		this.date=date;
		this.messageHeadading=messageHeadading;
		this.messageBody=messageBody;
	}
	public void send(String whichmail)
	{
		if(whichmail.equals("performance"))
		{
			this.directorylocation = "C:/mypdf/performancedocs/";
		}else if(whichmail.equals("schedule"))
		{
			this.directorylocation = "C:/mypdf/scheduledocs/";
		}
		else if(whichmail.equals("news"))
		{
			this.directorylocation = null;
		}
		SendPerformanceReport();
	}

	private void SendPerformanceReport() {
		ParentNamePlate parentnameplate = null;
		ArrayList<ParentNamePlate> listofparentnameplate = new ArrayList<ParentNamePlate>();
		
		String fileNames[] = null;
		String emailids[] = null;
		File mainFolder = new File(this.directorylocation);
		// get all files in direcory
		fileNames = getFiles(mainFolder);
		// get all parent nameplates
		listofparentnameplate = getAllParentsNameplate();

		matchFileAndSend(listofparentnameplate, fileNames);

	}

	public void SendEventInfo() {

	}

	public void SendSchedule() {

	}

	/*
	 * this method iteratos over the list of parents and available array of
	 * files to search the appropriate file for that parent
	 */
	private void matchFileAndSend(ArrayList<ParentNamePlate> listOfParentNamePlates, String[] fileNames) {
		Iterator<ParentNamePlate> it = listOfParentNamePlates.iterator();

		while (it.hasNext()) {
			ParentNamePlate parentnameplate = it.next();
			int childid = parentnameplate.getChildid();
			for (int i = 0; i < fileNames.length; i++) {
				String[] dateAndId = new String[3];
				/* split the childid(0),date(1),filetype(2) */
				dateAndId = fileNames[i].split("_");
				// check if childid(from filename)==childid(from parent list)
				// and date(entered)==date(report generated)
				if ((Integer.parseInt(dateAndId[0]) == childid) && dateAndId[1].equals(this.date)) {
					// file to attach with mail
					String attachment=this.directorylocation+fileNames[i];
					String email=parentnameplate.getEmailid();
					String messagebody="Dear "+parentnameplate.getName()+" "+this.messageBody;
					MailSender mailsender=new MailSender(this.messageHeadading,messagebody,email,attachment);
					mailsender.sendMail();
					
					break;
				} else {// when entered date is wrong
					if (i == (fileNames.length - 1)) {
						System.out.println(
								"Either date format is Wrong or Report is not available for: " + childid + " child");
						break;
					}
                 }
			}
		}

	}

	private ArrayList<ParentNamePlate> getAllParentsNameplate() {
		ArrayList<ParentNamePlate> listofparentnameplate = new ArrayList<ParentNamePlate>();
		String sql = "Select distinct con.emailid,p.name,c.idchild from contact con join parent p join child_info c on(con.fk_idparent=p.idparent and c.fk_idparent=p.idparent);";
		try {
			listofparentnameplate.addAll(dbConnector.getParentNameplate(sql));
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listofparentnameplate;

	}

	@SuppressWarnings("null")
	// this method returns all files in the directory in string array
	private String[] getFiles(File f) {
		File[] files;
		files = f.listFiles();// extract all files in array
		String[] fileNames = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				fileNames[i] = files[i].getAbsoluteFile().getName();

		}
		return fileNames;
	}

}
