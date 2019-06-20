package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.VehicleInfo;

@Repository
public interface VehicleMapper_Ryhr {

	int selectVehicleListCount(PageParam pageParam);

	List<VehicleInfo> selectVehicleList(PageParam pageParam);

	VehicleInfo selectVehicle(VehicleInfo param);

	int insertVehicle(VehicleInfo param);

	int updateFileVehicle(VehicleInfo param);

	int updateVehicle(VehicleInfo param);

	int deleteVehicle(VehicleInfo param);

	List<VehicleInfo> selectVehicleStatisticsList(VehicleInfo param);

}
