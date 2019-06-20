package kr.co.reyonpharm.security;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogoutListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		// 세션에서 세션 정보 확인
		HttpSession session = hse.getSession();
		String sessionId = session.getId();
		String adminId = (String) session.getAttribute("adminId");

		// 세션 정보를 이용하여 로그아웃 처리
		if (adminId != null && sessionId != null) {
			log.info("[SessionTimedOutDestroyed] adminId : {}, sessionId : {}", adminId, sessionId);
		} else {
			log.debug("[DummySessionDestroyed] adminId : {}, sessionId : {}", adminId, sessionId);
		}
	}

}