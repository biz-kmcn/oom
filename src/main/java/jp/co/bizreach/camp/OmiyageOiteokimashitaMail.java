package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class OmiyageOiteokimashitaMail {

	private static final Log log = LogFactory.getLog(OmiyageOiteokimashitaMail.class);

	@Autowired
	private CameraImage cameraImage;

	@Autowired
	private SendMail sendMail;

	@Scheduled(fixedDelay = 2000)
	public void execute() {
		log.info("Hello World !");
		File imageFile = cameraImage.getLatestImage();
		sendMail.send(imageFile);
	}

}
