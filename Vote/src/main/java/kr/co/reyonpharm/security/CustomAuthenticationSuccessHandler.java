package kr.co.reyonpharm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		log.info("SUCCESS LOGIN");

		if (SpringSecurityUtil.isAjaxRequest(request)) {
			Cookie cookie = new Cookie("saupcode", "");
	        cookie.setMaxAge(0);
	        response.addCookie(cookie);
			
			Map<String, Object> jsonData = new HashMap<String, Object>();

			if (request.getAttribute("loginStatus") != null) {
				CustomExceptionCodes customExceptionCodes = CustomExceptionCodes.valueOf((String) request.getAttribute("loginStatus"));
				jsonData.put("resultCode", customExceptionCodes.getId());
			} else {
				jsonData.put("resultCode", 0);
			}
			CommonUtils.jsonResponse(response, jsonData);
		} else {
			super.onAuthenticationSuccess(request, response, auth);
		}
	}
}
