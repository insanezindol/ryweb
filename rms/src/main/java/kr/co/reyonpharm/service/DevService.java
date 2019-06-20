package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.DevMapper_Ryhr;
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("devService")
public class DevService {

	@Autowired
	private DevMapper_Ryhr devMapper_Ryhr;

	// 발송 이력 가져오기
	@Transactional(value = "ryhr_transactionManager")
	public List<AlarmInfo> getAlarmHistoryList() {
		return devMapper_Ryhr.selectAlarmHistoryList();
	}

	// 푸시 알람 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<UserInfo> getAlarmUserList() {
		return devMapper_Ryhr.selectAlarmUserList();
	}

	// 발송 예약 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addAlarm(AlarmInfo param) {
		return devMapper_Ryhr.insertAlarm(param);
	}

	// 발송 이력 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteAlarm(AlarmInfo param) {
		return devMapper_Ryhr.deleteAlarm(param);
	}

}
