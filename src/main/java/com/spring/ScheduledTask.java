package com.spring;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dao.GetTableData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.service.EmailSender;
import org.apache.commons.mail.EmailException;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.util.Util;


@Configuration
@Component
@ConfigurationProperties(value="application")
public class ScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private Integer count0 = 1;
//    private Integer count1 = 1;
    private Integer count2 = 1;
    
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void reportCurrentTimeAfterSleep() throws InterruptedException {
//        System.out.println(String.format("===第%s次执行，当前时间为：%s", count1++, dateFormat.format(new Date())));
//    }

    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "${RepeatDataCron}")
    public void reportCurrentTimeCron() throws InterruptedException {

        JsonArray emailOutArray = new JsonArray();
        try {
            emailOutArray = GetTableData.getEmailOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("emailOutArray: "+emailOutArray);

        for(JsonElement emailOut:emailOutArray){
            JsonObject emailOutJsonObject= emailOut.getAsJsonObject();
            String subject = emailOutJsonObject.get("subject").getAsString();
            String email_SenderName = emailOutJsonObject.get("frompersonal").getAsString();
            String content = emailOutJsonObject.get("content").getAsString();
            String contentType = emailOutJsonObject.get("contenttype").getAsString();
            String email_SenderAddress = emailOutJsonObject.get("fromaddress").getAsString();
            String msgtype = emailOutJsonObject.get("msgtype").getAsString();
            String emailToName = emailOutJsonObject.get("toaddresses").getAsString();

            String emailToNameForCc = "none";
            if(emailOutJsonObject.has("ccaddresses")){
                emailToNameForCc = emailOutJsonObject.get("ccaddresses").getAsString();
            }

            String emailToNameForBcc = "none";
            if(emailOutJsonObject.has("bccaddresses")){
                emailToNameForBcc = emailOutJsonObject.get("bccaddresses").getAsString();
            }

            String emailAttachName = "";
            if(emailOutJsonObject.has("attachname")){
                emailAttachName = emailOutJsonObject.get("attachname").getAsString();
            }

            String emailAttach = "";
            if(emailOutJsonObject.has("attach")){
                emailAttach = emailOutJsonObject.get("attach").getAsString();
            }


//        String subject = "測試抬頭"+count2;
//        String content = String.format("+++Count: %s，DateTime：%s", count2, dateFormat.format(new Date()));
//        String emailToName = "holylin@crm.com.tw";
//        String emailToNameForCc = "none";
//        String emailToNameForBcc = "none";
//        String aEmailAttachName = "";
//        String aEmailAttach = "";
//        String msgtype = "rtf"; //text, html, rtf
//        String contentType = "Message/Partial";

//        String email_SenderName = "Lin";
//        String email_SenderAddress = "lintsai@crm.com.tw";

            JsonArray fromEmailInfoArray = new JsonArray();
            try {
                fromEmailInfoArray = GetTableData.getFromEmailInfo(email_SenderAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("fromEmailInfoArray: "+fromEmailInfoArray);

            String email_Charset = fromEmailInfoArray.get(0).getAsJsonObject().get("charset").getAsString();
            String email_Password = fromEmailInfoArray.get(0).getAsJsonObject().get("password").getAsString();
            String email_MailServer_port = fromEmailInfoArray.get(0).getAsJsonObject().get("mailserverport").getAsString();
            String email_MailServer = fromEmailInfoArray.get(0).getAsJsonObject().get("mailserver").getAsString();
            String email_IsTLS = fromEmailInfoArray.get(0).getAsJsonObject().get("istls").getAsString();
            String email_UserId = fromEmailInfoArray.get(0).getAsJsonObject().get("userid").getAsString();
            String email_IsSSL = fromEmailInfoArray.get(0).getAsJsonObject().get("isssl").getAsString();

////        String email_MailServer = "localhost";
//        String email_MailServer = "smtp.office365.com";
////        String email_MailServer_port = "25";
//        String email_MailServer_port = "587";
//        String email_UserId = "lintsai@crm.com.tw";
//        String email_Password = "Ji3g4h94cj/6xup6";
//        String email_Charset = "UTF-8";
//        String email_IsSSL = "false";
//        String email_IsTLS = "true";

            Util.getFileLogger().info(
                    "[InfoAPI]ToolsController(EmailSender) - emailToName: "
                            + emailToName);

            String[] emailToNameArray = emailToName.split(";");

            Map<String, String> emailToNameMap = new HashMap<>();
            for (String emailToNameVal : emailToNameArray) {
                emailToNameMap.put(emailToNameVal, emailToNameVal);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - emailToNameVal: "
                                + emailToNameVal);
            }
//        jsonObject.addProperty("emailToName", emailToName);

            Util.getFileLogger().info(
                    "[InfoAPI]ToolsController(EmailSender) - emailToNameForCc: "
                            + emailToNameForCc);

            Map<String, String> emailToNameMapForCc = new HashMap<>();
            if (!emailToNameForCc.equals("none")) {
                String[] emailToNameForCcArray = emailToNameForCc.split(";");
                for (String emailToNameForCcVal : emailToNameForCcArray) {
                    emailToNameMapForCc.put(emailToNameForCcVal,
                            emailToNameForCcVal);
                }
            }


            Util.getFileLogger().info(
                    "[InfoAPI]ToolsController(EmailSender) - emailToNameForBcc: "
                            + emailToNameForBcc);

            Map<String, String> emailToNameMapForBcc = new HashMap<>();
            if (!emailToNameForBcc.equals("none")) {
                String[] emailToNameForBccArray = emailToNameForBcc.split(";");
                for (String emailToNameForBccVal : emailToNameForBccArray) {
                    emailToNameMapForBcc.put(emailToNameForBccVal,
                            emailToNameForBccVal);
                }
            }

            Map<String, byte[]> emailAttachMap = new HashMap<>();
            byte[] emailAttachAsBytes = null;
            if (!emailAttach.equals("")
                    && !emailAttachName.equals("")) {
                String csv = null;
                try {
                    JSONArray docs = new JSONArray(emailAttach);

                    csv = CDL.toString(docs);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    Util.getFileLogger().error(
                            "[InfoAPI]ToolsController(EmailSender) - JSONException: "
                                    + e.getMessage());

                }
                try {
                    emailAttachAsBytes = csv.getBytes("BIG5");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    Util.getFileLogger().error(
                            "[InfoAPI]ToolsController(EmailSender) - Exception: "
                                    + e.getMessage());

                }
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - csv: " + csv);
                emailAttachMap.put(emailAttachName, emailAttachAsBytes);
            }
            EmailSender sender = new EmailSender(emailToNameMap,
                    emailToNameMapForCc, emailToNameMapForBcc, emailAttachMap,
                    subject, content);
//		sender.setSenderAddress("ChatRobot@fareastone.com.tw");
//		sender.setSenderName("ITT");
            sender.setSenderAddress(email_SenderAddress);
            sender.setSenderName(email_SenderName);

            try {
                Util.getFileLogger()
                        .info("[InfoAPI]ToolsController(EmailSender) - setEmailServer GO");
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_MailServer: "
                                + email_MailServer);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_MailServer_port: "
                                + email_MailServer_port);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_UserId: "
                                + email_UserId);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_Password: "
                                + email_Password);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_Charset: "
                                + email_Charset);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_IsSSL: "
                                + email_IsSSL);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - email_IsTLS: "
                                + email_IsTLS);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - msgtype: "
                                + msgtype);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - contentType: "
                                + contentType);
                sender.setMailServer(email_MailServer);
                sender.setServerPort(email_MailServer_port);
                sender.setServerPortInt(Integer.valueOf(email_MailServer_port)
                        .intValue());
                sender.setUserId(email_UserId);
                sender.setPassword(email_Password);
                sender.setCharset(email_Charset);
                sender.setIsSSL(Boolean.valueOf(email_IsSSL));
                sender.setIsTLS(Boolean.valueOf(email_IsTLS));
                sender.setMsgType(msgtype);
                sender.setContentType(contentType);
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - sendEmail GO");
                sender.sendEmail();
                Util.getFileLogger().info(
                        "[InfoAPI]ToolsController(EmailSender) - sendEmail END");
            } catch (EmailException e) {
                e.printStackTrace();

                Util.getFileLogger().error(
                        "[InfoAPI]ToolsController(EmailSender) - EmailException: "
                                + e.getMessage());

            } catch (Exception e) {
                e.printStackTrace();

                Util.getFileLogger().error(
                        "[InfoAPI]ToolsController(EmailSender) - Exception: "
                                + e.getMessage());

            }

            //update[tblEmailOut] table

            System.out.println(String.format("+++Count: %s，DateTime：%s", count2++, dateFormat.format(new Date())));
        }
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer
      propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }

}
