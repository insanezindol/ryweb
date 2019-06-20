package kr.co.reyonpharm.mapper.ryhr;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.NtsInfo;

@Repository
public interface NtsMapper_Ryhr {

	NtsInfo selectUserInfo(NtsInfo param);
	
	NtsInfo selectExistInfo(NtsInfo existParam);
	
	int deleteExistInfo(NtsInfo existParam);
	
	int insertNtsInfo(NtsInfo param);

}
