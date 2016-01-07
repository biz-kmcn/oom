package jp.co.bizreach.camp;

import javafx.scene.Camera;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;


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
		
		File directory = new File("/tmp/motion");
		// get just files, not directories
		File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);

//		System.out.println("Default order");
//		File file = findImage(files);
//
//		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
//		System.out.println("\nLast Modified Ascending Order (LASTMODIFIED_COMPARATOR)");
//		File file = findImage(files);

		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
//		System.out.println("\nLast Modified Descending Order (LASTMODIFIED_REVERSE)");
		File file = findImage(files);
		
		return file;
	}
	
	public File findImage(File[] files) {
		for (File file : files) {
			//System.out.printf("File: %-20s Last Modified:" + new Date(file.lastModified()) + "\n", file.getName());
			if (FilenameUtils.getExtension(file.getName()).toLowerCase() == "jpg") {
				return file;
			}
		}
		return new File("");
	}


}
