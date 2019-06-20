package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ScheduleInfo;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.PageParam;

@Repository
public interface ScheduleMapper_Ryhr {

	int selectScheduleListCount(PageParam pageParam);

	List<ScheduleInfo> selectScheduleList(PageParam pageParam);

	ScheduleInfo selectSchedule(ScheduleInfo param);

	int insertSchedule(ScheduleInfo param);
	
	int updateSchedule(ScheduleInfo param);

	int deleteSchedule(ScheduleInfo param);

	List<DateTimePickerInfo> selectScheduleTableList(PageParam param);

	List<DateTimePickerInfo> selectScheduleTimeTableList(PageParam param);
	
	List<AttendantInfo> selectAttendantList(AttendantInfo info);
	
	int insertAttendant(AttendantInfo info);
	
	int deleteAttendant(AttendantInfo info);
	
}
