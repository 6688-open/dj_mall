package com.dj.mall.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送邮件
 */

public class MailUtils {

	private static String from = "18351867657@163.com";

	private static String password = "slh110711";

	private static String smtp = "smtp.163.com";

	private static String mailSmtpAuth = "true";

	public static void  sendMail(String to, String subject, String text, boolean isHtml){
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.host", smtp);//163 邮件服务器
			properties.put("mail.smtp.auth", mailSmtpAuth);
			Session session = Session.getDefaultInstance(properties,new Authenticator(){
				public PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(from, password); //发件人邮件用户名、授权码
				}
			});
			MimeMessage message = new MimeMessage(session);
			// Set From: 发件人
			message.setFrom(new InternetAddress(from));
			// Set To: 收件人
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			if(isHtml){
				message.setContent(text,"text/html;charset=UTF-8");
			} else {
				message.setText(text);
			}
			// 調用系統發送郵件的方法
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}


	}


}
