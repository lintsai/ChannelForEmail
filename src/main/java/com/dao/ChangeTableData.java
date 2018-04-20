package com.dao;

import java.sql.DriverManager;
import java.util.List;

import javax.naming.InitialContext;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import com.google.gson.JsonObject;
import com.util.Util;

public class ChangeTableData {
	/**
	 * changeTableDataFromSql2O
	 *
	 * @return
	 * @throws Exception
	 */
	public static JsonObject updateEmailOut(String aId, String aIxnId, String aSentDate) throws Exception {

		JsonObject jsonObject = new JsonObject();

		try {
			Class.forName(Util.getSystemParam().get("email_dbDriver"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sql2o sql2o = null;
		try{
			sql2o = new Sql2o(Util.getSystemParam().get("email_dbJndi"));
		} catch (Exception e){
			sql2o = new Sql2o(Util.getSystemParam().get("email_dbUrl"), Util.getSystemParam().get("email_dbUser"), Util.getSystemParam().get("email_dbPassword"));
		}

		String emailOutSqlCommands = "UPDATE [dbo].[tblEmailOut] " +
				"SET SentDate = :sentDate " +
				"WHERE id = :id";

		String ixnSqlCommands = "UPDATE [dbo].[tblInteraction] SET EndDate = :endDate WHERE IxnID = :ixnID";

		int emailOutSqlCommandsResult = 0;
		int ixnSqlCommandsResult = 0;
		try (Connection con = sql2o.open()){
			emailOutSqlCommandsResult = con.createQuery(emailOutSqlCommands).addParameter("id",aId).addParameter("sentDate",aSentDate)
					.executeUpdate().getResult();

			ixnSqlCommandsResult = con.createQuery(ixnSqlCommands).addParameter("ixnID",aIxnId).addParameter("endDate",aSentDate)
					.executeUpdate().getResult();
		}
		jsonObject.addProperty("emailOutCount",emailOutSqlCommandsResult);
		jsonObject.addProperty("ixnCount",ixnSqlCommandsResult);
		
		return jsonObject;
	}
	
	/**
	 * changeTableDataFromSql
	 * 
	 * @param aDBDriver
	 * @param aSqlCommands
	 * @param aDBUrl
	 * @param aDBUser
	 * @param aPassword
	 * @return
	 * @throws Exception
	 */
	public static JsonObject changeTableDataFromSql(String aDBDriver, String aSqlCommands, String aDBJndi, String aDBUrl, String aDBUser, String aPassword) throws Exception {
		
		Util.getFileLogger().info(
				"[InfoAPI]ChangeTableData(changeTableDataFromSql) start ###");
		
		try {
			Class.forName(aDBDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Util.getFileLogger().error(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - ClassNotFoundException: "+ e.getMessage());
		}
		
		int result = 0;
		
		java.sql.Connection connectionDb = null;
		try{
			InitialContext ctx = new InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(aDBJndi);
			connectionDb = ds.getConnection();
			Util.getFileLogger().info(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - DataSource call jndi");
		} catch (Exception e){
			Util.getFileLogger().error(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - Exception: "+e.getMessage());
			Util.getFileLogger().info(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - jndi fail!!!");
			connectionDb = DriverManager.getConnection(aDBUrl,
					aDBUser, aPassword);
			Util.getFileLogger().info(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - DataSource call jdbc");
		}
		
		connectionDb.setAutoCommit(false);
		java.sql.Statement statement = connectionDb.createStatement();
		try {
			statement.addBatch(aSqlCommands);
			statement.executeBatch();
			connectionDb.commit();
			result = 1;
		} catch (Exception e) {
			statement.cancel();
			try {
				connectionDb.rollback();
			} catch (Exception ex){
				ex.printStackTrace();
				Util.getFileLogger().error(
						"[InfoAPI]ChangeTableData(changeTableDataFromSql) - Exception: "+ex.getMessage());
			}
			e.printStackTrace();
			Util.getFileLogger().error(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - Exception: "+e.getMessage());
		} finally {
			statement.close();
			connectionDb.close();
		}
		
		JsonObject jsonobject = new JsonObject();
		
		Util.getFileLogger().info(
					"[InfoAPI]ChangeTableData(changeTableDataFromSql) - result: "+result);
		jsonobject.addProperty("result", result);
		
		Util.getFileLogger().info(
				"[InfoAPI]ChangeTableData(changeTableDataFromSql) End ###");
		
		return jsonobject;
	}
}
