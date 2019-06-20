package kr.co.reyonpharm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.ryhr.AppMapper_Ryhr;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.PcMessageInfo;
import kr.co.reyonpharm.models.PcOffInfo;
import kr.co.reyonpharm.models.PcProgramInfo;
import kr.co.reyonpharm.models.PcReportInfo;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("appService")
public class AppService {

	@Autowired
	private AppMapper_Ryhr appMapper_Ryhr;

	@Transactional(value = "ryhr_transactionManager")
	public PcReportInfo getPcReportInfoList(PcReportInfo param) {
		return appMapper_Ryhr.selectPcReportInfoList(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int addPcReportInfo(PcReportInfo param) {
		return appMapper_Ryhr.insertPcReportInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int modifyPcReportInfo(PcReportInfo param) {
		return appMapper_Ryhr.updatePcReportInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public List<PcMessageInfo> getPcMessageInfoList(PcMessageInfo param) {
		return appMapper_Ryhr.selectPcMessageInfoList(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public int modifyPcMessageInfo(PcMessageInfo param) {
		return appMapper_Ryhr.updatePcMessageInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public List<PcOffInfo> getPcOffInfo(PcOffInfo param) {
		return appMapper_Ryhr.selectPcOffInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public UserInfo getUserInfo(UserInfo param) {
		return appMapper_Ryhr.selectUserInfo(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public PcProgramInfo getPcProgramInfo(PcProgramInfo param) {
		return appMapper_Ryhr.selectPcProgramInfo(param);
	}

	// PC관리 - PC ON/OFF 보고 목록 전체 건수
	@Transactional(value = "ryhr_transactionManager")
	public int getPcReportInfoListCount(PageParam param) {
		return appMapper_Ryhr.selectPcReportInfoListCount(param);
	}

	// PC관리 - PC ON/OFF 보고 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<PcReportInfo> getPcReportInfoListByRms(PageParam param) {
		return appMapper_Ryhr.selectPcReportInfoListByRms(param);
	}

	// PC관리 - PC 메시지 전송 목록 전체 건수
	@Transactional(value = "ryhr_transactionManager")
	public int getPcMessageInfoListCount(PageParam param) {
		return appMapper_Ryhr.selectPcMessageInfoListCount(param);
	}

	// PC관리 - PC 메시지 전송 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<PcMessageInfo> getPcMessageInfoListByRms(PageParam param) {
		return appMapper_Ryhr.selectPcMessageInfoListByRms(param);
	}

	// PC관리 - PC 메시지 전송 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addPcMessageInfo(PcMessageInfo param) {
		return appMapper_Ryhr.insertPcMessageInfo(param);
	}

	// PC관리 - PC 메시지 전송 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deletePcMessageInfo(PcMessageInfo param) {
		return appMapper_Ryhr.deletePcMessageInfo(param);
	}

	// PC관리 - PC 종료 해제 목록 전체 건수
	@Transactional(value = "ryhr_transactionManager")
	public int getPcProgramInfoListCount(PageParam param) {
		return appMapper_Ryhr.selectPcProgramInfoListCount(param);
	}

	// PC관리 - PC 종료 해제 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<PcProgramInfo> getPcProgramInfoListByRms(PageParam param) {
		return appMapper_Ryhr.selectPcProgramInfoListByRms(param);
	}

	// PC관리 - PC 종료 해제 상세
	@Transactional(value = "ryhr_transactionManager")
	public PcProgramInfo getPcProgramInfoByRms(PcProgramInfo param) {
		return appMapper_Ryhr.selectPcProgramInfoByRms(param);
	}

	// PC관리 - PC 종료 해제 등록
	@Transactional(value = "ryhr_transactionManager")
	public int addPcProgramInfo(PcProgramInfo param) {
		return appMapper_Ryhr.insertPcProgramInfo(param);
	}

	// PC관리 - PC 종료 해제 수정
	@Transactional(value = "ryhr_transactionManager")
	public int modifyPcProgramInfo(PcProgramInfo param) {
		return appMapper_Ryhr.updatePcProgramInfo(param);
	}

	// PC관리 - PC 종료 해제 승인/거절 목록
	@Transactional(value = "ryhr_transactionManager")
	public List<PcProgramInfo> getPcProgramInfoConfirmList(PcProgramInfo param) {
		return appMapper_Ryhr.selectPcProgramInfoConfirmList(param);
	}

	// PC관리 - PC 종료 해제 승인/거절
	@Transactional(value = "ryhr_transactionManager")
	public int confirmPcProgramInfo(PcProgramInfo param) {
		return appMapper_Ryhr.confirmPcProgramInfo(param);
	}

	// PC관리 - PC 종료 해제 삭제
	@Transactional(value = "ryhr_transactionManager")
	public int deletePcProgramInfo(PcProgramInfo param) {
		return appMapper_Ryhr.deletePcProgramInfo(param);
	}

}
