package kr.co.reyonpharm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryxrs.WholesaleMapper_Ryxrs;
import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.models.LoginHistoryInfo;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("loginService")
public class LoginService {

	@Autowired
	private WholesaleMapper_Ryxrs wholesaleMapper_Ryxrs;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	SecurityContextRepository repository;

	@Transactional(value = "ryxrs_transactionManager")
	public CustomUserDetails getAdmin(String adminId) {
		UserInfo param = new UserInfo();
		param.setUsername(adminId);
		CustomUserDetails userInfo = wholesaleMapper_Ryxrs.loginUserInfo(param);
		return userInfo;
	}

	@Transactional(value = "ryxrs_transactionManager")
	public void saveLoginHistory(String sessionId, String username, String ip) {
		LoginHistoryInfo param = new LoginHistoryInfo();
		param.setSessionId(sessionId);
		param.setUsername(username);
		param.setIp(ip);
		int affectedCount = wholesaleMapper_Ryxrs.insertLoginHistory(param);
		log.info("affectedCount : " + affectedCount);
	}
	
	@Transactional(value = "ryxrs_transactionManager")
	public void saveLogoutHistory(String sessionId, String username) {
		LoginHistoryInfo param = new LoginHistoryInfo();
		param.setSessionId(sessionId);
		param.setUsername(username);
		int affectedCount = wholesaleMapper_Ryxrs.updateLoginHistory(param);
		log.info("affectedCount : " + affectedCount);
	}

}
