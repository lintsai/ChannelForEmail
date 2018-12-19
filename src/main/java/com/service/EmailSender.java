package com.service;

import java.net.URL;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

import com.util.Util;


public class EmailSender {

    private Map<String, String> emailToNameMap;
    private Map<String, String> emailToNameMapForCc;
    private Map<String, String> emailToNameMapForBcc;
    private Map<String, String> emailAttachMap;
    private String senderName;
    private String senderAddress;
    private String subject;
    private String content;
    private String mailServer;
    private String serverPort;
    private int serverPortInt;
    private String userId;
    private String password;
    private String charset;
    private Boolean isSSL;
    private Boolean isTLS;
    private Boolean isAuth;
    private String msgType;
    private String contentType;

    public EmailSender() {}

    /**
     * @param emailToNameMap
     * @param emailToNameMapForCc
     * @param emailToNameMapForBcc
     * @param subject
     * @param content
     */
    public EmailSender(Map<String, String> emailToNameMap, Map<String, String> emailToNameMapForCc,
            Map<String, String> emailToNameMapForBcc, Map<String, String> emailAttachMap, String subject, String content) {
        this.emailToNameMap = emailToNameMap;
        this.emailToNameMapForCc = emailToNameMapForCc;
        this.emailToNameMapForBcc = emailToNameMapForBcc;
        this.emailAttachMap = emailAttachMap;
        this.subject = subject;
        this.content = content;
    }

    public String sendEmail() throws Exception {
    	
    	Util.getFileLogger()
		.info("[InfoAPI]EmailSender(sendEmail) start ###");

        String msg = null;

        if (emailToNameMap != null) {
            HtmlEmail htmlEmail = null;
            htmlEmail = new HtmlEmail();
            htmlEmail.setHostName(mailServer);
            htmlEmail.setCharset(charset);
            if(isAuth){
                htmlEmail.setAuthentication(userId, password);
            }
            htmlEmail.setSmtpPort(serverPortInt);
            if (isTLS) {
                htmlEmail.setStartTLSEnabled(true);
            }
            if (isSSL) {
                htmlEmail.setSSLOnConnect(true);
                htmlEmail.setSslSmtpPort(serverPort);
            } else {}
            Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  read emailToNameMap");
            for (String email : emailToNameMap.keySet()) {
                htmlEmail.addTo(email, emailToNameMap.get(email));
            }

            if (emailToNameMapForCc != null) {
            	Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  read emailToNameMapForCc");
                for (String email : emailToNameMapForCc.keySet()) {
                    htmlEmail.addCc(email, emailToNameMapForCc.get(email));
                }
            }
            if (emailToNameMapForBcc != null) {
            	Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  read emailToNameMapForBcc");
                for (String email : emailToNameMapForBcc.keySet()) {
                    htmlEmail.addBcc(email, emailToNameMapForBcc.get(email));
                }
            }
            
            Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  read emailToNameMap finish");
            
            if(emailAttachMap != null && emailAttachMap.size() > 0) {
            	Util.getFileLogger().info("sendEmail -  read emailAttachMap");
            	for(String fileName : emailAttachMap.keySet()) {
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setURL(new URL(emailAttachMap.get(fileName)));
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setName(fileName);
            		htmlEmail.attach(attachment);
            	}
            }
            
            Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  read emailAttachMap finish");

            htmlEmail.setFrom(senderAddress, senderName);
            htmlEmail.setSubject(subject);
            if(msgType.equals("rtf")){
                htmlEmail.setMsg(content);
            }else if(msgType.equals("text")){
                htmlEmail.setTextMsg(content);
            }else{
                htmlEmail.setHtmlMsg(content);
            }
            if(contentType!=null){
                htmlEmail.updateContentType(contentType);
            }

            Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  htmlEmail.send() GO");
            msg = htmlEmail.send();
            Util.getFileLogger().info("[InfoAPI]EmailSender(sendEmail) -  htmlEmail.send() END");
        }
        
        Util.getFileLogger()
		.info("[InfoAPI]EmailSender(sendEmail) End ###");

        return msg;
    }

    /**
     * @return the emailToNameMap
     */
    public Map<String, String> getEmailToNameMap() {
        return emailToNameMap;
    }

    /**
     * @param emailToNameMap
     *            the emailToNameMap to set
     */
    public void setEmailToNameMap(Map<String, String> emailToNameMap) {
        this.emailToNameMap = emailToNameMap;
    }

    /**
     * @return the senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @param senderName
     *            the senderName to set
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * @return the senderAddress
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * @param senderAddress
     *            the senderAddress to set
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Map<String, String> getEmailToNameMapForCc() {
        return emailToNameMapForCc;
    }

    public void setEmailToNameMapForCc(Map<String, String> emailToNameMapForCc) {
        this.emailToNameMapForCc = emailToNameMapForCc;
    }

    public Map<String, String> getEmailToNameMapForBcc() {
        return emailToNameMapForBcc;
    }

    public void setEmailToNameMapForBcc(Map<String, String> emailToNameMapForBcc) {
        this.emailToNameMapForBcc = emailToNameMapForBcc;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the mailServer
     */
    public String getMailServer() {
        return mailServer;
    }

    /**
     * @param mailServer
     *            the mailServer to set
     */
    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    /**
     * @return the serverPort
     */
    public String getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort
     *            the serverPort to set
     */
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the serverPortInt
     */
    public int getServerPortInt() {
        return serverPortInt;
    }

    /**
     * @param serverPortInt
     *            the serverPortInt to set
     */
    public void setServerPortInt(int serverPortInt) {
        this.serverPortInt = serverPortInt;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset
     *            the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the isSSL
     */
    public Boolean getIsSSL() {
        return isSSL;
    }

    /**
     * @param isSSL
     *            the isSSL to set
     */
    public void setIsSSL(Boolean isSSL) {
        this.isSSL = isSSL;
    }

    /**
     * @return the isTLS
     */
    public Boolean getIsTLS() {
        return isTLS;
    }

    /**
     * @param isTLS
     *            the isTLS to set
     */
    public void setIsTLS(Boolean isTLS) {
        this.isTLS = isTLS;
    }

    /**
     * @return the isAuth
     */
    public Boolean getIsAuth() {
        return isAuth;
    }

    /**
     * @param isAuth
     *            the isTLS to set
     */
    public void setIsAuth(Boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     *
     * @return
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     *
     * @param msgType
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}