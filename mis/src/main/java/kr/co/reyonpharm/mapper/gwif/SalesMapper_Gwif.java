package kr.co.reyonpharm.mapper.gwif;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.SaleInfo;

@Repository
public interface SalesMapper_Gwif {

	List<SaleInfo> selectSalesPerformanceInfoDateList();
	
	List<SaleInfo> selectSalesPerformanceInfoList(SaleInfo param);

}
