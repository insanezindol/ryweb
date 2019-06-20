package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.MainMapper_Ryhr;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mainService")
public class MainService {

	@Autowired
	private MainMapper_Ryhr mainMapper_Ryhr;

	// 조건에 해당하는 스케쥴 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<DateTimePickerInfo> getScheduleList(MeetingInfo param) {
		return mainMapper_Ryhr.selectScheduleList(param);
	}

	// 메인화면 회의,방문건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getMainMeetingListCount(PageParam pageParam) {
		return mainMapper_Ryhr.selectMainMeetingListCount(pageParam);
	}

	// 메인화면 회의,방문 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getMainMeetingList(PageParam pageParam) {
		return mainMapper_Ryhr.selectMainMeetingList(pageParam);
	}

	// 전체 진행 대기 내역 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getTotalTodoList(PageParam pageParam) {
		return mainMapper_Ryhr.selectTotalTodoList(pageParam);
	}

	// 진행 대기 내역 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getTodoListCount(PageParam pageParam) {
		return mainMapper_Ryhr.selectTodoListCount(pageParam);
	}

	// 진행 대기 내역 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getTodoList(PageParam pageParam) {
		return mainMapper_Ryhr.selectTodoList(pageParam);
	}

	// 인계 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<TakeOverInfo> getTakingoverList(TakeOverInfo param) {
		return mainMapper_Ryhr.selectTakingoverList(param);
	}

	// 인수인계 정보 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addtakeOver(TakeOverInfo param) {
		return mainMapper_Ryhr.insertTakeOver(param);
	}

	// 인수인계 정보 상태 업데이트
	@Transactional(value = "ryhr_transactionManager")
	public int modifyTakeOver(TakeOverInfo param) {
		return mainMapper_Ryhr.updateTakeOver(param);
	}

	// 참석자,참고인 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<AttendantInfo> getAttendantList(AttendantInfo param) {
		return mainMapper_Ryhr.selectAttendantList(param);
	}

	// 참석자,참고인 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addAttendant(AttendantInfo info) {
		return mainMapper_Ryhr.insertAttendant(info);
	}

	// 참석자,참고인 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteAttendant(AttendantInfo info) {
		return mainMapper_Ryhr.deleteAttendant(info);
	}

}
