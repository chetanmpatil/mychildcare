package cdccm.dbServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cdccm.helper.PropertyReader;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.ParentNamePlate;

public class MySQLDBConnector {
	public Connection conn = null;
	private Statement resultStatement = null;
	private static MySQLDBConnector dbConnectorObj = null;
	private PreparedStatement preparedstatement = null;
	PropertyReader dbProperties = null;

	private MySQLDBConnector() {
		this.dbProperties = new PropertyReader();

		String url = this.dbProperties.getUrl();
		String dbName = this.dbProperties.getDbName();
		String driver = this.dbProperties.getDriver();
		String userName = this.dbProperties.getUserName();
		String password = this.dbProperties.getDbPassword();

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

	public ArrayList<ParentNamePlate> getParentNameplate(String query) throws SQLException {
		ResultSet resultset = null;
		ArrayList<ParentNamePlate> parentnameplate = new ArrayList<ParentNamePlate>();
		try {
			resultStatement = conn.createStatement();
			resultset = resultStatement.executeQuery(query);
			while (resultset.next()) {
				parentnameplate
						.add(new ParentNamePlate(resultset.getString(1), resultset.getString(2), resultset.getInt(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return parentnameplate;
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

	public ResultSet getReport(String sql, int id) throws SQLException {
		preparedstatement = conn.prepareStatement(sql);
		preparedstatement.setInt(1, id);

		return preparedstatement.executeQuery();
	}

	public ResultSet callProcedure(String sql, int childid, int groupid) {
		ResultSet resultset = null;
		int flag = 0;

		CallableStatement callablestatement;
		try {
			callablestatement = conn.prepareCall(sql);
			callablestatement.setInt(1, childid);
			callablestatement.setInt(2, groupid);

			flag = callablestatement.executeUpdate();
			callablestatement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		if (flag > 0) {
			System.out.println("Procedure Executed Successfully");
			try {
				preparedstatement = conn.prepareStatement("select * from timetable");
				resultset = preparedstatement.executeQuery();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		} else {
			System.out.println("Procedure NOT Executed Successfully");
		}

		return resultset;
	}

	public void callLoadChildToReportTabProce(String sql) {

		CallableStatement callablestatement;
		try {
			delete("Delete From report");
			callablestatement = conn.prepareCall(sql);
			callablestatement.executeUpdate();
			callablestatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int updateAllChildren(String sqlAssignactivity, int activityId, int providerId, int ageGroupId,
			int sessionId) throws SQLException {
		preparedstatement = conn.prepareStatement(sqlAssignactivity);

		preparedstatement.setInt(1, activityId);
		preparedstatement.setInt(2, providerId);
		preparedstatement.setInt(3, ageGroupId);
		preparedstatement.setInt(4, sessionId);

		int rowsupdated = preparedstatement.executeUpdate();
		return rowsupdated;
		// TODO Auto-generated method stub

	}

	public ResultSet displayInfo(String query, int id) {
		ResultSet resultSetActivity = null;
		try {
			preparedstatement = conn.prepareStatement(query);
			preparedstatement.setInt(1, id);
			resultSetActivity = preparedstatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSetActivity;
	}

	public int giveFeedback(String query, AssignActivityPOJO activityObj) {
		int resultUpdate = 0;
		try {
			preparedstatement = conn.prepareStatement(query);
			preparedstatement.setString(1, activityObj.getFeedback());
			preparedstatement.setInt(2, activityObj.getChildID());
			preparedstatement.setInt(3, activityObj.getSession());

			resultUpdate = preparedstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultUpdate;
	}
}
