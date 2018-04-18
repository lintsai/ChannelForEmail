package com.bean;

import java.util.HashMap;
import java.util.Map;

public class EmailBean {

	HashMap<String, Object> emailObj = new HashMap<String, Object>();
	
	private final static String CC = "CC";
	private final static String TO = "TO";
	private final static String BCC = "BCC";
	private final static String SUBJECT = "Subject";
	private final static String BODY = "Body";
	private final static String ATTACH = "attach";
	
	
	public EmailBean() {
		
	}
	public EmailBean(String toAddr, String toName, String subject, String body) {
		addTo(toAddr,toName);
		addSubject(subject);
		addBody(body);
	}
	
	/**
	 * @param toAddr
	 * @param toName
	 */
	@SuppressWarnings("unchecked")
	public void addTo(String toAddr ,String toName) {
		if(toAddr != null && !"".equals(toAddr)) {	
			HashMap<String, String> toJson = null;
			if(emailObj.containsKey(EmailBean.TO)) {
				toJson = (HashMap<String, String>) emailObj.get(EmailBean.TO);
			}
			else {
				toJson = new HashMap<String, String>();
			}			
			toJson.put(toAddr, toName==null ? toAddr : toName);
			emailObj.put(EmailBean.TO, toJson);
		}
	}
	
	
	/**
	 * @param ccAddr
	 * @param ccName
	 */
	@SuppressWarnings("unchecked")
	public void addCc(String ccAddr, String ccName) {
		if(ccAddr != null && !"".equals(ccAddr)) {
			HashMap<String, String> ccJson = null;
			if(emailObj.containsKey(EmailBean.CC)) {
				ccJson = (HashMap<String, String>) emailObj.get(EmailBean.CC);
			}
			else {
				ccJson = new HashMap<String, String>();
			}			
			ccJson.put(ccAddr, ccName==null ? ccAddr : ccName);
			emailObj.put(EmailBean.CC, ccJson);
		}
	}
	
	
	/**
	 * @param bccAddr
	 * @param bccName
	 */
	@SuppressWarnings("unchecked")
	public void addBcc(String bccAddr, String bccName) {
		if(bccAddr != null && !"".equals(bccAddr)) {
			HashMap<String, String> bccJson = null;
			if(emailObj.containsKey(EmailBean.BCC)) {
				bccJson = (HashMap<String, String>) emailObj.get(EmailBean.BCC);
			}
			else {
				bccJson = new HashMap<String, String>();
			}			
			bccJson.put(bccAddr, bccName==null ? bccAddr : bccName);
			emailObj.put(EmailBean.BCC, bccJson);
		}
	}
	
	
	/**
	 * @param subject
	 */
	public void addSubject(String subject) {
		String text = subject==null?"":subject;
		emailObj.put(EmailBean.SUBJECT, text);
	}
	
	
	/**
	 * @param body
	 */
	public void addBody(String body) {
		String text = body==null?"":body;
		emailObj.put(EmailBean.BODY, text);
	}
	
		
	/**
	 * @param fileName 
	 * @param file
	 */
	@SuppressWarnings("unchecked")
	public boolean addAttachment(String fileName, byte[] file) {
		if(fileName!=null && !"".equals(fileName) && file != null && file.length > 0) {			
			HashMap<String, byte[]> attachJson = null;
			if(emailObj.containsKey(EmailBean.ATTACH)) {
				attachJson = (HashMap<String,byte[]>) emailObj.get(EmailBean.ATTACH);
			}
			else {
				attachJson = new HashMap<String,byte[]>();
			}			
			attachJson.put(fileName, file);
			emailObj.put(EmailBean.ATTACH, attachJson);
		}
		return false;
	}
	
	
	public Map<String,String> getTo() {
		return (HashMap<String,String>) emailObj.get(EmailBean.TO);
	}
	
	public Map<String,String> getCc() {
		return (HashMap<String,String>) emailObj.get(EmailBean.CC);
	}
	
	public Map<String,String> getBcc() {
		return (HashMap<String,String>) emailObj.get(EmailBean.BCC);
	}
	
	public String getSubject() {
		return (String)emailObj.get(EmailBean.SUBJECT);
	}
	
	public String getBody() {
		return (String)emailObj.get(EmailBean.BODY);
	}
	
	public Map<String,byte[]> getAttachment() {
		return (HashMap<String,byte[]>) emailObj.get(EmailBean.ATTACH);
	}
	
//	public String getEmailObj(String systemCode) {
//		this.emailObj.put("systemCode", systemCode);
//		return JSONObject.fromObject(this.emailObj).toString();
//	}

}
