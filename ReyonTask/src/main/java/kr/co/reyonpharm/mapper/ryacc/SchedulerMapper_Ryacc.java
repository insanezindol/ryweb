package kr.co.reyonpharm.mapper.ryacc;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GwApprovalInfo;

@Repository
public interface SchedulerMapper_Ryacc {

	int updateApprovalProceedingStatusJp(GwApprovalInfo info);
	
	int updateApprovalProceedingStatusYs(GwApprovalInfo info);
	
	int updateApprovalCompeleteStatusJp(GwApprovalInfo info);
	
	int updateApprovalCompeleteStatusYs(GwApprovalInfo info);

}
