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

	@Autowired
	private SshLogic sshLogic;

	@Scheduled(fixedDelay = 2000)
	public void execute() {

		// FIXME このへんにボタンが押された状態であることを判定する処理を追加

		sshLogic.countDown();
		cameraImage.getLatestImage().ifPresent(imageFile -> {
			sendMail.send(imageFile);
		});
	}
}
