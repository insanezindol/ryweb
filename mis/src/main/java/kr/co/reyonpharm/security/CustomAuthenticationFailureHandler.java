package kr.co.reyonpharm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		log.info("FAILURE LOGIN");

		if (SpringSecurityUtil.isAjaxRequest(request) && request.getAttribute("loginStatus") != null) {
			Map<String, Object> jsonData = new HashMap<String, Object>();

			CustomExceptionCodes customExceptionCodes = CustomExceptionCodes.valueOf((String) request.getAttribute("loginStatus"));
			jsonData.put("resultCode", customExceptionCodes.getId());
			jsonData.put("resultMsg", customExceptionCodes.getMsg());

			CommonUtils.jsonResponse(response, jsonData);
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}
