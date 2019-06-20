package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.rymm.ProductionMapper_Rymm;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProductionInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("productionService")
public class ProductionService {

	@Autowired
	private ProductionMapper_Rymm productionMapper_Rymm;
	
	@Transactional(value = "rymm_transactionManager")
	public List<ProductionInfo> getContent(ProductionInfo param) {
		return productionMapper_Rymm.selectContent(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public boolean addContent(ProductionInfo param) {
		return productionMapper_Rymm.insertContent(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public boolean delContent(ProductionInfo param) {
		return productionMapper_Rymm.deleteContent(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public List<ProductionInfo> getName(ProductionInfo param) {
		return productionMapper_Rymm.selectName(param);
	}

	@Transactional(value = "rymm_transactionManager")
	public int getProductionListCount(PageParam param) {
		return productionMapper_Rymm.selectProductionListCount(param);
	}

	@Transactional(value = "rymm_transactionManager")
	public List<ProductionInfo> getProductionList(PageParam param) {
		return productionMapper_Rymm.selectProductionList(param);
	}

	@Transactional(value = "rymm_transactionManager")
	public ProductionInfo getProduction(ProductionInfo param) {
		return productionMapper_Rymm.selectProduction(param);
	}

	@Transactional(value = "rymm_transactionManager")
	public int addProduction(ProductionInfo param) {
		return productionMapper_Rymm.insertProduction(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int modifyProduction(ProductionInfo param) {
		return productionMapper_Rymm.updateProduction(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int deleteProduction(ProductionInfo param) {
		return productionMapper_Rymm.deleteProduction(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int getDelayCount(ProductionInfo param) {
		return productionMapper_Rymm.selectDelayCount(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int getStockCount(ProductionInfo param) {
		return productionMapper_Rymm.selectStockCount(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public List<ProductionInfo> getStock(ProductionInfo param) {
		return productionMapper_Rymm.selectStock(param);
	}

}
