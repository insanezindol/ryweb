package kr.co.reyonpharm.security;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kr.co.reyonpharm.service.LoginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogoutListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		WebApplicationContext wb = WebApplicationContextUtils.getWebApplicationContext(hse.getSession().getServletContext());
		LoginService loginService = (LoginService) wb.getBean("loginService");

		// 세션에서 세션 정보 확인
		HttpSession session = hse.getSession();
		String sessionId = session.getId();
		String adminId = (String) session.getAttribute("adminId");

		// Spring Security에서 권한 정보 확인 -> 사용안함, 세션 타임아웃일 경우 auth 객체를 가져오지 못함(로그아웃 엑션을 취할때는 정보를 가져옴)

		// 세션 정보를 이용하여 로그아웃 처리
		if (adminId != null && sessionId != null) {
			loginService.saveLogoutHistory(sessionId, adminId);
			log.info("[SessionTimedOutDestroyed] adminId : [" + adminId + "], sessionId : [" + sessionId + "]");
		} else {
			log.debug("[DummySessionDestroyed] adminId : [" + adminId + "], sessionId : [" + sessionId + "]");
		}
	}

}