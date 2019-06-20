package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;

@Repository
public interface MeetingMapper_Ryhr {

	// meeting
	int selectMeetingListCount(PageParam pageParam);

	List<MeetingInfo> selectMeetingList(PageParam pageParam);

	MeetingInfo selectMeeting(MeetingInfo param);

	int insertMeeting(MeetingInfo info);

	int updateMeeting(MeetingInfo info);

	int deleteMeeting(MeetingInfo info);

	int insertMeetingResult(MeetingInfo info);

	int updateMeetingResult(MeetingInfo info);

	int deleteMeetingResult(MeetingInfo info);

	int confirmMeetingResult(MeetingInfo info);

	int insertVisitor(MeetingInfo info);

	int updateVisitor(MeetingInfo info);

}
