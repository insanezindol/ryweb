package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryxrs.WholesaleMapper_Ryxrs;
import kr.co.reyonpharm.models.LoginHistoryInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("adminService")
public class AdminService {

	@Autowired
	private WholesaleMapper_Ryxrs wholesaleMapper_Ryxrs;

	// 사용자 관리 리스트 카운트
	@Transactional(value = "ryxrs_transactionManager")
	public int getUserInfoListCount(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectUserInfoListCount(pageParam);
	}

	// 사용자 관리 리스트
	@Transactional(value = "ryxrs_transactionManager")
	public List<UserInfo> getUserInfoList(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectUserInfoList(pageParam);
	}

	// 사업자 번호 존재 여부 체크
	@Transactional(value = "ryxrs_transactionManager")
	public int getUsernameCheckCount(UserInfo info) {
		return wholesaleMapper_Ryxrs.selectUsernameCheckCount(info);
	}
	
	// 사용자 조회
	@Transactional(value = "ryxrs_transactionManager")
	public UserInfo getUserInfo(UserInfo info) {
		return wholesaleMapper_Ryxrs.selectUserInfo(info);
	}

	// 사용자 관리 추가
	@Transactional(value = "ryxrs_transactionManager")
	public int addUserInfo(UserInfo info) {
		return wholesaleMapper_Ryxrs.insertUserInfo(info);
	}

	// 사용자 수정
	@Transactional(value = "ryxrs_transactionManager")
	public int modifyUserInfo(UserInfo info) {
		return wholesaleMapper_Ryxrs.updateUserInfo(info);
	}
	
	// 사용자 삭제
	@Transactional(value = "ryxrs_transactionManager")
	public int deleteUserInfo(UserInfo info) {
		return wholesaleMapper_Ryxrs.deleteUserInfo(info);
	}

	// 사용자 비밀번호 변경
	@Transactional(value = "ryxrs_transactionManager")
	public int modifyUserPwd(UserInfo info) {
		return wholesaleMapper_Ryxrs.updateUserPwd(info);
	}

	// 접속 로그 관리 리스트 카운트
	@Transactional(value = "ryxrs_transactionManager")
	public int getLoginLogListCount(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectLoginLogListCount(pageParam);
	}
	
	// 접속 로그 관리 리스트
	@Transactional(value = "ryxrs_transactionManager")
	public List<LoginHistoryInfo> getLoginLogList(PageParam pageParam) {
		return wholesaleMapper_Ryxrs.selectLoginLogList(pageParam);
	}

}
