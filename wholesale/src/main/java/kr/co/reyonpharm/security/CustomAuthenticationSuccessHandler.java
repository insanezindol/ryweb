package kr.co.reyonpharm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.service.LoginService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired 
    LoginService loginService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		log.info("SUCCESS LOGIN");
		
		/*********************************
         * 로그인 이력 저장
         *********************************/
    	HttpSession session = (HttpSession)request.getSession(false);
        String sessionId = session.getId();
        String ip = CommonUtils.getClientIp(request);
        CustomUserDetails customUserDetails = SpringSecurityUtil.getCustomUserDetails();
        
        // 로그인 이력 저장
        if (null != customUserDetails) {
        	loginService.saveLoginHistory(sessionId, customUserDetails.getUsername(), ip);
        }
		
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
