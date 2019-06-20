package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.VehicleMapper_Ryhr;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.VehicleInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("vehicleService")
public class VehicleService {

	@Autowired
	private VehicleMapper_Ryhr vehicleMapper_Ryhr;

	// 법인 차량 관리 전체 카운트
	@Transactional(value = "ryhr_transactionManager")
	public int getVehicleListCount(PageParam pageParam) {
		return vehicleMapper_Ryhr.selectVehicleListCount(pageParam);
	}

	// 법인 차량 관리 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<VehicleInfo> getVehicleList(PageParam pageParam) {
		return vehicleMapper_Ryhr.selectVehicleList(pageParam);
	}

	// 법인 차량 관리 상세
	@Transactional(value = "ryhr_transactionManager")
	public VehicleInfo getVehicle(VehicleInfo param) {
		return vehicleMapper_Ryhr.selectVehicle(param);
	}

	// 법인 차량 관리 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addVehicle(VehicleInfo param) {
		return vehicleMapper_Ryhr.insertVehicle(param);
	}

	// 법인 차량 관리 파일 등록
	@Transactional(value = "ryhr_transactionManager")
	public int modifyFileVehicle(VehicleInfo param) {
		return vehicleMapper_Ryhr.updateFileVehicle(param);
	}

	// 법인 차량 관리 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyVehicle(VehicleInfo param) {
		return vehicleMapper_Ryhr.updateVehicle(param);
	}

	// 법인 차량 관리 종료 처리
	@Transactional(value = "ryhr_transactionManager")
	public int deleteVehicle(VehicleInfo param) {
		return vehicleMapper_Ryhr.deleteVehicle(param);
	}

	// 법인 차량 관리 통계
	@Transactional(value = "ryhr_transactionManager")
	public List<VehicleInfo> getVehicleStatisticsList(VehicleInfo param) {
		return vehicleMapper_Ryhr.selectVehicleStatisticsList(param);
	}

}
