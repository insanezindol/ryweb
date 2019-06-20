package kr.co.reyonpharm.util;

import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class Constants {

	public static Properties configProp;

	// 환경변수 조회용 상수
	public static final String SERVER_TYPE = "system.whoami";
	//public static final String SMTP_IP = "smtp.ip";
	//public static final String SMTP_PORT = "smtp.port";
	public static final String SMTP_ACCOUNT = "smtp.account";
	//public static final String SMTP_PASSWORD = "smtp.password";
	public static final String SYSTEM_FILE_DIR = "system.filedir";
	public static final String SYSTEM_TEMP_DIR = "system.tempDir";
	public static final String USER_LIST_FILENAME = "totalUserList.filename";
	public static final String DEPT_LIST_FILENAME = "totalDeptList.filename";
	public static final String WEATHER_LIST_FILENAME = "weather.filename";
	public static final String GOOGLE_FCM_URL = "google.fcm.url";
	public static final String GOOGLE_FCM_KEY = "google.fcm.key";

	public static void setConfigProp(Properties configProp) {
		Constants.configProp = configProp;
	}

}