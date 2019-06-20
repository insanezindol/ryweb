package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.rymm.ControlMapper_Rymm;
import kr.co.reyonpharm.models.ControlInfo;
import kr.co.reyonpharm.models.PageParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("controlService")
public class ControlService {

	@Autowired
	private ControlMapper_Rymm controlMapper_Rymm;
	
	
	@Transactional(value = "rymm_transactionManager")
	public List<ControlInfo> getControlList(PageParam param) {
		return controlMapper_Rymm.selectControlList(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int getControlListCount(PageParam param) {
		return controlMapper_Rymm.selectControlCount(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public List<ControlInfo> getControlMaster(ControlInfo param) {
		return controlMapper_Rymm.selectControlMaster(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public List<ControlInfo> getBom(ControlInfo param) {
		return controlMapper_Rymm.selectBOM(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public ControlInfo getControl(ControlInfo param) {
		return controlMapper_Rymm.selectControl(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int setControl(ControlInfo param) {
		return controlMapper_Rymm.insertControl(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int delControl(ControlInfo param) {
		return controlMapper_Rymm.deleteControl(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int modifyControl(ControlInfo param) {
		return controlMapper_Rymm.updateControl(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int addControlFilePath(ControlInfo param) {
		return controlMapper_Rymm.addControlFilePath(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int insertControlFilePath(ControlInfo param) {
		return controlMapper_Rymm.insertControlFilePath(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public ControlInfo getControlFilePath(ControlInfo param) {
		return controlMapper_Rymm.selectControlFilePath(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int delControlFilePath(ControlInfo param) {
		return controlMapper_Rymm.deleteControlFilePath(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public ControlInfo getControlLog(ControlInfo param) {
		return controlMapper_Rymm.selectControlLog(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int addControlLog(ControlInfo param) {
		return controlMapper_Rymm.insertControlLog(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int modifyControlLog(ControlInfo param) {
		return controlMapper_Rymm.updateControlLog(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public List<ControlInfo> getEmailAddressList() {
		return controlMapper_Rymm.selectEmailAddressList();
	}
	
	@Transactional(value = "rymm_transactionManager")
	public ControlInfo getEmailAddress(ControlInfo param) {
		return controlMapper_Rymm.selectEmailAddress(param);
	}
	
	@Transactional(value = "rymm_transactionManager")
	public int modifyControlFilePath(ControlInfo param) {
		return controlMapper_Rymm.updateControlFilePath(param);
	}
}
