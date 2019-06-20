package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.PcMessageInfo;
import kr.co.reyonpharm.models.PcOffInfo;
import kr.co.reyonpharm.models.PcProgramInfo;
import kr.co.reyonpharm.models.PcReportInfo;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface AppMapper_Ryhr {

	// application
	PcReportInfo selectPcReportInfoList(PcReportInfo param);

	int insertPcReportInfo(PcReportInfo param);

	int updatePcReportInfo(PcReportInfo param);

	List<PcMessageInfo> selectPcMessageInfoList(PcMessageInfo param);

	int updatePcMessageInfo(PcMessageInfo param);

	List<PcOffInfo> selectPcOffInfo(PcOffInfo param);

	UserInfo selectUserInfo(UserInfo param);

	PcProgramInfo selectPcProgramInfo(PcProgramInfo param);

	// management system admin menu
	int selectPcReportInfoListCount(PageParam param);

	List<PcReportInfo> selectPcReportInfoListByRms(PageParam param);

	int selectPcMessageInfoListCount(PageParam param);

	List<PcMessageInfo> selectPcMessageInfoListByRms(PageParam param);

	int insertPcMessageInfo(PcMessageInfo param);

	int deletePcMessageInfo(PcMessageInfo param);

	int selectPcProgramInfoListCount(PageParam param);

	List<PcProgramInfo> selectPcProgramInfoListByRms(PageParam param);

	PcProgramInfo selectPcProgramInfoByRms(PcProgramInfo param);

	int insertPcProgramInfo(PcProgramInfo param);

	int updatePcProgramInfo(PcProgramInfo param);

	List<PcProgramInfo> selectPcProgramInfoConfirmList(PcProgramInfo param);

	int confirmPcProgramInfo(PcProgramInfo param);

	int deletePcProgramInfo(PcProgramInfo param);

}
