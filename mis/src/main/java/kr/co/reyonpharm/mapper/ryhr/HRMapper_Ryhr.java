package kr.co.reyonpharm.mapper.ryhr;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.reyonpharm.models.HolidayInfo;
import kr.co.reyonpharm.models.OvertimeInfo;
import kr.co.reyonpharm.models.SalaryInfo;
import kr.co.reyonpharm.models.UserInfo;

@Repository
public interface HRMapper_Ryhr {

	int selectSalesCheck(UserInfo param);
	
	List<SalaryInfo> selectTotalPayGb(SalaryInfo param);
	
	List<SalaryInfo> selectTotalPayDate(SalaryInfo param);

	SalaryInfo selectSalaryInfo(SalaryInfo param);

	List<HolidayInfo> selectHolidayYYMM(HolidayInfo param);

	HolidayInfo selectHolidayInfo(HolidayInfo param);
	
	List<HolidayInfo> selectHolidayList(HolidayInfo param);

	List<HolidayInfo> selectHolidayMasterList(HolidayInfo param);

	int insertHolidayMaster(HolidayInfo param);

	List<HolidayInfo> selectHolidayMasterDetailList(HolidayInfo param);

	int insertMIS003(HolidayInfo param);

	OvertimeInfo selectOvertimeInfo(OvertimeInfo param);

	List<OvertimeInfo> selectOvertimeList(OvertimeInfo param);

	List<OvertimeInfo> selectUserinfoList(OvertimeInfo param);

	int insertMIS004(OvertimeInfo param);

	int insertMIS004Worker(OvertimeInfo param);

	List<OvertimeInfo> selectOvertimeMasterList(OvertimeInfo param);

}
