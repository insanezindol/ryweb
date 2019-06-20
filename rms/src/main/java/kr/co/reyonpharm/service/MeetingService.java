package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.MeetingMapper_Ryhr;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("meetingService")
public class MeetingService {

	@Autowired
	private MeetingMapper_Ryhr meetingMapper_Ryhr;

	// 회의, 방문자 리스트 전체 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getMeetingListCount(PageParam pageParam) {
		return meetingMapper_Ryhr.selectMeetingListCount(pageParam);
	}

	// 회의, 방문자 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getMeetingList(PageParam pageParam) {
		return meetingMapper_Ryhr.selectMeetingList(pageParam);
	}

	// 회의 정보 조회
	@Transactional(value = "ryhr_transactionManager")
	public MeetingInfo getMeeting(MeetingInfo param) {
		return meetingMapper_Ryhr.selectMeeting(param);
	}

	// 회의 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addMeeting(MeetingInfo info) {
		return meetingMapper_Ryhr.insertMeeting(info);
	}

	// 회의 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyMeeting(MeetingInfo info) {
		return meetingMapper_Ryhr.updateMeeting(info);
	}

	// 회의 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteMeeting(MeetingInfo info) {
		return meetingMapper_Ryhr.deleteMeeting(info);
	}

	// 회의 결과 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addMeetingResult(MeetingInfo info) {
		return meetingMapper_Ryhr.insertMeetingResult(info);
	}

	// 회의 결과 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyMeetingResult(MeetingInfo info) {
		return meetingMapper_Ryhr.updateMeetingResult(info);
	}

	// 회의 결과 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteMeetingResult(MeetingInfo info) {
		return meetingMapper_Ryhr.deleteMeetingResult(info);
	}

	// 회의 결과 확인
	@Transactional(value = "ryhr_transactionManager")
	public int confirmMeetingResult(MeetingInfo info) {
		return meetingMapper_Ryhr.confirmMeetingResult(info);
	}

	// 방문자 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addVisitor(MeetingInfo info) {
		return meetingMapper_Ryhr.insertVisitor(info);
	}

	// 방문자 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyVisitor(MeetingInfo info) {
		return meetingMapper_Ryhr.updateVisitor(info);
	}

}
