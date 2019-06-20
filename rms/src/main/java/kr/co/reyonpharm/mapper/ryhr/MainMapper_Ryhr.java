package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;

@Repository
public interface MainMapper_Ryhr {

	List<DateTimePickerInfo> selectScheduleList(MeetingInfo param);

	int selectMainMeetingListCount(PageParam pageParam);

	List<MeetingInfo> selectMainMeetingList(PageParam pageParam);

	List<MeetingInfo> selectTotalTodoList(PageParam pageParam);

	int selectTodoListCount(PageParam pageParam);

	List<MeetingInfo> selectTodoList(PageParam pageParam);

	List<TakeOverInfo> selectTakingoverList(TakeOverInfo param);

	int insertTakeOver(TakeOverInfo param);

	int updateTakeOver(TakeOverInfo param);

	List<AttendantInfo> selectAttendantList(AttendantInfo param);

	int insertAttendant(AttendantInfo info);

	int deleteAttendant(AttendantInfo info);

}
