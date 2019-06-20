package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.PunctualityInfo;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface CommonMapper_Ryhr {

	List<CommonCodeInfo> selectCommonCodeList(CommonCodeInfo param);

	List<ConfirmInfo> selectConfirmInfoList(ConfirmInfo param);

	List<TakeOverInfo> selectTakeoverList(TakeOverInfo param);

	List<DeptInfo> selectTotalDeptList(DeptInfo param);

	int selectActiveTicketListCount(PageParam pageParam);

	List<ComingInfo> selectActiveTicketList(PageParam pageParam);

	int selectActiveVisitorListCount(PageParam pageParam);

	List<MeetingInfo> selectActiveVisitorList(PageParam pageParam);

	List<CommonCodeInfo> selectAvailableRoomList(MeetingInfo param);

	List<DateTimePickerInfo> selectTimetableList(MeetingInfo param);

	List<MeetingInfo> selectGnbTodoList(PageParam pageParam);

	List<DateTimePickerInfo> selectMainScheduleList(PageParam pageParam);

	UserInfo selectUserEmail(UserInfo param);

	int updateChangeMeetingType(MeetingInfo param);

	PunctualityInfo selectPunctualityAuth(PunctualityInfo param);

}
