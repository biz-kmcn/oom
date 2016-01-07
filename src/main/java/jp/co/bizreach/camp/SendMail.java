package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);
	private ImageHtmlEmail email = new ImageHtmlEmail();
	
	// account setup
	private static final String hostName = "smtp.gmail.com";
	private static final int smtpPort = 587;
	private static final String userName = "luntiang.pentakulo@gmail.com";
	private static final String password = "temporaryPassword";
	private static final String fromEmail = "luntiang.pentakulo@gmail.com";
	private static final String fromName = "お土産おじさん";
	
	// email contents
	private static final String toEmail = "david.genesis.bizreach@gmail.com";
	private static final String subject = "お土産を置いてきました！";
	private static final String mailHeader = "お土産を置いてきました！";
	private static final String mailFooter = "みんなで食べてください！";
	private static final String imageCid = "Omiyage Image";
	
	/**
	 * TODO Fileを受け取ってそれをメール添付で送信する
	 *
	 * @param file
	 */
	public void send(File file) {
		log.info("Setting up email account...");
		setupEmailAccount();
		log.info("Creating email body...");
		createEmail(file);
		log.info("Sending mail...");
		try {
			email.send();
		} catch (EmailException e) {
			log.error("Failed to send mail: " + email, e);
		}
		log.info("Mail sent!");
	}

	private void setupEmailAccount() {
		email.setHostName(hostName);
		email.setSmtpPort(smtpPort);
		email.setAuthentication(userName, password);
		email.setSSLOnConnect(true);
		try {
			email.setFrom(fromEmail, fromName);
		} catch (EmailException e) {
			log.error("Failed to set FROM address:" + fromEmail, e);
		}
	}
	
	private void createEmail(File file) {
		try {
			email.addTo(toEmail);
		} catch (EmailException e) {
			log.error("Failed to set TO: " + toEmail, e);
		}
		email.setSubject(subject);
		createEmailBody(file);
	}

	private void createEmailBody(File file) {
		String cid = embedImage(file);
		String mailBody = buildHtmlBody(cid);
		try {
			email.setHtmlMsg(mailBody);
		} catch (EmailException e) {
			log.error("Failed to create email body: " + mailBody, e);
		}
	}

	private String buildHtmlBody(String cid) {
		return "<html>" + mailHeader + "<br><img src=\"cid:" + cid + "\"><br>" + mailFooter + "</html>";
	}

	private String embedImage(File file) {
		String cid = new String();
		try {
			cid = email.embed(file, imageCid);
		} catch (EmailException e) {
			log.error("Failed to attach image: " + file.getPath(), e);
		}
		return cid;
	}
}
