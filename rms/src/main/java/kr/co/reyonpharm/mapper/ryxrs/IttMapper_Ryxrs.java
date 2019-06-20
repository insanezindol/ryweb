package kr.co.reyonpharm.mapper.ryxrs;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.BtsInfo;

@Repository
public interface IttMapper_Ryxrs {

	List<BtsInfo> selectMigrationInfoCnt(BtsInfo param);

	List<BtsInfo> selectBtsInfo();

	int insertBtsInfo(BtsInfo info);

	int deleteBtsInfo(BtsInfo param);

}
