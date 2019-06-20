package kr.co.reyonpharm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.CommonMapper_Ryhr;
import kr.co.reyonpharm.mapper.ryxrs.PunctualityMapper_Ryxrs;
import kr.co.reyonpharm.models.PunctualityInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("punctualityService")
public class PunctualityService {

	@Autowired
	private PunctualityMapper_Ryxrs punctualityMapper_Ryxrs;
	
	@Autowired
	private CommonMapper_Ryhr commonMapper_Ryhr;

	// 근태 정보 조회
	@Transactional(value = "ryxrs_transactionManager")
	public Map getAdtCapsList(Map param) {
		punctualityMapper_Ryxrs.selectAdtCapsList(param);
		return param;
	}
	
	// 근태 정보 조회
	@Transactional(value = "ryxrs_transactionManager")
	public List<PunctualityInfo> getPunctualityList(PunctualityInfo param) {
		return punctualityMapper_Ryxrs.selectPunctualityList(param);
	}

	// 근태 정보 조회 권한
	@Transactional(value = "ryhr_transactionManager")
	public PunctualityInfo getPunctualityAuth(PunctualityInfo param) {
		return commonMapper_Ryhr.selectPunctualityAuth(param);
	}

	
	
}
