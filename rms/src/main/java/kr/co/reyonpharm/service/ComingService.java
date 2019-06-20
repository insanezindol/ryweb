package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.ComingMapper_Ryhr;
import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TicketInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("comingService")
public class ComingService {

	@Autowired
	private ComingMapper_Ryhr comingMapper_Ryhr;

	// 출입자 리스트 전체 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getComingListCount(PageParam pageParam) {
		return comingMapper_Ryhr.selectComingListCount(pageParam);
	}

	// 출입자 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<ComingInfo> getComingList(PageParam pageParam) {
		return comingMapper_Ryhr.selectComingList(pageParam);
	}

	// 출입자 정보 조회
	@Transactional(value = "ryhr_transactionManager")
	public ComingInfo getComing(ComingInfo param) {
		return comingMapper_Ryhr.selectComing(param);
	}

	// 출입자 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addComing(ComingInfo info) {
		return comingMapper_Ryhr.insertComing(info);
	}

	// 출입자 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyComing(ComingInfo info) {
		return comingMapper_Ryhr.updateComing(info);
	}

	// 출입자 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteComing(ComingInfo info) {
		return comingMapper_Ryhr.deleteComing(info);
	}

	// 부서별 주차권 지급 현황
	@Transactional(value = "ryhr_transactionManager")
	public List<TicketInfo> getStatisticsByDept(PageParam pageParam) {
		return comingMapper_Ryhr.selectStatisticsByDept(pageParam);
	}

	// 주차권 지급 상세 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<TicketInfo> getStatisticsByTotal(PageParam pageParam) {
		return comingMapper_Ryhr.selectStatisticsByTotal(pageParam);
	}

	// 출입자 상세 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<ComingInfo> getStatisticsByVisitor(PageParam pageParam) {
		return comingMapper_Ryhr.selectStatisticsByVisitor(pageParam);
	}

	// 주차권 지급 웹할인 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<TicketInfo> getStatisticsByWebSale(PageParam pageParam) {
		return comingMapper_Ryhr.selectStatisticsByWebSale(pageParam);
	}

}
