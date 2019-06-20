package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.ContractInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.GwApprovalInfo;
import kr.co.reyonpharm.models.GwPwdInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PhoneInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.models.VehicleInfo;

@Repository
public interface SchedulerMapper_Ryhr {
	
	List<UserInfo> selectTotalUserList(UserInfo param);
	
	List<DeptInfo> selectTotalDeptList(DeptInfo param);
	
	List<MeetingInfo> selectMeetingResultStatusList();
	
	int updateMeetingResultStatusToResult(MeetingInfo param);
	
	int updateMeetingResultStatusToComplete();
	
	List<MeetingInfo> selectIncompleteList();
	
	List<ContractInfo> selectContractForSendMail();
	
	List<ContractInfo> selectContractUpdateStatusForSendMail();
	
	List<VehicleInfo> selectVehicleForSendMail();
	
	List<VehicleInfo> selectVehicleUpdateStatusForSendMail();
	
	List<ConfirmInfo> selectConfirmInfoList(ConfirmInfo param);
	
	UserInfo selectUserEmail(UserInfo param);
	
	int deleteHrHistoryPwd(GwPwdInfo info);

	int insertHrHistoryPwd(GwPwdInfo info);
	
	int updateHrPwd(GwPwdInfo info);
	
	List<AlarmInfo> selectSendAlarmList();
	
	int updateAlarm(AlarmInfo param);

	List<GroupwareExtInfo> selectGroupwareExtIngList();
	
	List<GroupwareExtInfo> selectGroupwareExtEndList();

	int updateGroupwareExt(GroupwareExtInfo param);

	List<PhoneInfo> selectPhonenumberList(PhoneInfo param);

	int updateApprovalProceedingStatusHoliday(GwApprovalInfo info);

	int updateApprovalCompeleteStatusHoliday(GwApprovalInfo info);

	int updateApprovalProceedingStatusOvertime(GwApprovalInfo info);

	int updateApprovalCompeleteStatusOvertime(GwApprovalInfo info);
	
}
