package kr.co.reyonpharm.mapper.gwif;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GwApprovalInfo;
import kr.co.reyonpharm.models.GwPwdInfo;
import kr.co.reyonpharm.models.SaleInfo;

@Repository
public interface SchedulerMapper_Gwif {
	
	List<GwPwdInfo> selectPwdInfo();

	int updatePwdInfo(GwPwdInfo info);

	int deletePwdInfo(GwPwdInfo info);

	int deleteReyonSaleList(SaleInfo maxInfo);

	int insertReyonSaleInfo(SaleInfo info);

	List<GwApprovalInfo> selectApprovalProceedingList();
	
	List<GwApprovalInfo> selectApprovalCompeleteList();

	int updateApprovalCompeleteStatus(GwApprovalInfo info);

	int deleteApprovalList();
	
}
