package jp.co.bizreach.camp;

import javafx.scene.Camera;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;


import java.io.File;
import java.util.Optional;

@Component
public class CameraImage {

	private static final Log log = LogFactory.getLog(CameraImage.class);
	private String latestImageFileHash = null;

	/**
	 * TODO /tmp/motion配下の最新のjpgファイルをひとつ取得して java.io.Fileで返す
	 *
	 */
	public Optional<File> getLatestImage() {
		log.trace("getting image file!");
		
		File directory = new File("/tmp/motion");
		// get just files, not directories
		File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		File file = findImage(files);

		// 同じ画像を重複して検知しないように、最後に取得したファイルのハッシュ値を保存しておく。
		try {
			FileInputStream fis = new FileInputStream(file);
			String fileHash = DigestUtils.md5Hex(fis);
			if (fileHash.equals(this.latestImageFileHash)) {
				return Optional.empty();
			} else {
				log.info("new file find ! " + file.toString());
				this.latestImageFileHash = fileHash;
				return Optional.of(file);
			}
		} catch (IOException e) {
			return Optional.empty();
		}
	}
	
	public File findImage(File[] files) {
		for (File file : files) {
			if (FilenameUtils.getExtension(file.getName()).toLowerCase().equals("jpg")) {
				return file;
			}
		}
		return null;
	}


}
