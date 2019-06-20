package kr.co.reyonpharm.mapper.rymm;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProductionInfo;

@Repository
public interface ProductionMapper_Rymm {

	List<ProductionInfo> selectContent(ProductionInfo param);
	
	boolean insertContent(ProductionInfo param);
	
	boolean deleteContent(ProductionInfo param);
	
	List<ProductionInfo> selectName(ProductionInfo param);
	
	int selectProductionListCount(PageParam param);

	List<ProductionInfo> selectProductionList(PageParam param);

	ProductionInfo selectProduction(ProductionInfo param);

	int insertProduction(ProductionInfo param);

	int updateProduction(ProductionInfo param);

	int deleteProduction(ProductionInfo param);
	
	int selectDelayCount(ProductionInfo param); //성정승인 지연 count
	
	int selectStockCount(ProductionInfo param); //재고월수 1개월 count
	
	List<ProductionInfo> selectStock(ProductionInfo param);
}
