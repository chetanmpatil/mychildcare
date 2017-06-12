package cdccm.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    protected Properties prop=null;
    Properties properties = new Properties();
    Thread currentThread = Thread.currentThread();
    ClassLoader contextClassLoader = currentThread.getContextClassLoader();
    //protected InputStream input=PropertyReader.class.getClassLoader().getResourceAsStream("/CDCCM/src/data/config.properties");
    protected InputStream input=contextClassLoader.getResourceAsStream("../data/config.properties");

    public PropertyReader() {
    	prop=new Properties();
    	try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public String getHost()
    {   
    	System.out.println(prop.getProperty("host"));
    	return prop.getProperty("host");
    }
    public String getFrom()
    {   System.out.println(prop.getProperty("from"));
    	return prop.getProperty("from");
    }
    public String getUser()
    {
    	return prop.getProperty("user");
    }
    public String getPassword()
    {
    	return prop.getProperty("pass");
    }
    public String getPort()
    {
    	return prop.getProperty("port");
    }
    public String getPerformanceDirectory()
   {
   	return prop.getProperty("peformancedirectory");
    }
   public String getScheduleDirectory()
   {
  	return prop.getProperty("scheduledirectory");
    }
}