package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.mapper.gwif.SalesMapper_Gwif;
import kr.co.reyonpharm.models.SaleInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("salesService")
public class SalesService {

	@Autowired
	private SalesMapper_Gwif salesMapper_Gwif;
	
	public List<SaleInfo> getSalesPerformanceInfoDateList() {
		return salesMapper_Gwif.selectSalesPerformanceInfoDateList();
	}

	public List<SaleInfo> getSalesPerformanceInfoList(SaleInfo param) {
		return salesMapper_Gwif.selectSalesPerformanceInfoList(param);
	}

}
