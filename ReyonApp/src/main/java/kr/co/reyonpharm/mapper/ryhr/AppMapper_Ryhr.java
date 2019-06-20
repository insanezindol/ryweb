package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.TokenInfo;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface AppMapper_Ryhr {

	UserInfo selectUserInfo(UserInfo param);

	TokenInfo selectTokenInfo(TokenInfo param);

	int insertTokenInfo(TokenInfo param);

	int modifyTokenInfo(TokenInfo param);

	int modifyTokenInfoMsgType(TokenInfo param);

	List<AlarmInfo> selectAlarmList(AlarmInfo param);

}
