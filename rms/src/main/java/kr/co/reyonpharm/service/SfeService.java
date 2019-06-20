package kr.co.reyonpharm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.rysd.SfeMapper_Rysd;
import kr.co.reyonpharm.models.SalesInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("sfeService")
public class SfeService {

	@Autowired
	private SfeMapper_Rysd sfeMapper_Rysd;

	// 부서 가져오기
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesDept(Map param) {
		sfeMapper_Rysd.selectSalesDept(param);
		return param;
	}
	
	// 사원 가져오기
	@Transactional(value = "rysd_transactionManager")
	public List<SalesInfo> getSalesUser(SalesInfo param) {
		return sfeMapper_Rysd.selectSalesUser(param);
	}
	
	// 거래처 가져오기
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesAccount(Map param) {
		sfeMapper_Rysd.selectSalesAccount(param);
		return param;
	}

	// 활동 경로 - 사원 가져오기
	@Transactional(value = "rysd_transactionManager")
	public List<SalesInfo> getSalesRouteActivityUser(SalesInfo param) {
		return sfeMapper_Rysd.selectSalesRouteActivityUser(param);
	}

	// 활동 경로 - 경로 가져오기
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesRouteActivityRoute(Map param) {
		sfeMapper_Rysd.selectSalesRouteActivityRoute(param);
		return param;
	}
	
	// 기준 좌표 변경 - 경로 가져오기
	@Transactional(value = "rysd_transactionManager")
	public List<SalesInfo> getSalesRouteChangeRoute(SalesInfo param) {
		return sfeMapper_Rysd.selectSalesRouteChangeRoute(param);
	}
	
	// 기준 좌표 변경 - 거래처 좌표 가져오기
	@Transactional(value = "rysd_transactionManager")
	public SalesInfo getSalesRouteChangePosition(SalesInfo param) {
		int sfaSalesNo = Integer.parseInt(param.getSfaSalesNo());
		if(sfaSalesNo >= 5000000) {
			return sfeMapper_Rysd.selectSalesRouteChangePosition1(param);
		} else {
			return sfeMapper_Rysd.selectSalesRouteChangePosition2(param);
		}
	}
	
	// 기준 좌표 변경 - 거래처 좌표 변경
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesRouteChangeAccount(Map param) {
		sfeMapper_Rysd.selectSalesRouteChangeAccount(param);
		return param;
	}

	// 콜매출 Trend - 리스트
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesTrendList(Map param) {
		sfeMapper_Rysd.selectSalesTrendList(param);
		return param;
	}

	// 거래처 방문 활동 - 리스트
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesAccountList(Map param) {
		sfeMapper_Rysd.selectSalesAccountList(param);
		return param;
	}

	// 지점별 방문 활동 - 리스트 가져오기
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesPointList(Map param) {
		sfeMapper_Rysd.selectSalesPointList(param);
		return param;
	}

	// 방문 매출 현황 - 리스트 가져오기
	@Transactional(value = "rysd_transactionManager")
	public Map getSalesProfitList(Map param) {
		sfeMapper_Rysd.selectSalesProfitList(param);
		return param;
	}

}
