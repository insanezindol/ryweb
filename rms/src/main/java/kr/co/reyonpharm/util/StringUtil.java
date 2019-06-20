package kr.co.reyonpharm.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {
	
	//날짜(시간) 구분자 상수정의
	private static final String[][] DIV = {
		{".","."," ",".","."},
		{"/","/","",":",":"},
		{"년 ","월 ","일","시 ","분 "},
		{"-","-","",":",":"}
	};
	
	/**
	 * @param str
	 * @return
	 */
	public static String nullCheck(String str)
	{
		return((str == null || str.equals("null") || str.length() == 0) ? "" : str.trim());
	}
	
	/**
	 * @param request
	 * @param param
	 * @return
	 */
	public static String reqNullCheck(HttpServletRequest request, String param)
	{
		return nullCheck(request.getParameter(param));
	}
	
	/**
	 * 한글 파라미터 처리
	 * @param req
	 * @param paramKey
	 * @return
	 */
	public static String reqNullCheckHangulUTF8(HttpServletRequest req, String paramKey) {

		String searchText = reqNullCheck(req, paramKey);
		String result = "";
		/* set searhText */
		try {
			if(searchText != null) {
				
				//searchText = java.net.URLDecoder.decode(searchText);
				if(containsHangul(searchText) ) {
					result = searchText;
				} else {
					/* 한글이 포함되지 않은 경우 */
					result = new String(searchText.getBytes("8859_1"),"utf-8");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 문자열에서 한글 존재여부 확인
	 * @param str
	 * @return
	 */
	public static boolean containsHangul(String str)
	{
	    for(int i = 0 ; i < str.length() ; i++)
	    {
	        char ch = str.charAt(i);
	        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
	        if(UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||
	                UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) ||
	                UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)){
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * @param request
	 * @param string
	 * @return
	 */
	public static int reqNullCheckIntVal(HttpServletRequest request, String string) 
	{
		int intVal = 0;
		String str = reqNullCheck(request, string);

		try {
			intVal = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}

		return intVal;
	}
	
	/**
	 * @param request
	 * @param string
	 * @return
	 */
	public static BigInteger reqNullCheckBigIntVal(HttpServletRequest request, String string) 
	{
		BigInteger bigIntVal = BigInteger.ZERO;
		String str = reqNullCheck(request, string);

		try {
			bigIntVal = new BigInteger(str);
		} catch (NumberFormatException e) {
			return BigInteger.ZERO;
		}

		return bigIntVal;
	}
	
	/**
	 * @param request
	 * @param string
	 * @return
	 */
	public static long reqNullCheckLongVal(HttpServletRequest request, String string) 
	{
		Long longVal = Long.valueOf(0);
		String str = reqNullCheck(request, string);

		try {
			longVal = Long.valueOf(str);
		} catch (NumberFormatException e) {
			return longVal;
		}

		return longVal;
	}
	
    /**
     * 문자열이 비어 있지 않은지 확인.
     * 
     * @param str
     *            원본 문자열.
     * @return true if the String is non-null, and not length zero
     */
    public static boolean isNotEmpty(String str) {
        return ( str != null && str.length() > 0 );
    }
    
    /**
     * Checks if is empty.
     * 
     * @param str
     *            the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        return ( str == null || str.length() == 0 );
    }
    
    // 가격 포멧
	public static String formatPrice(long price) {
		NumberFormat fmt = NumberFormat.getInstance();
		return fmt.format(price);
	}
	
	
	/**
	 * yyyymmddHHMMDD : 년월일시분초에 대한 String을 구분자로 표기하여 가져온다.
	 * @param String dt          : yyyymmddHHMMDD(년월일시분초) 
	 * @param int divNo          : 표기 구분값(0,1,2)
	 * @param boolean isViewTime : 시간 표시여부
	 * @return String            : 구분자를 추가한 날짜
	*/
	public static String getDivDate(String dt, int divNo, boolean isViewTime) {
		
		if(dt == null || dt == "") {
			return "";
		}
		
		StringBuilder result = new StringBuilder();
		
		String year = "";     //년
		String month = "";    //월
		String day = "";      //일
		String hour = "";     //시
		String minute = "";   //분
		//String second = "";   //초

		year = dt.substring(0, 4);
		month = dt.substring(4, 6);
		
		if(dt.length() != 8) {
			day = dt.substring(6, 8);
		} else {
			day = dt.substring(6);
		}
		
		if(dt.length() == 14) {
			hour = dt.substring(8, 10);
			minute = dt.substring(10, 12);
			//second = dt.substring(13);
		}
		
		result.append(year + DIV[divNo][0]);
		result.append(month + DIV[divNo][1]);
		result.append(day + DIV[divNo][2]);
			
		if(isViewTime) {
			result.append(" " + hour + DIV[divNo][3]);
			result.append(minute);
		}
		
		return result.toString();
	}
	
	/**
	 * 비밀번호 패턴 매칭 체크
	 * @param pwd
	 * @return true|false
	 */
	public static boolean pwdRegexCheck(String pwd) {
		// []안에 들어가는 규칙과 관련한 [, ], -, _ 4개는 escape 대상, java 기본 escape \ escape 대상
		// 문자열 의미인 "는 escape 대상
		// escape 전 정규식 문자열 : (?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~`!@#$%^&*()-_+={}[]\|;:'"<>,./?]).{1,15}
    	Pattern p = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~`!@#$%^&*()\\-\\_+={}\\[\\]\\\\|;:'\"<>,./?]).{8,15}$");
    	Matcher m = p.matcher(pwd);
    	
    	if (m.find()){
    	    log.debug("비밀번호 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("비밀번호 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * 영문숫자 패턴 매칭 체크
	 * @param str
	 * @return true|false
	 */
	public static boolean strAlphaNumericCheck(String str) {
    	Pattern p = Pattern.compile("^[A-Za-z0-9]*$");
    	Matcher m = p.matcher(str);
    	
    	if (m.find()){
    	    log.debug("영문숫자 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("영문숫자 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * 이메일 패턴 매칭 체크
	 * @param email
	 * @return true|false
	 */
	public static boolean emailRegexCheck(String email) {
		// []안에 들어가는 규칙과 관련한 -, _ 2개는 escape 대상, java 기본 escape \ escape 대상
    	Pattern p = Pattern.compile("^[0-9a-zA-Z]([\\-\\_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([\\-\\_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$");
    	Matcher m = p.matcher(email);
    	
    	if (m.find()){
    	    log.debug("이메일 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("이메일 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * 이메일 뒤 세자리 마스킹
	 * @param email
	 * @return masked email
	 */
	public static String emailRegexMasked(String email) {
		String regex = "\\b(\\S+)+@(\\S+.\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(email);
		if (matcher.find()) {
			String id = matcher.group(1);

			int length = id.length();
			if (length == 1) {
				return email.replaceAll("\\b(\\S+)+@(\\S+)", "*@$2");
			} else if (length == 2) {
				return email.replaceAll("\\b(\\S+)[^@]+@(\\S+)", "**@$2");
			} else if (length == 3) {
				return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "***@$2");
			} else {
				return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
			}

		}
		return email;
	}
	
	/**
	 * 전화번호 패턴 매칭 체크
	 * @param phoneNumber
	 * @return true|false
	 */
	public static boolean phoneNumberRegexCheck(String phoneNumber) {
    	Pattern p = Pattern.compile("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})[0-9]{3,4}?[0-9]{4}$");
    	Matcher m = p.matcher(phoneNumber);
    	
    	if (m.find()){
    	    log.debug("전화번호 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("전화번호 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * 앞번호 두자리 뒷번호 한자리 마스킹 (하이픈 있음)
	 * @param phoneNumber
	 * @return masked phoneNumber
	 */
	public static String phoneNumberRegexMasked(String phoneNumber) {
		// return phoneNumber.replaceAll("(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})$", "$1-****-$3");
		return phoneNumber.replaceAll("^(\\d{3})-?(\\d{1,2})\\d{2}-?\\d(\\d{3})$", "$1-$2**-*$3");
	}
	
	/**
	 * 앞번호 두자리 뒷번호 한자리 마스킹 (하이픈 없음)
	 * @param phoneNumber
	 * @return masked phoneNumber
	 */
	public static String phoneNumberNonHyphenRegexMasked(String phoneNumber) {
		// return phoneNumber.replaceAll("(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})$", "$1-****-$3");
		return phoneNumber.replaceAll("^(\\d{3})-?(\\d{1,2})\\d{2}-?\\d(\\d{3})$", "$1$2***$3");
	}
	
	/**
	 * IP 패턴 매칭 체크
	 * @param ip
	 * @return true|false
	 */
	public static boolean ipRegexCheck(String ip) {
    	Pattern p = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    	Matcher m = p.matcher(ip);
    	
    	if (m.find()){
    	    log.debug("ip 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("ip 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * IP 첫번째 세자리 세번째 세자리 마스킹
	 * @param ip
	 * @return masked ip
	 */
	public static String ipRegexMasked(String ip) {
		String regex = "\\b(\\S+)\\.(\\S+)\\.(\\S+)\\.(\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(ip);
		if (matcher.find()) {
			int octet1Length = matcher.group(1).length();
			int octet3Length = matcher.group(3).length();

			if (octet1Length == 1 && octet3Length == 1) {
				return ip.replaceAll("\\b[^@]\\.(\\S+)\\.[^@]\\.(\\S+)", "*.$1.*.$2");
			} else if (octet1Length == 1 && octet3Length == 2) {
				return ip.replaceAll("\\b[^@]\\.(\\S+)\\.[^@][^@]\\.(\\S+)", "*.$1.**.$2");
			} else if (octet1Length == 1 && octet3Length == 3) {
				return ip.replaceAll("\\b[^@]\\.(\\S+)\\.[^@][^@][^@]\\.(\\S+)", "*.$1.***.$2");
			} else if (octet1Length == 2 && octet3Length == 1) {
				return ip.replaceAll("\\b[^@][^@]\\.(\\S+)\\.[^@]\\.(\\S+)", "**.$1.*.$2");
			} else if (octet1Length == 2 && octet3Length == 2) {
				return ip.replaceAll("\\b[^@][^@]\\.(\\S+)\\.[^@][^@]\\.(\\S+)", "**.$1.**.$2");
			} else if (octet1Length == 2 && octet3Length == 3) {
				return ip.replaceAll("\\b[^@][^@]\\.(\\S+)\\.[^@][^@][^@]\\.(\\S+)", "**.$1.***.$2");
			} else if (octet1Length == 3 && octet3Length == 1) {
				return ip.replaceAll("\\b[^@][^@][^@]\\.(\\S+)\\.[^@]\\.(\\S+)", "***.$1.*.$2");
			} else if (octet1Length == 3 && octet3Length == 2) {
				return ip.replaceAll("\\b[^@][^@][^@]\\.(\\S+)\\.[^@][^@]\\.(\\S+)", "***.$1.**.$2");
			} else {
				return ip.replaceAll("\\b[^@][^@][^@]\\.(\\S+)\\.[^@][^@][^@]\\.(\\S+)", "***.$1.***.$2");
			}
		}
		return ip.replaceAll("\\b(\\S+)\\.(\\S+)\\.(\\S+)\\.(\\S+)", "***.$2.***.$4");
	}
	
	/**
	 * 뒤에 세자리 마스킹
	 * @param str
	 * @return masked string
	 */
	public static String lastThreeStringRegexMasked(String str) {
		return str.replaceAll("(?!.{4}).", "*");
	}

	/**
	 * 뒤에 한자리 마스킹
	 * @param str
	 * @return masked string
	 */
	public static String lastOneStringRegexMasked(String str) {
		return str.replaceAll("(?!.{2}).", "*");
	}
	
	/**
	 * 로그 파일명 패턴 매칭 체크
	 * @param fileName
	 * @return true|false
	 */
	public static boolean logFileNameRegexCheck(String fileName) {
    	Pattern p = Pattern.compile("^(service\\_[0-9a-zA-Z]*.out\\.[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\.[a-z]{3})$");
    	Matcher m = p.matcher(fileName);
    	
    	if (m.find()){
    	    log.debug("파일명 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("파일명 패턴 매칭 실패");
    		return false;
    	}
	}	

	/**
	 * 날짜(YYYY-MM-DD) 패턴 매칭 체크
	 * @param date
	 * @return true|false
	 */
	public static boolean dateRegexCheck(String date) {
    	Pattern p = Pattern.compile("^([0-9]{4}\\-[0-9]{2}\\-[0-9]{2})$");
    	Matcher m = p.matcher(date);
    	
    	if (m.find()){
    	    log.debug("날짜 패턴 매칭 성공");
    	    return true;
    	}else{
    		log.debug("날짜 패턴 매칭 실패");
    		return false;
    	}
	}
	
	/**
	 * 결과 코드를 문자로 치환
	 * @param String para   : 코드
	 * @returns String      : 치환 문자열
	 */
	public static String cfCtStatus(String txt) {
		String rtnTxt  = "";
		if (txt.equals("01")) {
			rtnTxt = "강종";
		} else if (txt.equals("02")) {
			rtnTxt = "일치";
		} else if (txt.equals("03")) {
			rtnTxt = "이탈";
		}
		return rtnTxt;
	}

	/**
	 * 모의/루팅 코드를 문자로 치환
	 * @param String para   : 코드
	 * @returns String      : 치환 문자열
	 */
	public static String cfCtImigps(String txt) {
		String rtnTxt  = "";
		if (txt.equals("1")) {
			rtnTxt = "Y";
		} else if (txt.equals("0")) {
			rtnTxt = "N";
		}
		return rtnTxt;
	}

	/**
	 * 계획구분 코드를 문자로 치환
	 * @param String para   : 코드
	 * @returns String      : 치환 문자열
	 */
	public static String cfCtPlan(String txt) {
		String rtnTxt  = "";
		if (txt.equals("Y")) {
			rtnTxt = "정상";
		} else if (txt.equals("A")) {
			rtnTxt = "추가";
		}
		return rtnTxt;
	}
	
	/*public static void main (String[] args) {
		String str = "service_was1.out.2017-05-13.log";
		System.out.println(logFileNameRegexCheck(str));
	}*/
	
    
}