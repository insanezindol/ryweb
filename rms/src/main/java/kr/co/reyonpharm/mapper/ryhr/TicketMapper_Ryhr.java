package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TicketInfo;

@Repository
public interface TicketMapper_Ryhr {

	// ticket
	int selectTicketListCount(PageParam pageParam);

	List<TicketInfo> selectTicketList(PageParam pageParam);

	TicketInfo selectTicket(TicketInfo param);

	int insertTicket(TicketInfo info);

	int updateTicket(TicketInfo info);

	int deleteTicket(TicketInfo info);

	int confirmTicket(TicketInfo info);

}
