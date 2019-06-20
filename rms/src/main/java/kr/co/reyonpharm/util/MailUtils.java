package kr.co.reyonpharm.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailUtils {

	// SMTP 메일 전송
	// 20180930 메일 전환으로 종료 (Exchange -> Groupware)
	/*public static void sendNotifyEmail(String id, String title, StringBuffer contents) throws Exception {
		log.info("[BEG] title : " + title);
		String user = Constants.configProp.getProperty(Constants.SMTP_ACCOUNT);
		String password = Constants.configProp.getProperty(Constants.SMTP_PASSWORD);
		String addr = id + "@reyonpharm.co.kr";
		Properties prop = new Properties();
		prop.put("mail.smtp.host", Constants.configProp.getProperty(Constants.SMTP_IP));
		prop.put("mail.smtp.port", Constants.configProp.getProperty(Constants.SMTP_PORT));

		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
		message.setSubject(title);
		message.setText(contents.toString(), "UTF-8", "html");

		Transport.send(message);
		log.info("[END] addr : " + addr);
	}*/

}
