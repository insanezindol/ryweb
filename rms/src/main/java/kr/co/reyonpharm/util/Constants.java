package kr.co.reyonpharm.util;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class Constants {

	public static Properties configProp;

	// 환경변수 조회용 상수
	public static final String SERVICE_DOMAIN = "domain.url";
	public static final String SERVER_TYPE = "system.whoami";
	public static final String IP_LOCAL = "ip.local";
	public static final String IP_REAL1 = "ip.real";
	//public static final String SMTP_IP = "smtp.ip";
	//public static final String SMTP_PORT = "smtp.port";
	public static final String SMTP_ACCOUNT = "smtp.account";
	//public static final String SMTP_PASSWORD = "smtp.password";
	public static final String SYSTEM_FILE_DIR = "system.filedir";
	public static final String FILE_UPLOAD_DIR = "system.fileUploadDir";
	public static final String FILE_UPLOAD_TEMP_DIR = "system.fileUploadTempDir";
	public static final String USER_LIST_FILENAME = "totalUserList.filename";
	public static final String DEPT_LIST_FILENAME = "totalDeptList.filename";
	public static final String WEATHER_LIST_FILENAME = "weather.filename";
	public static final String FILE_UPLOAD_CONTRACT_DIR = "contract.fileUploadContractDir";
	public static final String FILE_UPLOAD_VEHICLE_DIR = "vehicle.fileUploadContractDir";
	public static final String FILE_UPLOAD_DECLARATION_DIR = "settlement.fileUploadDeclarationDir";
	public static final String FILE_UPLOAD_SPECIFICATION_DIR = "settlement.fileUploadSpecificationDir";
	public static final String FILE_UPLOAD_CONTROL_DIR = "control.fileUploadDir";
	public static final String RYHR_JDBC_URL = "ryhr.jdbc.url";
	public static final String RYHR_JDBC_USERNAME = "ryhr.jdbc.username";
	public static final String RYHR_JDBC_PASSWORD = "ryhr.jdbc.password";

	public static void setConfigProp(Properties configProp) {
		Constants.configProp = configProp;
	}

}