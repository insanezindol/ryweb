package kr.co.reyonpharm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import kr.co.reyonpharm.models.HttpClientResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

	// jsonResponse
	public static void jsonResponse(HttpServletResponse response, Map<String, Object> jsonData) {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");

		StringBuffer responseString = new StringBuffer();
		String keyString = "";
		String valueString = "";
		int i = 0;
		for (String key : jsonData.keySet()) {
			if (i == 0)
				keyString = "{\"" + key + "\":";
			else
				keyString = ",\"" + key + "\":";

			if (jsonData.get(key) instanceof Integer)
				valueString = jsonData.get(key).toString();
			else if (jsonData.get(key) instanceof String) {
				valueString = "\"" + jsonData.get(key).toString() + "\"";
			}

			responseString.append(keyString);
			responseString.append(valueString);
			i++;
		}
		responseString.append("}");

		try {
			response.getWriter().write(responseString.toString());
		} catch (IOException e) {
			log.error("Error writing json to response", e);
		}
	}

	// Client IP 확인
	public static String getClientIp(HttpServletRequest req) {
		// 아파치, 웹로직, L4, Proxy 등이 앞에 있을 경우 클라이언트 IP 확인하기
		String ip = req.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		return ip;
	}

	// 배포 임시 테이블에서 서비스 테이블로 데이터를 복사할때 문자열을 치환해주는 함수
	public static String checkWord(String str) {
		String output = str;
		// 쌍따옴표 (제거)
		output = replaceAll(output, "&quot;", "");
		output = replaceAll(output, "\"", "");
		// 부등호 꺽쇠 (괄호로 대체)
		output = replaceAll(output, "&lt;", "(");
		output = replaceAll(output, "&gt;", ")");
		output = replaceAll(output, "<", "(");
		output = replaceAll(output, ">", ")");
		// 홀따옴표 ( ` 로 대체)
		output = replaceAll(output, "&#39;", "`");
		output = replaceAll(output, "&rsquo;", "`");
		output = replaceAll(output, "&lsquo;", "`");
		output = replaceAll(output, "'", "`");
		// 앰퍼샌드 ( n 으로 대체
		output = replaceAll(output, "&amp;", " n ");
		output = replaceAll(output, "&", " n ");
		return output;
	}

	// 실제 문자열을 치환하는 함수
	public static String replaceAll(String source, String pattern, String replacement) {
		if (source == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int index;
		int patIndex = 0;
		while ((index = source.indexOf(pattern, patIndex)) != -1) {
			sb.append(source.substring(patIndex, index));
			sb.append(replacement);
			patIndex = index + pattern.length();
		}
		sb.append(source.substring(patIndex));
		return sb.toString();
	}

	// 문자열 길이를 짤라서 리턴해주는 함수
	public static String stringCut(String text, int cutLength) {
		String outputText = text;
		if (outputText == null) {
			outputText = "";
		}
		if (text.length() >= cutLength) {
			outputText = text.substring(0, cutLength);
		}
		return outputText;
	}

	// 성인음원 여부 NULL 체크
	public static String getR19LicenceTf(String r19LicenceTf) {
		String output = r19LicenceTf;
		if (output == null) {
			output = "G";
		}
		return output;
	}

	// MSSQL DB TBL_BELLCONTENT, TBL_BELLCONTENT_SUB, TBL_BELLCONTENT_INFO 테이블
	// regidate와 modidate에 들어가는 값
	public static String getMssqlDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.KOREA);
		return format.format(now);
	}

	/**
	 * @return HttpClientResult
	 */
	public static HttpClientResult getHttpClient(String url) {
		HttpClientResult httpClientResult = new HttpClientResult();
		httpClientResult.setResult(false);

		if (url == null || "".equals(url)) {
			httpClientResult.setResultMsg("URL is empty");
			return httpClientResult;
		}

		HttpClient httpclient = new DefaultHttpClient();
		StringBuffer stringBuffer = new StringBuffer();

		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			String status = response.getStatusLine().toString();

			if (status.indexOf("HTTP/1.1 200") < 0) {
				httpClientResult.setResultMsg("연결할 수 없습니다.");
				return httpClientResult;
			}

			if (entity != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line);
				}
			}

			httpget.abort();
			httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			httpClientResult.setResultMsg("연결할 수 없습니다.");
			return httpClientResult;
		}

		httpClientResult.setResult(true);
		httpClientResult.setResultMsg(stringBuffer.toString());
		return httpClientResult;
	}

	/**
	 * 외부 URL 호출 (Post 방식)
	 * 
	 * @param url,
	 *            params
	 * @return HttpClientResult
	 */
	public static HttpClientResult postHttpClient(String url, Map<String, String> params) {
		HttpClientResult httpClientResult = new HttpClientResult();
		httpClientResult.setResult(false);

		if (url == null || "".equals(url)) {
			httpClientResult.setResultMsg("URL is empty");
			return httpClientResult;
		}

		HttpClient httpclient = new DefaultHttpClient();
		StringBuffer stringBuffer = new StringBuffer();

		try {
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			Iterator<String> keys = params.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
			}

			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "euc-kr"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String status = response.getStatusLine().toString();

			if (status.indexOf("HTTP/1.1 200") < 0) {
				httpClientResult.setResultMsg("연결할 수 없습니다.");
				return httpClientResult;
			}

			if (entity != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line);
				}
			}

			httpPost.abort();
			httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();
			httpClientResult.setResultMsg("연결할 수 없습니다.");
			return httpClientResult;
		}

		httpClientResult.setResult(true);
		httpClientResult.setResultMsg(stringBuffer.toString());
		return httpClientResult;
	}

	/***************************
	 * 맵의 내용이 NULL 인지 체크하는 함수
	 ***************************/
	public static String valueToStringOrEmpty(HashMap<Object, Object> map, String key) {
		Object value = map.get(key);
		return value == null ? "(null)" : value.toString();
	}

}
