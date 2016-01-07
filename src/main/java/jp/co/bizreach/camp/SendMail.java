package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);

	/**
	 * TODO Fileを受け取ってそれをメール添付で送信する
	 *
	 * @param file
	 */
	public void send(File file) {
		log.info("mail sending!!!");
	}
}
