package kr.co.reyonpharm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.LoginMapper_Ryhr;
import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.models.UserInfo;

@Service("loginService")
public class LoginService {

	@Autowired
	private LoginMapper_Ryhr loginMapper_Ryhr;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	SecurityContextRepository repository;

	@Transactional(value = "ryhr_transactionManager")
	public CustomUserDetails getAdmin(String adminId) {
		UserInfo param = new UserInfo();
		param.setSabun(adminId);
		CustomUserDetails userInfo = loginMapper_Ryhr.selectUserInfo(param);
		return userInfo;
	}

}
