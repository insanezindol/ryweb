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
	 * 로그인한 사용자의 이름(kname) 리턴
	 */
	public static String getKname() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getKname();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 이름(fname) 리턴
	 */
	public static String getFname() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getFname();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 부서코드(deptCode) 리턴
	 */
	public static String getDeptCode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getDeptCode();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 부서이름(deptName) 리턴
	 */
	public static String getDeptName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getDeptName();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 사업장코드(saupcode) 리턴
	 */
	public static String getSaupcode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getSaupcode();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 사업장이름(saupname) 리턴
	 */
	public static String getSaupname() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getSaupname();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 인사부서코드(insadept) 리턴
	 */
	public static String getInsadept() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getInsadept();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 인사부서이름(insaname) 리턴
	 */
	public static String getInsaname() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getInsaname();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 퇴사일(retireDay) 리턴
	 */
	public static String getRetireDay() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getRetireDay();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (workType) 리턴
	 */
	public static String getWorkType() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getWorkType();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (accCode) 리턴
	 */
	public static String getAccCode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getAccCode();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (grade) 리턴
	 */
	public static String getGrade() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getGrade();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (respon) 리턴
	 */
	public static String getRespon() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getRespon();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (positCd) 리턴
	 */
	public static String getPositCd() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getPositCd();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 (posLog) 리턴
	 */
	public static String getPosLog() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getPosLog();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 권한 정보(userRoleInfo) 리턴
	 */
	public static String getRoleName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getUserRoleInfo();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}

	/**
	 * 로그인한 사용자의 캡스 일반 권한 정보(capsUserRole) 리턴
	 */
	public static String getCapsUserRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getCapsUserRole();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 로그인한 사용자의 캡스 어드민 권한 정보(capsAdminRole) 리턴
	 */
	public static String getCapsAdminRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
		CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
		if (customUserDetails != null && customUserDetails instanceof CustomUserDetails) {
			return customUserDetails.getCapsAdminRole();
		} else {
			throw new CustomException(CustomExceptionCodes.SYSTEM_ERROR);
		}
	}
	
}
