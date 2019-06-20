package kr.co.reyonpharm.mapper.gw;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.GwMailInfo;
import kr.co.reyonpharm.models.GwPwdInfo;
import kr.co.reyonpharm.models.PhoneInfo;

@Repository
public interface SchedulerMapper_Gw {
	
	int insertGwMailInfo(GwMailInfo param);

	int updateGwPwd(GwPwdInfo info);

	int updateGwExtStatus(GroupwareExtInfo info);

	int updatePhonenumberInfo(PhoneInfo info);
	
}
