package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface DevMapper_Ryhr {

	List<AlarmInfo> selectAlarmHistoryList();

	List<UserInfo> selectAlarmUserList();

	int insertAlarm(AlarmInfo param);

	int deleteAlarm(AlarmInfo param);

}
