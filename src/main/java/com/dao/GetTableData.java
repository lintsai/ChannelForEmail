package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.StringConverter;
import org.sql2o.data.Column;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.util.Util;

public class GetTableData {

	/**
	 * getEmailOut
	 *
	 * @return
	 * @throws Exception
	 */
	public static JsonArray getEmailOut() throws Exception {


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

		JsonArray jsonarray = new JsonArray();

		String sqlCommands = "SELECT [FromAddress],[FromPersonal],[ToAddresses],[CcAddresses],[BccAddresses],[SentDate],[Subject],[Content],[Attach],[AttachName],[MsgType],[ContentType],[ExpectTime] FROM [dbo].[tblEmailOut] WHERE SentDate is NULL;";

		List<Map<String, Object>> list = new ArrayList<>();
		try (Connection con = sql2o.open()){
			list = con.createQuery(sqlCommands)
					.executeAndFetchTable().asList();

			for(Map<String, Object> map : list){
				JsonObject jsonobject = new JsonObject();
				String k = null;
				for(String key : map.keySet()){
					if(map.get(key)!=null){
						jsonobject.addProperty(key, map.get(key).toString());
					}
				}
				jsonarray.add(jsonobject);
			}
		}
		return jsonarray;
	}

	/**
	 * getFromEmailInfo
	 *
	 * @return
	 * @throws Exception
	 */
	public static JsonArray getFromEmailInfo(String aFromEmail) throws Exception {


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

		JsonArray jsonarray = new JsonArray();

		String sqlCommands = "SELECT [MailServer],[MailServerPort],[UserId],[Password],[Charset],[IsSSL] ,[IsTLS] FROM [dbo].[tblCfg_FromEmail] WHERE FromEmail=:fromEmail";

		List<Map<String, Object>> list = new ArrayList<>();
		try (Connection con = sql2o.open()){
			list = con.createQuery(sqlCommands).addParameter("fromEmail",aFromEmail)
					.executeAndFetchTable().asList();
			
			for(Map<String, Object> map : list){
		    	JsonObject jsonobject = new JsonObject();
		    	String k = null;
			    for(String key : map.keySet()){
			    	if(map.get(key)!=null){
			    		jsonobject.addProperty(key, map.get(key).toString());
			    	}
			    }
			    jsonarray.add(jsonobject);
		    }
		}
		return jsonarray;
	}

	/**
	 * getTableDataFromCLOBSql
	 * 
	 * @param aDBDriver
	 * @param aSqlCommands
	 * @param aDBUrl
	 * @param aDBUser
	 * @param aPassword
	 * @return
	 * @throws Exception
	 */
	public static String getTableDataFromCLOBSql(String aDBDriver, String aSqlCommands, String aDBJndi, String aDBUrl, String aDBUser, String aPassword) throws Exception {
		
		Util.getFileLogger().info(
				"[InfoAPI]GetTableData(getTableDataFromCLOBSql) start ###");
		
		try {
			Class.forName(aDBDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Util.getFileLogger().error(
					"[InfoAPI]ChangeTableData(getTableDataFromCLOBSql) - ClassNotFoundException: "+ e.getMessage());
		}
		
		Sql2o sql2o = null;
		try{
			sql2o = new Sql2o(aDBJndi);
			Util.getFileLogger().info(
					"[InfoAPI]GetTableData(getTableDataFromCLOBSql) - sql2o call jndi");
		} catch (Exception e){
			Util.getFileLogger().error(
					"[InfoAPI]GetTableData(getTableDataFromCLOBSql) - Exception: "+e.getMessage());
			Util.getFileLogger().info(
					"[InfoAPI]GetTableData(getTableDataFromCLOBSql) - jndi fail!!!");
			sql2o = new Sql2o(aDBUrl, aDBUser, aPassword);
			Util.getFileLogger().info(
					"[InfoAPI]GetTableData(getTableDataFromCLOBSql) - sql2o call jdbc");
		}

		String val = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try (Connection con = sql2o.open()){
			val = con.createQuery(aSqlCommands).executeScalar(String.class);
			Util.getFileLogger().info(
					"[InfoAPI]GetTableData(getTableDataFromCLOBSql) - val: "+val);
		}
		
		Util.getFileLogger().info(
				"[InfoAPI]GetTableData(getTableDataFromCLOBSql) End ###");
		
		return val;
	}
}
