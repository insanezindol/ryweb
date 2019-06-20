package kr.co.reyonpharm.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.reyonpharm.service.SchedulerService;
import kr.co.reyonpharm.util.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReyonScheduler {

	@Autowired
	SchedulerService schedulerService;

	// 사용자 리스트 json 파일 생성
	public void madeTotalUserListToJson() {
		schedulerService.madeTotalUserListToJson();
	}

	// 부서 리스트 json 파일 생성
	public void madeTotalDeptListToJson() {
		schedulerService.madeTotalDeptListToJson();
	}

	// 회의 상태 업데이트
	public void updateMeetingStatus() {
		schedulerService.updateMeetingStatus();
	}

	// 미완료 건에 대한 처리 진행 메일 발송
	public void sendMailForIncomplete() {
		schedulerService.sendMailForIncomplete();
	}

	// 계약기간 도래 안내 메일 발송
	public void sendMailContract() {
		schedulerService.sendMailContract();
	}

	// 계약기간 종료 상태 업데이트 진행 메일 발송
	public void sendUpdateContractStatusMail() {
		schedulerService.sendUpdateContractStatusMail();
	}
	
	// 메일 발송 정보 이관
	public void migrationMail() {
		schedulerService.migrationMail();
	}
	
	// 메일 발송 정보 이관 데이터 삭제 (1개월 이전)
	public void deleteMigrationMail() {
		schedulerService.deleteMigrationMail();
	}
	
	// 그룹웨어 비밀번호 변경시 MIS 동일하게 반영
	public void syncPwd() {
		// 그룹웨어 비밀번호를 MIS로 연동
		schedulerService.syncPwdGW2MIS();
		// MIS 비밀번호를 그룹웨어로 연동
		schedulerService.syncPwdMIS2GW();
	}
	
	// 그룹웨어 EIS 판매,수금 실적 데이터 연동
	public void syncSale() {
		schedulerService.syncSale();
	}
	
	// 그룹웨어 결재 상태 MIS 동일하게 반영
	public void syncApproval() {
		schedulerService.syncApprovalProceedingData();
		schedulerService.syncApprovalCompeleteData();
	}
	
	// 그룹웨어 외부접속 상태 변경
	public void changeGroupwareStatus() {
		schedulerService.changeGroupwareStatus();
	}
	
	// 그룹웨어 결재 완료 1개월 이전 데이터 삭제
	public void deleteApprovalData() {
		schedulerService.deleteApprovalData();
	}

	// 제품 일련번호 이관
	public void migrationProduct() {
		log.info("[BEG] migrationProduct");
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.DATE, -1);
			String outdate = df.format(cal.getTime());
			Map<String, Integer> output = schedulerService.migrationProduct(outdate);
			int deleteSize = output.get("deleteSize");
			int originSize = output.get("originSize");
			int copySize = output.get("copySize");
			log.info("[RESULT] migrationProduct - deleteSize : " + deleteSize + ", originSize : " + originSize + ", copySize : " + copySize);
			log.info("[migrationProduct] SUCCESS MIGRATION PRODUCT");
		} else {
			log.info("[migrationProduct] SCHEDULER PASS : " + serverType);
		}
		log.info("[END] migrationProduct");
	}

	// 날씨 리스트 json 파일 생성
	public void madeWeatherListToJson() {
		schedulerService.madeWeatherListToJson();
	}

	// 스마트폰 알림 발송
	public void sendAlarm() {
		schedulerService.sendAlarm();
	}
	
	// 미리보기에 사용한 임시 폴더 내 파일 삭제
	public void deleteTempData() {
		schedulerService.deleteTempData();
	}
	
	// 그룹웨어로 내선번호 연동
	public void changeGroupwarePhonenum() {
		schedulerService.changeGroupwarePhonenum();
	}

}
