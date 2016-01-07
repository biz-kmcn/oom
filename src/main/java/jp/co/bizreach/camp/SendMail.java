package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author david.genesis.cruz
 *
 */
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
	private static final List<String> toEmails = Arrays.asList(new String[]{
		    "ken.toriumi@bizreach.co.jp",
		    "david.genesis.cruz@bizreach.co.jp",
		    "kyunbum.yi@bizreach.co.jp",
		    "akane.sutou@bizreach.co.jp",
		    "hongseok.jeong@bizreach.co.jp",
		    "misawa@bizreach.co.jp",
		    "hayato.nakamura@bizreach.co.jp",
		    "kota.inoue@bizreach.co.jp",
		    "even.he@bizreach.co.jp",
		    "akifumi.tominaga@bizreach.co.jp",
		    "katsumaru@bizreach.co.jp",
		    "kazuhiro.yoshimoto@bizreach.co.jp",
		    "hori@bizreach.co.jp",
		    "tomoya.masuri@bizreach.co.jp",
		    "junya.masuda@bizreach.co.jp",
		    "masayuki.ouchi@bizreach.co.jp",
		    "yuki.ohnishi@bizreach.co.jp",
		    "kyota.yasuda@bizreach.co.jp",
		    "masaki.ogawa@bizreach.co.jp",
		    "kosuke.obata@bizreach.co.jp",
		    "tomoki.iwai@bizreach.co.jp",
		    "kazuya.hirota@bizreach.co.jp",
		    "daisuke.megumi@bizreach.co.jp",
		    "sotsuka@bizreach.co.jp",
		    "aram.park@bizreach.co.jp",
		    "ayumi.toukairin@bizreach.co.jp",
		    "koichiro.matsuoka@bizreach.co.jp",
		    "yuta.yokoo@bizreach.co.jp",
		    "yuuki.ikehata@bizreach.co.jp",
		    "kouchi@bizreach.co.jp",
		    "akira.tsuno@bizreach.co.jp",
		    "daisuke.sei@bizreach.co.jp",
		    "yu.watanabe@bizreach.co.jp",
		    "sawaguchi@bizreach.co.jp",
		    "mashiko@bizreach.co.jp",
		    "yuta.ishizaka@bizreach.co.jp",
		    "shinji.sogawa@bizreach.co.jp",
		    "yuuta.inoo@bizreach.co.jp",
		    "takayuki.takemura@bizreach.co.jp",
		    "satoshi.tanaka@bizreach.co.jp",
		    "toshiaki.arai@bizreach.co.jp",
		    "masaki.kamachi@bizreach.co.jp",
		    "shouta.fujino@bizreach.co.jp",
		    "junpei.nishina@bizreach.co.jp",
		    "jumpei.toyoda@bizreach.co.jp",
		    "tatsuhiro.gunji@bizreach.co.jp",
		    "mayuko.sakaba@bizreach.co.jp",
		    "keisuke.shigematsu@bizreach.co.jp",
		    "suzuki@bizreach.co.jp",
		    "yuuki.nagahara@bizreach.co.jp",
		    "yasuhiro.sakamoto@bizreach.co.jp",
		    "takakura@bizreach.co.jp",
		    "kohei.takata@bizreach.co.jp",
		    "shuichiro.washio@bizreach.co.jp",
		    "hiroki.saito@bizreach.co.jp"
	});
	private static final String subject = "お土産があります！";
	private static final String mailGreeting = "合宿中のみなさん、";
	private static final String mailHeader = "お土産を置いてきました！";
	private static final String mailFooter = "自由に食べてください！";
	
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
			// 全員に送信
			//			for (String toEmail : toEmails) {
			//				email.addTo(toEmail);
			//			}
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
