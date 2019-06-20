package kr.co.reyonpharm.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.CustomUserDetails;

//@Slf4j
@Component
public class SpringSecurityUtil {

	private static final RequestMatcher AJAX_REQUEST_MATCHER = new ELRequestMatcher("hasHeader('X-Requested-With', 'XMLHttpRequest')");

	public static final String JSON_VALUE = "{\"%s\": %s}";

	/**
	 * 요청 헤더를 확인하여 Ajax 요청여부 리턴 Spring Security LIB 이용
	 */
	public static Boolean isAjaxRequest(HttpServletRequest request) {
		return AJAX_REQUEST_MATCHER.matches(request);
	}

	/**
	 * 로그인한 사용자의 UserDetails 리턴
	 */
	public static CustomUserDetails getCustomUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 로그인 이전에는 auth가 null 이거나 getPrincipal()의 결과가 "anonymous"(String)일 수 있다.
		// 로그인 이후에는 getPrincipal()은 CustomUserDetails 객체이다.
		if (auth == null || auth.getPrincipal() instanceof String) {
			return null;
		}

		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails;

		} else {
			return null;
		}
	}

	/**
	 * 로그인한 사용자의 아이디(adminId) 리턴
	 */
	public static String getUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getUsername();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 사업자명 리턴
	 */
	public static String getSaupName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getSaupName();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 사용권한 리턴
	 */
	public static String getFname() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getUserRole();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 사용구분 리턴
	 */
	public static String getDeptCode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getUseYn();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 등록일자 리턴
	 */
	public static String getDeptName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getRegDate();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}
	
}
