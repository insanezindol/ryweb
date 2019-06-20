package kr.co.reyonpharm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileIOUtils {

	public static String stringFileWrite(String text, String fileName) {
		String result = "0";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
			String s = text;
			writer.write(s);
			writer.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			result = "1";
		}
		return result;
	}

	public static String fileReadString(String vfile) {
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		File file = new File(vfile);
		String temp = "";
		StringBuffer content = new StringBuffer();
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			br = new BufferedReader(isr);

			while ((temp = br.readLine()) != null) {
				content.append(temp);
				content.append("\n");
			}
		} catch (FileNotFoundException e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				log.error(e.getClass() + ": " + e.getMessage(), e);
			}
			try {
				isr.close();
			} catch (IOException e) {
				log.error(e.getClass() + ": " + e.getMessage(), e);
			}
			try {
				br.close();
			} catch (IOException e) {
				log.error(e.getClass() + ": " + e.getMessage(), e);
			}
		}
		return content.toString();
	}

	public static void deleteAllFiles(String path) {
		File file = new File(path);
		File[] tempFile = file.listFiles();

		if(tempFile != null){
			if (tempFile.length > 0) {
				for (int i = 0; i < tempFile.length; i++) {
					if (tempFile[i].isFile()) {
						tempFile[i].delete();
					} else {
						deleteAllFiles(tempFile[i].getPath());
					}
					tempFile[i].delete();
				}
				file.delete();
			}
		}
	}
}
