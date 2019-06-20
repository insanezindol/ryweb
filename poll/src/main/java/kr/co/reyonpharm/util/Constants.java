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
	public static final String IP_REAL = "ip.real";

	public static void setConfigProp(Properties configProp) {
		Constants.configProp = configProp;
	}

}