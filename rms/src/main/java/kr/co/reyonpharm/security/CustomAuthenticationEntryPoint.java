package kr.co.reyonpharm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		/*
		 * 세션이 만료된 경우
		 */
		log.debug("commence start");
		if (SpringSecurityUtil.isAjaxRequest(request)) {
			Map<String, Object> jsonData = new HashMap<String, Object>();

			// 비 로그인 상태로 Ajax요청을 한 경우는 세션이 만료된 경우로 간주하고 SESSION_EXPIRE를 내려준다.
			jsonData.put("resultCode", CustomExceptionCodes.SESSION_EXPIRE.getId());
			jsonData.put("resultMsg", CustomExceptionCodes.SESSION_EXPIRE.getMsg());
			CommonUtils.jsonResponse(response, jsonData);

		} else {
			log.debug("getRequestURI:" + request.getRequestURI());
			// 팝업창에서 세션 만료 상태가 되면 세션 만료 페이지로 이동 시킨다. (팝업창을 닫기 위해 코드 분기)
			if (request.getRequestURI().indexOf("Popup") > -1) {
				request.setAttribute("resultCode", CustomExceptionCodes.SESSION_EXPIRE_FOR_POPUP.getId());
				request.setAttribute("resultMsg", CustomExceptionCodes.SESSION_EXPIRE_FOR_POPUP.getMsg());
				request.getRequestDispatcher("/error403.do").forward(request, response);
			} else {
				request.getRequestDispatcher("/login.do").forward(request, response);
			}
		}
		log.debug("commence end");
	}

}
