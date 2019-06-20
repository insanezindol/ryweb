package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.mapper.ryhr.AppMapper_Ryhr;
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.TokenInfo;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("appService")
public class AppService {

	@Autowired
	private AppMapper_Ryhr appMapper_Ryhr;

	// 로그인 유저 정보 가져오기
	public UserInfo getUserInfo(UserInfo param) {
		return appMapper_Ryhr.selectUserInfo(param);
	}

	// 로그인 토큰 정보 가져오기
	public TokenInfo getTokenInfo(TokenInfo param) {
		return appMapper_Ryhr.selectTokenInfo(param);
	}

	// 로그인 토큰 정보 생성
	public int addTokenInfo(TokenInfo param) {
		return appMapper_Ryhr.insertTokenInfo(param);
	}

	// 로그인 토큰 정보 수정
	public int modifyTokenInfo(TokenInfo param) {
		return appMapper_Ryhr.modifyTokenInfo(param);
	}
	
	// 푸시 메시지 수신 타입 변경
	public int modifyTokenInfoMsgType(TokenInfo param) {
		return appMapper_Ryhr.modifyTokenInfoMsgType(param);
	}

	// 알람 정보 리스트 조회
	public List<AlarmInfo> getAlarmList(AlarmInfo param) {
		return appMapper_Ryhr.selectAlarmList(param);
	}

}
