package cdccm.dbServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDBConnector {
	public Connection conn;
	private Statement resultStatement;
	private static MySQLDBConnector dbConnectorObj;
	private PreparedStatement preparedstatement;
	private MySQLDBConnector() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "child_care";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";//default user name
		String password = "atcs";//default password
		
		try {
			Class.forName(driver).newInstance();
			this.conn = (Connection) DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}
	}
	
	public static MySQLDBConnector getInstance() {
		if (dbConnectorObj == null)
			dbConnectorObj = new MySQLDBConnector();
		return dbConnectorObj;
	}
	
	public ResultSet query(String query) throws SQLException {
		resultStatement = conn.createStatement();
		ResultSet res = resultStatement.executeQuery(query);
		return res;
	}

	public int insert(String insertQuery) throws SQLException {
		resultStatement = conn.createStatement();
		int resultCount = resultStatement.executeUpdate(insertQuery);
		return resultCount;
	}
	
	public int delete(String deleteQuery) throws SQLException {
		resultStatement = conn.createStatement();
		int resultCount = resultStatement.executeUpdate(deleteQuery);
		return resultCount;
	}
	public ResultSet getReport(String sql, int childid) throws SQLException {
		ResultSet resultset=null;
	
		preparedstatement=conn.prepareStatement(sql);
		preparedstatement.setInt(1,childid);
	
	
		return preparedstatement.executeQuery();
	}
//	public ResultSet getArecord(String sql, int childid) throws SQLException {
//		ResultSet resultset=null;
//		System.out.println(sql+ " , "+ childid);
//	    preparedstatement=conn.prepareStatement(sql);
//		preparedstatement.setInt(1,childid);
//		System.out.println("**********"+preparedstatement.getWarnings());
//		resultset = preparedstatement.executeQuery();
//	
//		return resultset;
//	}
	public ResultSet callProcedure(String sql,int childid,int groupid) 
	{   ResultSet resultset = null;
	   int flag = 0;
	  
		CallableStatement callablestatement;
		try {
			callablestatement = conn.prepareCall(sql);
			callablestatement.setInt(1,childid);
			callablestatement.setInt(2, groupid);
			
			flag=callablestatement.executeUpdate();
			callablestatement.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
		if(flag>0)
		{
			System.out.println("Procedure Executed Successfully");
			try {
				preparedstatement=conn.prepareStatement("select * from timetable");
				resultset=preparedstatement.executeQuery();
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
 	 	   
		}else{
			System.out.println("Procedure NOT Executed Successfully");
		}
	
		return resultset;
	}
}
