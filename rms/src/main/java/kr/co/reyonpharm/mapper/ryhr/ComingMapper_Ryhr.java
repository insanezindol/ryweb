package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TicketInfo;

@Repository
public interface ComingMapper_Ryhr {

	// coming
	int selectComingListCount(PageParam pageParam);

	List<ComingInfo> selectComingList(PageParam pageParam);

	ComingInfo selectComing(ComingInfo info);

	int insertComing(ComingInfo info);

	int updateComing(ComingInfo info);

	int deleteComing(ComingInfo info);

	List<TicketInfo> selectStatisticsByDept(PageParam pageParam);

	List<TicketInfo> selectStatisticsByTotal(PageParam pageParam);

	List<ComingInfo> selectStatisticsByVisitor(PageParam pageParam);

	List<TicketInfo> selectStatisticsByWebSale(PageParam pageParam);

}
