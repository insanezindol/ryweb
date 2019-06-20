package kr.co.reyonpharm.mapper.rysd;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.SaleInfo;

@Repository
public interface SchedulerMapper_Rysd {

	List<SaleInfo> selectReyonSaleList();

}
