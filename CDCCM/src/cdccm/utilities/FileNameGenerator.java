package cdccm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cdccm.pojo.ChildReportPOJO;

public class FileNameGenerator {
private Collection<ChildReportPOJO> listofscoreobject = new ArrayList<>();

public FileNameGenerator(Collection<ChildReportPOJO> listofscoreobject) {
	this.listofscoreobject = listofscoreobject;
}
public File generateUniqueFileName()
{   int rollno=0;
    rollno=listofscoreobject.iterator().next().getChildid();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    File f = new File("C:/mypdf/" +rollno+"_"+dateFormat.format(new Date())+ "_mypdf.pdf");
    try {
		f.getParentFile().mkdirs();
		f.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return f;
	
}
}
