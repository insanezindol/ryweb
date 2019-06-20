package kr.co.reyonpharm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.mapper.ryhr.GWMapper_Ryhr;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("gWService")
public class GWService {

	@Autowired
	private GWMapper_Ryhr gWMapper_Ryhr;

	// 직원 정보 조회
	public UserInfo getUserInfo(UserInfo param) {
		return gWMapper_Ryhr.selectUserInfo(param);
	}

}
