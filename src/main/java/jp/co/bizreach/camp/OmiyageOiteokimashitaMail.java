package jp.co.bizreach.camp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
public class OmiyageOiteokimashitaMail {

	private static final Log log = LogFactory.getLog(OmiyageOiteokimashitaMail.class);

	@Autowired
	private CameraImage cameraImage;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private SshLogic sshLogic;

	@Scheduled(fixedDelay = 100)
	public void execute() {

		// FIXME このへんにボタンが押された状態であることを判定する処理を追加
		if (!isSwichOn()) return;
		
		sshLogic.countDown();
		cameraImage.getLatestImage().ifPresent(imageFile -> {
			sendMail.send(imageFile);
		});
	}

	public boolean isSwichOn() {
		try {
			File file = new File("/sys/class/gpio/gpio2/value");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();
			if (str != null) {
				if (str.equals("1")) {
					br.close();
					return true;
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			log.trace("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return false;
	}
}
