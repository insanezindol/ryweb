package kr.co.reyonpharm.mapper.mssql;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.BtsInfo;

@Repository
public interface IttMapper_Mssql {

	List<BtsInfo> selectBtsInfoListCnt(BtsInfo param);

	List<BtsInfo> selectBtsInfoList(BtsInfo param);

}
