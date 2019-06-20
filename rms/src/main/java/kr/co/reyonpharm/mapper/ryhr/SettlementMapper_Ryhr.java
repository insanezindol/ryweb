package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.NtsInfo;
import kr.co.reyonpharm.models.SettlementInfo;

@Repository
public interface SettlementMapper_Ryhr {

	// settlement
	List<SettlementInfo> selectDeclarationDBList(SettlementInfo param);
	
	List<SettlementInfo> selectExcelMasterData(SettlementInfo param);
	
	List<SettlementInfo> selectExcelMasterExData(SettlementInfo param);
	
	NtsInfo selectUserInfo(NtsInfo param);
	
	NtsInfo selectExistInfo(NtsInfo existParam);
	
	int deleteExistInfo(NtsInfo existParam);
	
	int insertNtsInfo(NtsInfo param);

	List<SettlementInfo> selectSpecificationDBList(SettlementInfo param);

	int insertNtsResultInfo(NtsInfo param);

}
