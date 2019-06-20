package kr.co.reyonpharm.mapper.ryxrs;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.BtsInfo;
import kr.co.reyonpharm.models.GwMailInfo;
import kr.co.reyonpharm.models.GwPwdInfo;

@Repository
public interface SchedulerMapper_Ryxrs {

	List<GwMailInfo> selectGwMailInfoList();
	
	int updateGwMailInfo(GwMailInfo info);
	
	int deleteGwMailInfo();
	
	List<GwPwdInfo> selectPwdInfo();

	int updatePwdInfo(GwPwdInfo info);

	int deletePwdInfo(GwPwdInfo info);
	
	int insertBtsInfo(BtsInfo info);
	
	int deleteBtsInfo(BtsInfo param);

}
