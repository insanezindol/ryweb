package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.ScheduleMapper_Ryhr;
import kr.co.reyonpharm.models.ScheduleInfo;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("scheduleService")
public class ScheduleService {

	@Autowired
	private ScheduleMapper_Ryhr scheduleMapper_Ryhr;

	@Transactional(value = "ryhr_transactionManager")
	public int getScheduleListCount(PageParam param) {
		return scheduleMapper_Ryhr.selectScheduleListCount(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public List<ScheduleInfo> getScheduleList(PageParam param) {
		return scheduleMapper_Ryhr.selectScheduleList(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public ScheduleInfo getSchedule(ScheduleInfo param) {
		return scheduleMapper_Ryhr.selectSchedule(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int addSchedule(ScheduleInfo param) {
		return scheduleMapper_Ryhr.insertSchedule(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public int modifySchedule(ScheduleInfo param) {
		return scheduleMapper_Ryhr.updateSchedule(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int deleteSchedule(ScheduleInfo param) {
		return scheduleMapper_Ryhr.deleteSchedule(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public List<DateTimePickerInfo> getScheduleTableList(PageParam param) {
		return scheduleMapper_Ryhr.selectScheduleTableList(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public List<DateTimePickerInfo> getScheduleTimeTableList(PageParam param) {
		return scheduleMapper_Ryhr.selectScheduleTimeTableList(param);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public List<AttendantInfo> getAttendantList(AttendantInfo info) {
		return scheduleMapper_Ryhr.selectAttendantList(info);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public int addAttendant(AttendantInfo info) {
		return scheduleMapper_Ryhr.insertAttendant(info);
	}
	
	@Transactional(value = "ryhr_transactionManager")
	public int deleteAttendant(AttendantInfo info) {
		return scheduleMapper_Ryhr.deleteAttendant(info);
	}
	
}
