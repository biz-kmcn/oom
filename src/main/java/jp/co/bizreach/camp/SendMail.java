package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);
	private HtmlEmail email = new HtmlEmail();
	
	// account setup
	private static final String hostName = "smtp.gmail.com";
	private static final int smtpPort = 587;
	private static final String userName = "luntiang.pentakulo@gmail.com";
	private static final String password = System.getProperty("smtpPasswd");
	private static final String fromEmail = "luntiang.pentakulo@gmail.com";
	private static final String fromName = "お土産おじさん";
	
	// email contents
	private static final String toEmail = "david.genesis.cruz@bizreach.co.jp";
	private static final String subject = "お土産を置いてきました！";
	private static final String mailGreeting = "プロダクトマーケのみなさん、";
	private static final String mailHeader = "お土産を置いてきました！";
	private static final String mailFooter = "ご自由に食べてください！";
	
	/**
	 *  @param file
	 */
	public void send(File file) {
		log.info("setting up email account...");
		setupEmailAccount();
		log.info("creating email body...");
		if (file.exists()) {
			createEmail(file);
			log.info("sending mail...");
			try {
				email.send();
				log.info("mail sent!");
			} catch (EmailException e) {
				log.error("failed to send mail: " + email, e);
			}
		} else {
			log.error("attachment is null.");
		}
	}

	private void setupEmailAccount() {
		email.setHostName(hostName);
		email.setSmtpPort(smtpPort);
		email.setSslSmtpPort(String.valueOf(smtpPort));
		email.setStartTLSEnabled(true);
		email.setAuthentication(userName, password);
		email.setSSLOnConnect(true);
		try {
			email.setFrom(fromEmail, fromName);
		} catch (EmailException e) {
			log.error("failed to set FROM address:" + fromEmail, e);
		}
	}
	
	private void createEmail(File file) {
		try {
			email.addTo(toEmail);
		} catch (EmailException e) {
			log.error("failed to set TO: " + toEmail, e);
		}
		email.setSubject(subject);
		createEmailBody(file);
	}

	private void createEmailBody(File file) {
		String cid = embedImage(file);
		String mailBody = buildHtmlBody(cid);
		email.setCharset(EmailConstants.UTF_8);
		try {
			email.setHtmlMsg(mailBody);
		} catch (EmailException e) {
			log.error("failed to create email body: " + mailBody, e);
		}
	}

	private String buildHtmlBody(String cid) {
		String htmlOpenTags = "<html><body>";
		String htmlCloseTags = "</body></html>";
		String image = htmlCreateImageUrl("cid:" + cid);
		String signature = htmlBold(fromName);
		String htmlBody = htmlOpenTags + mailGreeting + htmlNewLine(3) + mailHeader + htmlNewLine(1) + image + htmlNewLine(1) + mailFooter + htmlNewLine(2) + signature + htmlCloseTags;
				
		return htmlBody;
	}

	private String embedImage(File file) {
		String cid = new String();
		try {
			log.info("attaching file: " + file);
			cid = email.embed(file);
		} catch (EmailException e) {
			log.error("failed to attach image: " + file.getPath(), e);
		}
		return cid;
	}
	
	// HTML helpers
	private String htmlCreateImageUrl(String src) {
		String imageOpenTag = "<img src=";
		String imageCloseTag = ">";
		return imageOpenTag + src + imageCloseTag;
	}
	
	private String htmlBold(String string) {
		String htmlStrongOpenTag = "<strong>";
		String htmlStrongCloseTag = "</strong>";
		return htmlStrongOpenTag + string + htmlStrongCloseTag;
	}
	
	private String htmlNewLine(int count) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= count; i++) {
			builder.append("<br>");
		}
		return builder.toString();
	}
	
}
