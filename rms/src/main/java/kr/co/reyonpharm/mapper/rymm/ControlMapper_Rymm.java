package kr.co.reyonpharm.mapper.rymm;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.ControlInfo;
import kr.co.reyonpharm.models.PageParam;

@Repository
public interface ControlMapper_Rymm {
	
	List<ControlInfo> selectControlList(PageParam param);
	
	int selectControlCount(PageParam param);
	
	List<ControlInfo> selectControlMaster(ControlInfo param);
	
	List<ControlInfo> selectBOM(ControlInfo param);
	
	ControlInfo selectControl(ControlInfo param);
	
	int insertControl(ControlInfo param);
	
	int deleteControl(ControlInfo param);
	
	int updateControl(ControlInfo param);
	
	int addControlFilePath(ControlInfo param);
	
	int insertControlFilePath(ControlInfo param);
	
	int updateControlFilePath(ControlInfo param);
	
	ControlInfo selectControlFilePath(ControlInfo param);
	
	int deleteControlFilePath(ControlInfo param);
	
	ControlInfo selectControlLog(ControlInfo param);
	
	int insertControlLog(ControlInfo param);
	
	int updateControlLog(ControlInfo param);
	
	List<ControlInfo> selectEmailAddressList();
	
	ControlInfo selectEmailAddress(ControlInfo param);
}
