package kr.co.reyonpharm.mapper.ryxrs;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.CustomUserDetails;
import kr.co.reyonpharm.models.LoginHistoryInfo;
import kr.co.reyonpharm.models.NoticeInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface WholesaleMapper_Ryxrs {

	CustomUserDetails loginUserInfo(UserInfo param);
	
	int insertLoginHistory(LoginHistoryInfo param);

	int updateLoginHistory(LoginHistoryInfo param);

	int selectUserInfoListCount(PageParam pageParam);

	List<UserInfo> selectUserInfoList(PageParam pageParam);

	int selectUsernameCheckCount(UserInfo info);

	UserInfo selectUserInfo(UserInfo info);
	
	int insertUserInfo(UserInfo info);

	int updateUserInfo(UserInfo info);
	
	int deleteUserInfo(UserInfo info);

	int selectNoticeInfoListCount(PageParam pageParam);

	List<NoticeInfo> selectNoticeInfoList(PageParam pageParam);

	NoticeInfo selectNoticeInfo(NoticeInfo info);

	int insertNoticeInfo(NoticeInfo info);

	int updateNoticeInfoWithFile(NoticeInfo info);
	
	int updateNoticeInfo(NoticeInfo info);

	int deleteNoticeInfo(NoticeInfo info);

	int updateUserPwd(UserInfo info);

	int selectLoginLogListCount(PageParam pageParam);

	List<LoginHistoryInfo> selectLoginLogList(PageParam pageParam);

}
