package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.TicketMapper_Ryhr;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TicketInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ticketService")
public class TicketService {

	@Autowired
	private TicketMapper_Ryhr ticketMapper_Ryhr;

	// 주차권 리스트 전체 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getTicketListCount(PageParam pageParam) {
		return ticketMapper_Ryhr.selectTicketListCount(pageParam);
	}

	// 주차권 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<TicketInfo> getTicketList(PageParam pageParam) {
		return ticketMapper_Ryhr.selectTicketList(pageParam);
	}

	// 주차권 정보 조회
	@Transactional(value = "ryhr_transactionManager")
	public TicketInfo getTicket(TicketInfo param) {
		return ticketMapper_Ryhr.selectTicket(param);
	}

	// 주차권 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addTicket(TicketInfo info) {
		return ticketMapper_Ryhr.insertTicket(info);
	}

	// 주차권 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyTicket(TicketInfo info) {
		return ticketMapper_Ryhr.updateTicket(info);
	}

	// 주차권 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deleteTicket(TicketInfo info) {
		return ticketMapper_Ryhr.deleteTicket(info);
	}

	// 주차권 확인
	@Transactional(value = "ryhr_transactionManager")
	public int confirmTicket(TicketInfo info) {
		return ticketMapper_Ryhr.confirmTicket(info);
	}

}
