package jp.co.bizreach.camp;

import javafx.scene.Camera;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CameraImage {

	private static final Log log = LogFactory.getLog(CameraImage.class);

	/**
	 * TODO /tmp/motion配下の最新のjpgファイルをひとつ取得して java.io.Fileで返す
	 *
	 */
	public File getLatestImage() {
		log.info("getting image file!");
		return new File("");
	}

}
