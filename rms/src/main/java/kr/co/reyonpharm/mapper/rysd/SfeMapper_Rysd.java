package kr.co.reyonpharm.mapper.rysd;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.SalesInfo;

@Repository
public interface SfeMapper_Rysd {

	void selectSalesDept(Map param);
	
	List<SalesInfo> selectSalesUser(SalesInfo param);
	
	void selectSalesAccount(Map param);

	List<SalesInfo> selectSalesRouteActivityUser(SalesInfo param);

	void selectSalesRouteActivityRoute(Map param);

	List<SalesInfo> selectSalesRouteChangeRoute(SalesInfo param);
	
	SalesInfo selectSalesRouteChangePosition1(SalesInfo param);
	
	SalesInfo selectSalesRouteChangePosition2(SalesInfo param);
	
	void selectSalesRouteChangeAccount(Map param);
	
	void selectSalesTrendList(Map param);

	void selectSalesAccountList(Map param);

	void selectSalesPointList(Map param);

	void selectSalesProfitList(Map param);
	
}
