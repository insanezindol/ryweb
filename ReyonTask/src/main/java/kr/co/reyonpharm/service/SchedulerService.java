package kr.co.reyonpharm.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.mapper.gw.SchedulerMapper_Gw;
import kr.co.reyonpharm.mapper.gwif.SchedulerMapper_Gwif;
import kr.co.reyonpharm.mapper.mssql.SchedulerMapper_Mssql;
import kr.co.reyonpharm.mapper.ryacc.SchedulerMapper_Ryacc;
import kr.co.reyonpharm.mapper.ryhr.SchedulerMapper_Ryhr;
import kr.co.reyonpharm.mapper.rysd.SchedulerMapper_Rysd;
import kr.co.reyonpharm.mapper.ryxrs.SchedulerMapper_Ryxrs;
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.BtsInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.ContractInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.GwApprovalInfo;
import kr.co.reyonpharm.models.GwMailInfo;
import kr.co.reyonpharm.models.GwPwdInfo;
import kr.co.reyonpharm.models.HttpClientResult;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PhoneInfo;
import kr.co.reyonpharm.models.SaleInfo;
import kr.co.reyonpharm.models.TokenInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.models.VehicleInfo;
import kr.co.reyonpharm.util.AesUtil;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.JsonUtil;
import kr.co.reyonpharm.util.ReyonSha256;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Slf4j
@Service("schedulerService")
public class SchedulerService {

	@Autowired
	private SchedulerMapper_Ryhr schedulerMapper_Ryhr;
	
	@Autowired
	private SchedulerMapper_Rysd schedulerMapper_Rysd;
	
	@Autowired
	private SchedulerMapper_Ryxrs schedulerMapper_Ryxrs;
	
	@Autowired
	private SchedulerMapper_Ryacc schedulerMapper_Ryacc;

	@Autowired
	private SchedulerMapper_Gw schedulerMapper_Gw;
	
	@Autowired
	private SchedulerMapper_Gwif schedulerMapper_Gwif;
	
	@Autowired
	private SchedulerMapper_Mssql schedulerMapper_Mssql;

	// 사원 정보 리스트 json 파일 생성
	public void madeTotalUserListToJson() {
		try {
			UserInfo param = new UserInfo();
			List<UserInfo> list = schedulerMapper_Ryhr.selectTotalUserList(param);
			String jsonData = JSONArray.fromObject(list).toString();
			AesUtil aesUtil = new AesUtil(128, 1000);
			String encData = aesUtil.encryptStr(jsonData);
			String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
			String fileName = Constants.configProp.getProperty(Constants.USER_LIST_FILENAME);
			FileIOUtils.stringFileWrite(encData, filePath + fileName);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 부서 정보 리스트 json 파일 생성
	public void madeTotalDeptListToJson() {
		try {
			DeptInfo param = new DeptInfo();
			List<DeptInfo> list = schedulerMapper_Ryhr.selectTotalDeptList(param);
			String jsonData = JSONArray.fromObject(list).toString();
			AesUtil aesUtil = new AesUtil(128, 1000);
			String encData = aesUtil.encryptStr(jsonData);
			String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
			String fileName = Constants.configProp.getProperty(Constants.DEPT_LIST_FILENAME);
			FileIOUtils.stringFileWrite(encData, filePath + fileName);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 회의 상태 업데이트
	public void updateMeetingStatus() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);

		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<MeetingInfo> list = schedulerMapper_Ryhr.selectMeetingResultStatusList();
			int updateDataCnt01to02 = 0;
			for (int i = 0; i < list.size(); i++) {
				MeetingInfo info = list.get(i);
				updateDataCnt01to02 += schedulerMapper_Ryhr.updateMeetingResultStatusToResult(info);
				sendResultMail(info);
			}
			// 결과 미등록 회의는 완료로 처리
			int updateDataCnt05toBB = schedulerMapper_Ryhr.updateMeetingResultStatusToComplete();
			log.info("[UPDATE MEETING STATUS COMPLETED] 01to02 : " + updateDataCnt01to02 + ", 05toBB : " + updateDataCnt05toBB);
		} else {
			log.info("[UPDATE MEETING STATUS COMPLETED] SCHEDULER PASS : " + serverType);
		}
	}

	// 미완료 건에 대한 처리 진행 메일 발송
	public void sendMailForIncomplete() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);

		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			// 02 : 결과미등록
			// 03 : 결재미상신
			// 04 : 결재대기
			// 99 : 결재반려
			List<MeetingInfo> list = schedulerMapper_Ryhr.selectIncompleteList();
			for (int i = 0; i < list.size(); i++) {
				MeetingInfo info = list.get(i);
				String meetingStatus = info.getMeetingStatus();
				if (meetingStatus.equals("02")) {
					sendResultMail(info);
				} else if (meetingStatus.equals("03")) {
					sendSactionMail(info);
				} else if (meetingStatus.equals("04")) {
					sendConfirmMail(info);
				} else if (meetingStatus.equals("99")) {
					sendConfirmFailMail(info);
				}
			}
			log.info("[sendMailForIncomplete] SUCCESS SEND MAIL TO INCOMPLETE JOB");
		} else {
			log.info("[sendMailForIncomplete] SCHEDULER PASS : " + serverType);
		}
	}

	// 계약기간 도래 안내 메일 발송 (부동산 + 법인차량)
	public void sendMailContract() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			// 부동산 계약
			List<ContractInfo> contractList = schedulerMapper_Ryhr.selectContractForSendMail();
			for (int i = 0; i < contractList.size(); i++) {
				ContractInfo info = contractList.get(i);
				String limitDay = info.getLimitDay();
				if (limitDay.equals("0") || limitDay.equals("5") || limitDay.equals("10") || limitDay.equals("15") || limitDay.equals("20") || limitDay.equals("25") || limitDay.equals("30")) {
					sendContractMail(info);
				}
			}
			log.info("[sendMailContract] SUCCESS SEND MAIL TO CONTRACT");

			// 법인 차량 계약
			List<VehicleInfo> vehicleList = schedulerMapper_Ryhr.selectVehicleForSendMail();
			for (int i = 0; i < vehicleList.size(); i++) {
				VehicleInfo info = vehicleList.get(i);
				String limitDay = info.getLimitDay();
				if (limitDay.equals("0") || limitDay.equals("5") || limitDay.equals("10") || limitDay.equals("15") || limitDay.equals("20") || limitDay.equals("25") || limitDay.equals("30")) {
					sendVehicleMail(info);
				}
			}
			log.info("[sendMailContract] SUCCESS SEND MAIL TO VEHICLE");
		} else {
			log.info("[sendMailContract] SCHEDULER PASS : " + serverType);
		}
	}

	// 계약기간 종료 상태 업데이트 진행 메일 발송
	public void sendUpdateContractStatusMail() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			// 부동산 계약
			List<ContractInfo> contractList = schedulerMapper_Ryhr.selectContractUpdateStatusForSendMail();
			for (int i = 0; i < contractList.size(); i++) {
				ContractInfo info = contractList.get(i);
				sendContractUpdateMail(info);
			}
			log.info("[sendUpdateContractStatusMail] SUCCESS SEND MAIL TO CONTRACT EXPIRED");

			// 법인 차량 계약
			List<VehicleInfo> vehicleList = schedulerMapper_Ryhr.selectVehicleUpdateStatusForSendMail();
			for (int i = 0; i < vehicleList.size(); i++) {
				VehicleInfo info = vehicleList.get(i);
				sendVehicleUpdateMail(info);
			}
			log.info("[sendUpdateContractStatusMail] SUCCESS SEND MAIL TO VEHICLE EXPIRED");
		} else {
			log.info("[sendUpdateContractStatusMail] SCHEDULER PASS : " + serverType);
		}
	}

	// 날씨 정보 리스트 json 파일 생성
	public void madeWeatherListToJson() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			try {
				String appid = "7d56fa86d8165c1ec2db1a51d9eae490";
				String[] weatherIdArr = { "1835848", "1846095", "1845033" }; // 서울, 진천, 충주
				
				Map<String, Object> weatherMap = new Hashtable();
				for (int i = 0; i < weatherIdArr.length; i++) {
					String url = "http://api.openweathermap.org/data/2.5/weather?";
					Map<String, String> params = new Hashtable<String, String>();
					params.put("appid", appid);
					params.put("id", weatherIdArr[i]);
	
					Iterator<String> keys = params.keySet().iterator();
					int idx = 0;
					while (keys.hasNext()) {
						String key = keys.next();
						if (idx++ != 0) {
							url += "&";
						}
						url += key + "=" + params.get(key).toString();
					}
					log.info("url : " + url);
					
					HttpClientResult httpClientResult = CommonUtils.getHttpClient(url);
					if (httpClientResult.isResult()) {
						String resultMsg = httpClientResult.getResultMsg();
						log.info(resultMsg);
						if(i == 0) {
							weatherMap.put("seoul", resultMsg);
						} else if(i == 1) {
							weatherMap.put("jincheon", resultMsg);
						} else if(i == 2) {
							weatherMap.put("chungju", resultMsg);
						}
					} else {
						log.error("[madeWeatherListToJson] WEATHER API NETWORK ERROR");
					}
				}
				
				JSONObject jsonobject = JsonUtil.getJsonStringFromMap(weatherMap);
				
				String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
				String fileName = Constants.configProp.getProperty(Constants.WEATHER_LIST_FILENAME);
				FileIOUtils.stringFileWrite(jsonobject.toString(), filePath + fileName);
				log.info("[madeWeatherListToJson] SCHEDULER SUCCESS");
			} catch (Exception e) {
				log.error("[madeWeatherListToJson] SCHEDULER FAIL : " + e.toString());
			}
		} else {
			log.info("[madeWeatherListToJson] SCHEDULER PASS : " + serverType);
		}
	}

	// 결과 등록 요청 메일 발송
	public void sendResultMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				String emailTitle = "관리시스템 결과등록 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				if (info.getMeetingType().equals("01")) {
					emailContents.append("[이연제약 관리 시스템]<br><br>");
					emailContents.append("결과 등록 하실 회의 정보가 있습니다.<br>확인해 주세요.<br><br>");
					emailContents.append(" - 회의제목 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 회의일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				} else {
					emailContents.append("[이연제약 관리 시스템]<br><br>");
					emailContents.append("결과 등록 하실 방문 정보가 있습니다.<br>확인해 주세요.<br><br>");
					emailContents.append(" - 방문목적 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 방문일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 방문 업체명 : ");
					emailContents.append(info.getVisitCompany());
					emailContents.append("<br> - 방문자 성함 : ");
					emailContents.append(info.getVisitName());
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				}
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 결재 상신 요청 메일 발송
	public void sendSactionMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				String emailTitle = "관리시스템 결재 상신 요청 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				if (info.getMeetingType().equals("01")) {
					emailContents.append("[이연제약 관리 시스템]<br><br>");
					emailContents.append("결재 상신 하실 회의 결과 정보가 있습니다.<br>");
					emailContents.append("확인해 주세요.<br><br>");
					emailContents.append(" - 회의제목 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 회의일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				} else {
					emailContents.append("[이연제약 관리 시스템]<br><br>");
					emailContents.append("결재 상신 하실 방문 결과 정보가 있습니다.<br>");
					emailContents.append("확인해 주세요.<br><br>");
					emailContents.append(" - 방문목적 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 방문일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 방문 업체명 : ");
					emailContents.append(info.getVisitCompany());
					emailContents.append("<br> - 방문자 성함 : ");
					emailContents.append(info.getVisitName());
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				}
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 결재 진행 요청 메일 발송
	public void sendConfirmMail(MeetingInfo info) {
		try {
			// 결재자 정보
			ConfirmInfo confirmInfoParam = new ConfirmInfo();
			confirmInfoParam.setDeptCode(info.getDeptCode());
			List<ConfirmInfo> confirmPersonList = schedulerMapper_Ryhr.selectConfirmInfoList(confirmInfoParam);

			if (confirmPersonList.size() != 0) {
				ConfirmInfo confirmInfo = confirmPersonList.get(0);
				UserInfo param = new UserInfo();
				param.setSabun(confirmInfo.getSabun());
				UserInfo userInfo = getUserEmail(param);
				String emailAddress = userInfo.getReyonMail();
				log.info("emailAddress : " + emailAddress);
				if (emailAddress != null) {
					String emailTitle = "관리시스템 결재요청 알림";
					StringBuffer emailContents = new StringBuffer();
					emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
					emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
					emailContents.append("<head>");
					emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
					emailContents.append("<title>이연제약 관리시스템</title>");
					emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
					emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
					emailContents.append("</head>");
					emailContents.append("<body>");
					if (info.getMeetingType().equals("01")) {
						emailContents.append("[이연제약 관리 시스템]<br><br>");
						emailContents.append("결재 하실 회의 결과 정보가 있습니다.<br>");
						emailContents.append("확인해 주세요.<br><br>");
						emailContents.append(" - 회의제목 : ");
						emailContents.append(info.getMeetingName());
						emailContents.append("<br> - 회의일시 : ");
						emailContents.append(info.getMeetingStartDate());
						emailContents.append(" ~ ");
						emailContents.append(info.getMeetingEndDate());
						emailContents.append("<br><br>");
						emailContents.append(CommonUtils.shortcutLinkMaker(confirmInfo.getSabun()));
						emailContents.append("<br><br>");
					} else {
						emailContents.append("[이연제약 관리 시스템]<br><br>");
						emailContents.append("결재 하실 방문 결과 정보가 있습니다.<br>");
						emailContents.append("확인해 주세요.<br><br>");
						emailContents.append(" - 방문목적 : ");
						emailContents.append(info.getMeetingName());
						emailContents.append("<br> - 방문일시 : ");
						emailContents.append(info.getMeetingStartDate());
						emailContents.append(" ~ ");
						emailContents.append(info.getMeetingEndDate());
						emailContents.append("<br> - 방문 업체명 : ");
						emailContents.append(info.getVisitCompany());
						emailContents.append("<br> - 방문자 성함 : ");
						emailContents.append(info.getVisitName());
						emailContents.append("<br><br>");
						emailContents.append(CommonUtils.shortcutLinkMaker(confirmInfo.getSabun()));
						emailContents.append("<br><br>");
					}
					emailContents.append("</body>");
					emailContents.append("</html>");

					sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
				} else {
					log.info("[sabun : " + confirmInfo.getSabun() + "] - mail address not exist");
				}
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 결재 반려 메일 발송
	public void sendConfirmFailMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				String emailTitle = "관리시스템 결재반려 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("결재가 반려되었습니다.<br>");
				emailContents.append("수정 후 재상신 바랍니다.<br><br>");
				if (info.getMeetingType().equals("01")) {
					emailContents.append(" - 회의제목 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 회의일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 결재자 : ");
					emailContents.append(info.getReturnName());
					if (info.getReturnComment() != null) {
						emailContents.append("<br> - 반려 코멘트 : ");
						emailContents.append(info.getReturnComment());
					}
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				} else {
					emailContents.append(" - 방문목적 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 방문일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 방문 업체명 : ");
					emailContents.append(info.getVisitCompany());
					emailContents.append("<br> - 방문자 성함 : ");
					emailContents.append(info.getVisitName());
					emailContents.append("<br> - 결재자 : ");
					emailContents.append(info.getReturnName());
					if (info.getReturnComment() != null) {
						emailContents.append("<br> - 반려 코멘트 : ");
						emailContents.append(info.getReturnComment());
					}
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				}
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 계약기간 도래 안내 메일 발송
	public void sendContractMail(ContractInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				DecimalFormat df = new DecimalFormat("#,###");

				String emailTitle = "관리시스템 부동산 계약 관리 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("곧 만료되는 계약 정보가 있습니다.<br>확인해 주세요.<br><br>");
				emailContents.append("<span style=\"color:red;font-weight: bold;\"> - 만료까지 남은기간 : ");
				emailContents.append(info.getLimitDay());
				emailContents.append(" 일</span><br><br> - 계약구분 : ");
				emailContents.append(info.getSaupGubun());
				emailContents.append("<br> - 용도 / 사용자 : ");
				emailContents.append(info.getDivision());
				emailContents.append(" / ");
				emailContents.append(info.getUsername());
				emailContents.append("<br> - 계약기간 : ");
				emailContents.append(info.getStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getEndDate());
				emailContents.append("<br> - 소재지(도로명) : ");
				emailContents.append(info.getRoadAddr());
				if (info.getDetailAddr() != null) {
					emailContents.append(" ");
					emailContents.append(info.getDetailAddr());
				}
				emailContents.append("<br> - 소재지(지번) : ");
				emailContents.append(info.getJibunAddr());
				if (info.getDetailAddr() != null) {
					emailContents.append(" ");
					emailContents.append(info.getDetailAddr());
				}
				emailContents.append("<br> - 지불방법 : ");
				emailContents.append(info.getPayment());
				emailContents.append("<br> - 보증금 : ");
				emailContents.append(df.format(Integer.parseInt(info.getDeposit())));
				emailContents.append("<br> - 임대료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getRent())));
				emailContents.append("<br> - 관리비 : ");
				emailContents.append(df.format(Integer.parseInt(info.getAdministrativeExpenses())));
				emailContents.append("<br><br>");
				emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
				emailContents.append("<br><br>");
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 계약기간 만료 안내 메일 발송
	public void sendContractUpdateMail(ContractInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				DecimalFormat df = new DecimalFormat("#,###");

				String emailTitle = "관리시스템 부동산 계약 관리 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("만료된 계약 정보가 있습니다.<br>임대 종료 상태로 변경해 주세요.<br><br>");
				emailContents.append("<span style=\"color:red;font-weight: bold;\"> - 만료된 기간 : ");
				emailContents.append(info.getLimitDay());
				emailContents.append(" 일 지남</span><br><br> - 계약구분 : ");
				emailContents.append(info.getSaupGubun());
				emailContents.append("<br> - 용도 / 사용자 : ");
				emailContents.append(info.getDivision());
				emailContents.append(" / ");
				emailContents.append(info.getUsername());
				emailContents.append("<br> - 계약기간 : ");
				emailContents.append(info.getStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getEndDate());
				emailContents.append("<br> - 소재지(도로명) : ");
				emailContents.append(info.getRoadAddr());
				if (info.getDetailAddr() != null) {
					emailContents.append(" ");
					emailContents.append(info.getDetailAddr());
				}
				emailContents.append("<br> - 소재지(지번) : ");
				emailContents.append(info.getJibunAddr());
				if (info.getDetailAddr() != null) {
					emailContents.append(" ");
					emailContents.append(info.getDetailAddr());
				}
				emailContents.append("<br> - 지불방법 : ");
				emailContents.append(info.getPayment());
				emailContents.append("<br> - 보증금 : ");
				emailContents.append(df.format(Integer.parseInt(info.getDeposit())));
				emailContents.append("<br> - 임대료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getRent())));
				emailContents.append("<br> - 관리비 : ");
				emailContents.append(df.format(Integer.parseInt(info.getAdministrativeExpenses())));
				emailContents.append("<br><br>");
				emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
				emailContents.append("<br><br>");
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 차량 기간 도래 안내 메일 발송
	public void sendVehicleMail(VehicleInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				DecimalFormat df = new DecimalFormat("#,###");

				String emailTitle = "관리시스템 법인 차량 관리 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("곧 만료되는 법인 차량 정보가 있습니다.<br>확인해 주세요.<br><br>");
				emailContents.append("<span style=\"color:red;font-weight: bold;\"> - 만료까지 남은기간 : ");
				emailContents.append(info.getLimitDay());
				emailContents.append(" 일</span><br><br> - 차량종류 : ");
				emailContents.append(info.getVehicleType());
				emailContents.append("<br> - 차량번호 : ");
				emailContents.append(info.getVehicleNo());
				emailContents.append("<br> - 용도 / 사용자 : ");
				emailContents.append(info.getDivision());
				emailContents.append(" / ");
				emailContents.append(info.getUsername());
				emailContents.append("<br> - 지급구분 : ");
				emailContents.append(info.getPayment());
				emailContents.append("<br> - 임대기간 : ");
				emailContents.append(info.getRentStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getRentEndDate());
				emailContents.append("<br> - 보험료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getInsuranceMoney())));
				emailContents.append("<br> - 임차료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getRentMoney())));
				emailContents.append("<br><br>");
				emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
				emailContents.append("<br><br>");
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 법인 차량 만료 안내 메일 발송
	public void sendVehicleUpdateMail(VehicleInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				DecimalFormat df = new DecimalFormat("#,###");

				String emailTitle = "관리시스템 법인 차량 관리 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("만료된 법인 차량 정보가 있습니다.<br>종료 상태로 변경해 주세요.<br><br>");
				emailContents.append("<span style=\"color:red;font-weight: bold;\"> - 만료된 기간 : ");
				emailContents.append(info.getLimitDay());
				emailContents.append(" 일 지남</span><br><br> - 차량종류 : ");
				emailContents.append(info.getVehicleType());
				emailContents.append("<br> - 차량번호 : ");
				emailContents.append(info.getVehicleNo());
				emailContents.append("<br> - 용도 / 사용자 : ");
				emailContents.append(info.getDivision());
				emailContents.append(" / ");
				emailContents.append(info.getUsername());
				emailContents.append("<br> - 지급구분 : ");
				emailContents.append(info.getPayment());
				emailContents.append("<br> - 임대기간 : ");
				emailContents.append(info.getRentStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getRentEndDate());
				emailContents.append("<br> - 보험료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getInsuranceMoney())));
				emailContents.append("<br> - 임차료 : ");
				emailContents.append(df.format(Integer.parseInt(info.getRentMoney())));
				emailContents.append("<br><br>");
				emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
				emailContents.append("<br><br>");
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 결재 성공 메일 발송
	public void sendConfirmSuccessMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = getUserEmail(param);
			String emailAddress = userInfo.getReyonMail();
			log.info("emailAddress : " + emailAddress);

			if (emailAddress != null) {
				String emailTitle = "관리시스템 결재완료 알림";
				StringBuffer emailContents = new StringBuffer();
				emailContents.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContents.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				emailContents.append("<head>");
				emailContents.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				emailContents.append("<title>이연제약 관리시스템</title>");
				emailContents.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
				emailContents.append("<style type=\"text/css\">body { font-size:10pt; font-family:\"맑은 고딕\"; }</style>");
				emailContents.append("</head>");
				emailContents.append("<body>");
				emailContents.append("[이연제약 관리 시스템]<br><br>");
				emailContents.append("결재가 완료되었습니다.<br><br>");
				if (info.getMeetingType().equals("01")) {
					emailContents.append(" - 회의제목 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 회의일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 결재자 : ");
					emailContents.append(info.getConfirmName());
					if (info.getConfirmComment() != null) {
						emailContents.append("<br> - 결재 코멘트 : ");
						emailContents.append(info.getConfirmComment());
					}
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				} else {
					emailContents.append(" - 방문목적 : ");
					emailContents.append(info.getMeetingName());
					emailContents.append("<br> - 방문일시 : ");
					emailContents.append(info.getMeetingStartDate());
					emailContents.append(" ~ ");
					emailContents.append(info.getMeetingEndDate());
					emailContents.append("<br> - 방문 업체명 : ");
					emailContents.append(info.getVisitCompany());
					emailContents.append("<br> - 방문자 성함 : ");
					emailContents.append(info.getVisitName());
					emailContents.append("<br> - 결재자 : ");
					emailContents.append(info.getConfirmName());
					if (info.getConfirmComment() != null) {
						emailContents.append("<br> - 결재 코멘트 : ");
						emailContents.append(info.getConfirmComment());
					}
					emailContents.append("<br><br>");
					emailContents.append(CommonUtils.shortcutLinkMaker(info.getUpdSabun()));
					emailContents.append("<br><br>");
				}
				emailContents.append("</body>");
				emailContents.append("</html>");

				sendNotifyEmailByGw(emailAddress, emailTitle, emailContents);
			} else {
				log.info("[sabun : " + info.getUpdSabun() + "] - mail address not exist");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 사용자 e-mail 주소 불러오기
	public UserInfo getUserEmail(UserInfo param) {
		return schedulerMapper_Ryhr.selectUserEmail(param);
	}
	
	// 메일 정보 이관(그룹웨어 이메일 발송 테이블 INSERT)
	public void migrationMail() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GwMailInfo> list = schedulerMapper_Ryxrs.selectGwMailInfoList();
			for (int i = 0; i < list.size(); i++) {
				GwMailInfo info = list.get(i);
				log.info("[migrationMail] ["+ info.getMfrom() +"] ["+ info.getMto() +"] ["+ info.getRcvDate() +"] [" + info.getSubject() + "]");
				try {
					int resultCnt = schedulerMapper_Gw.insertGwMailInfo(info);
					if(resultCnt == 1) {
						int updCnt = schedulerMapper_Ryxrs.updateGwMailInfo(info);
						if(updCnt == 1) {
							log.info("[migrationMail] SUCCESS SEND MAIL");
						} else {
							log.error("[migrationMail] FAIL SEND MAIL : UPDATE COUNT IS NO ONE - " + updCnt);
						}
					} else {
						log.error("[migrationMail] FAIL SEND MAIL : INSERT COUNT IS NO ONE - " + resultCnt);
					}
				} catch (Exception e) {
					log.error("[migrationMail] FAIL SEND MAIL : ["+ info.getMfrom() +"] ["+ info.getMto() +"] ["+ info.getRcvDate() +"] [" + info.getSubject() + "]");
				}
			}
		} else {
			log.info("[migrationMail] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 메일 발송 정보 이관 데이터 삭제 (1개월 이전)
	public void deleteMigrationMail() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			int deleteCnt = schedulerMapper_Ryxrs.deleteGwMailInfo();
			log.info("[deleteMigrationMail] SUCCESS DELETE MIGRATION MAIL : " + deleteCnt);
		} else {
			log.info("[deleteMigrationMail] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 그룹웨어 비밀번호 변경시 MIS 동일하게 반영
	public void syncPwdGW2MIS() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GwPwdInfo> list = schedulerMapper_Gwif.selectPwdInfo();
			for (int i = 0; i < list.size(); i++) {
				GwPwdInfo info = list.get(i);
				String encSiteInfo = ReyonSha256.getCiperText(info.getSiteInfo());
				info.setEncSiteInfo(encSiteInfo);
				// 이력 테이블 DELETE 후 INSERT
				int deleteHistoryCnt = schedulerMapper_Ryhr.deleteHrHistoryPwd(info);
				int insertHistoryCnt = schedulerMapper_Ryhr.insertHrHistoryPwd(info);
				// 마스터 테이블 UPDATE
				int resultCnt = schedulerMapper_Ryhr.updateHrPwd(info);
				if (resultCnt == 1) {
					// REGDATE UPDATE 처리
					//int uCnt = schedulerMapper_Gwif.updatePwdInfo(info);
					// DATA DELETE 처리
					int dCnt = schedulerMapper_Gwif.deletePwdInfo(info);
					if (dCnt != 1) {
						log.error("[syncPwdGW2MIS] FAIL UPDATE GWIF FLAG - " + resultCnt);
					}
					log.info("[syncPwdGW2MIS] [" + info.getSiteNo() + "] [" + info.getSiteInfo() + "] [" + resultCnt + "] [" + deleteHistoryCnt + "] [" + insertHistoryCnt + "] [" + dCnt + "]");
				} else {
					log.error("[syncPwdGW2MIS] FAIL UPDATE HR PWD - " + resultCnt);
				}
			}
		} else {
			log.info("[syncPwdGW2MIS] SCHEDULER PASS : " + serverType);
		}
	}
	
	// MIS 비밀번호 변경시 그룹웨어 동일하게 반영
	public void syncPwdMIS2GW() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GwPwdInfo> list = schedulerMapper_Ryxrs.selectPwdInfo();
			for (int i = 0; i < list.size(); i++) {
				GwPwdInfo info = list.get(i);
				// 마스터 테이블 UPDATE
				int resultCnt = schedulerMapper_Gw.updateGwPwd(info);
				// PROCESS_DATE UPDATE 처리
				//int uCnt = schedulerMapper_Ryxrs.updatePwdInfo(info);
				// DATA DELETE 처리
				int dCnt = schedulerMapper_Ryxrs.deletePwdInfo(info);
				log.info("[syncPwdMIS2GW] [" + info.getSiteNo() + "] [" + info.getSiteInfo() + "] [" + resultCnt + "] [" + dCnt + "]");
			}
		} else {
			log.info("[syncPwdMIS2GW] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 그룹웨어 EIS 판매,수금 실적 데이터 연동
	public void syncSale() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<SaleInfo> saleinfoList = schedulerMapper_Rysd.selectReyonSaleList();
			
			if(saleinfoList.size() != 0) {
				try {
					SaleInfo maxInfo = saleinfoList.get(0);
					SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd" );
					for (int i = 1; i < saleinfoList.size(); i++) {
						Date day1 = format.parse( maxInfo.getGijunDate() );
						Date day2 = format.parse( saleinfoList.get(i).getGijunDate() );
						int compare = day1.compareTo( day2 );
						if(compare < 0) {
							maxInfo = saleinfoList.get(i);
						}
					}
					
					int deleteCnt = schedulerMapper_Gwif.deleteReyonSaleList(maxInfo);
					int insertCnt = 0;
					for (int i = 0; i < saleinfoList.size(); i++) {
						SaleInfo info = saleinfoList.get(i);
						insertCnt += schedulerMapper_Gwif.insertReyonSaleInfo(info);
					}
					log.info("[syncSale] [" + saleinfoList.size() + "] [" + deleteCnt + "] [" + insertCnt + "]");
				} catch (ParseException e) {
					log.error("[syncSale] " + e.getMessage());
				}
			}
		} else {
			log.info("[syncSale] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 그룹웨어 결재 상태 MIS 동일하게 반영 (현재 진행중인 데이터)
	public void syncApprovalProceedingData() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GwApprovalInfo> approvalList = schedulerMapper_Gwif.selectApprovalProceedingList();
			
			for (int i = 0; i < approvalList.size(); i++) {
				GwApprovalInfo info = approvalList.get(i);
				String approKey = info.getApproKey();
				String approState = info.getApproState();
				String fcode = info.getFcode();

				if (fcode.equals("MIS001")) {
					// 회계전표
					String[] approKeyArr = approKey.split("-");
					String tsYy = approKeyArr[0];
					String tsNo = approKeyArr[1];

					info.setTsYy(tsYy);
					info.setTsNo(tsNo);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryacc.updateApprovalProceedingStatusJp(info);

					log.info("[syncApprovalProceedingData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "]");
				} else if (fcode.equals("MIS002")) {
					// 예산신청서
					String[] approKeyArr = approKey.split("-");
					String reqDate = approKeyArr[0];
					String reqDeptCode = approKeyArr[1];
					String reqSeq = approKeyArr[2];

					info.setReqDate(reqDate);
					info.setReqDeptCode(reqDeptCode);
					info.setReqSeq(reqSeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryacc.updateApprovalProceedingStatusYs(info);

					log.info("[syncApprovalProceedingData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "]");
				} else if (fcode.equals("MIS003")) {
					// 휴가신청서
					String[] approKeyArr = approKey.split("-");
					String holidaySeq = approKeyArr[1];

					info.setHolidaySeq(holidaySeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryhr.updateApprovalProceedingStatusHoliday(info);

					log.info("[syncApprovalProceedingData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "]");
				} else if (fcode.equals("MIS004")) {
					// 초과근무신청서
					String[] approKeyArr = approKey.split("-");
					String overtimeSeq = approKeyArr[1];

					info.setOvertimeSeq(overtimeSeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryhr.updateApprovalProceedingStatusOvertime(info);

					log.info("[syncApprovalProceedingData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "]");
				}
			}
		} else {
			log.info("[syncApprovalProceedingData] SCHEDULER PASS : " + serverType);
		}
	}
	
	
	// 그룹웨어 결재 상태 MIS 동일하게 반영 (완료된 데이터)
	public void syncApprovalCompeleteData() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GwApprovalInfo> approvalList = schedulerMapper_Gwif.selectApprovalCompeleteList();
			
			for (int i = 0; i < approvalList.size(); i++) {
				GwApprovalInfo info = approvalList.get(i);
				String approKey = info.getApproKey();
				String approState = info.getApproState();
				String fcode = info.getFcode();

				if (fcode.equals("MIS001")) {
					// 회계전표
					String[] approKeyArr = approKey.split("-");
					String tsYy = approKeyArr[0];
					String tsNo = approKeyArr[1];

					info.setTsYy(tsYy);
					info.setTsNo(tsNo);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryacc.updateApprovalCompeleteStatusJp(info);
					int resultCntGw = 0;
					if (resultCntMis > 0) {
						resultCntGw = schedulerMapper_Gwif.updateApprovalCompeleteStatus(info);
					}

					log.info("[syncApprovalCompeleteData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "] [" + resultCntGw + "]");
				} else if (fcode.equals("MIS002")) {
					// 예산신청서
					String[] approKeyArr = approKey.split("-");
					String reqDate = approKeyArr[0];
					String reqDeptCode = approKeyArr[1];
					String reqSeq = approKeyArr[2];

					info.setReqDate(reqDate);
					info.setReqDeptCode(reqDeptCode);
					info.setReqSeq(reqSeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryacc.updateApprovalCompeleteStatusYs(info);
					int resultCntGw = 0;
					if (resultCntMis > 0) {
						resultCntGw = schedulerMapper_Gwif.updateApprovalCompeleteStatus(info);
					}

					log.info("[syncApprovalCompeleteData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "] [" + resultCntGw + "]");
				} else if (fcode.equals("MIS003")) {
					// 휴가신청서
					String[] approKeyArr = approKey.split("-");
					String holidaySeq = approKeyArr[1];

					info.setHolidaySeq(holidaySeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryhr.updateApprovalCompeleteStatusHoliday(info);
					int resultCntGw = 0;
					if (resultCntMis > 0) {
						resultCntGw = schedulerMapper_Gwif.updateApprovalCompeleteStatus(info);
					}

					log.info("[syncApprovalCompeleteData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "] [" + resultCntGw + "]");
				} else if (fcode.equals("MIS004")) {
					// 초과근무신청서
					String[] approKeyArr = approKey.split("-");
					String overtimeSeq = approKeyArr[1];

					info.setOvertimeSeq(overtimeSeq);
					info.setGwStatus(approState);

					int resultCntMis = schedulerMapper_Ryhr.updateApprovalCompeleteStatusOvertime(info);
					int resultCntGw = 0;
					if (resultCntMis > 0) {
						resultCntGw = schedulerMapper_Gwif.updateApprovalCompeleteStatus(info);
					}

					log.info("[syncApprovalCompeleteData] [" + fcode + "] [" + approKey + "] [" + resultCntMis + "] [" + resultCntGw + "]");
				}
			}
		} else {
			log.info("[syncApprovalCompeleteData] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 그룹웨어 외부접속 상태 변경
	public void changeGroupwareStatus() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<GroupwareExtInfo> ingList = schedulerMapper_Ryhr.selectGroupwareExtIngList();
			for (int i = 0; i < ingList.size(); i++) {
				GroupwareExtInfo info = ingList.get(i);
				int updateCnt = schedulerMapper_Gw.updateGwExtStatus(info);
				if(updateCnt != 0) {
					info.setStatus("2");
					int resultCnt = schedulerMapper_Ryhr.updateGroupwareExt(info);
					log.info("[changeGroupwareStatus] "+ info.getSabun() + " , " + info.getStatus() + " , " + updateCnt + " , " + resultCnt);
				}
			}
			List<GroupwareExtInfo> endList = schedulerMapper_Ryhr.selectGroupwareExtEndList();
			for (int i = 0; i < endList.size(); i++) {
				GroupwareExtInfo info = endList.get(i);
				int updateCnt = schedulerMapper_Gw.updateGwExtStatus(info);
				if(updateCnt != 0) {
					info.setStatus("3");
					int resultCnt = schedulerMapper_Ryhr.updateGroupwareExt(info);
					log.info("[changeGroupwareStatus] "+ info.getSabun() + " , " + info.getStatus() + " , " + updateCnt + " , " + resultCnt);
				}
			}
		} else {
			log.info("[changeGroupwareStatus] SCHEDULER PASS : " + serverType);
		}
	}
	
	// 그룹웨어 결재 완료 1개월 이전 데이터 삭제
	public void deleteApprovalData() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			int resultCnt = schedulerMapper_Gwif.deleteApprovalList();
			log.info("[deleteApprovalData] [resultCnt : " + resultCnt + "]");
		} else {
			log.info("[deleteApprovalData] SCHEDULER PASS : " + serverType);
		}
	}

	// 스마트폰 알람 발송
	public void sendAlarm() {
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			List<AlarmInfo> alarmList = schedulerMapper_Ryhr.selectSendAlarmList();

			for (int i = 0; i < alarmList.size(); i++) {
				AlarmInfo info = alarmList.get(i);

				TokenInfo tokenInfoparam = new TokenInfo();
				tokenInfoparam.setSabun(info.getAlarmSabun());

				AlarmInfo result = new AlarmInfo();
				result.setAlarmSeq(info.getAlarmSeq());

				if (info.getTokenId() != null) {
					if (info.getMsgReceiveType().equals("Y")) {
						Map<String, String> resultMap = sendPushAlarm(info.getAlarmTitle(), info.getAlarmContents(), info.getTokenId());
						if (resultMap.get("resultCode").equals("1")) {
							result.setAlarmYn("Y");
							result.setResultMsg("성공");
						} else {
							result.setAlarmYn("F");
							result.setResultMsg(resultMap.get("resultMsg"));
						}
					} else {
						result.setAlarmYn("F");
						result.setResultMsg(CustomExceptionCodes.NOT_MSG_RECEIVE_USER.getMsg());
					}
				} else {
					result.setAlarmYn("F");
					result.setResultMsg(CustomExceptionCodes.NOT_TOKENID_USER.getMsg());
				}

				int resultCnt = schedulerMapper_Ryhr.updateAlarm(result);
				log.info("resultCnt : " + resultCnt);
			}
			log.info("[sendAlarm] SUCCESS SEND ALARM");
		} else {
			log.info("[sendAlarm] SCHEDULER PASS : " + serverType);
		}
	}

	// 푸시 알람 전송
	public Map<String, String> sendPushAlarm(String title, String msg, String tokenId) {
		Map<String, String> result = new Hashtable<String, String>();

		HttpURLConnection conn = null;
		OutputStream os = null;
		BufferedReader in = null;
		try {
			String apiUrl = Constants.configProp.getProperty(Constants.GOOGLE_FCM_URL);
			String apiKey = Constants.configProp.getProperty(Constants.GOOGLE_FCM_KEY);
			URL url = new URL(apiUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);

			// 전체 알림
			//String input = "{\"notification\" : {\"title\" : \"" + title + "\", \"body\" : \"" + msg + "\"}, \"to\":\"/topics/notice\"}";
			// 토큰별 알림
			String input = "{\"priority\" : \"high\", \"to\":\"" + tokenId + "\", \"data\" : {\"title\" : \"" + title + "\", \"body\" : \"" + msg + "\"} }";
			
			log.info("INPUT : " + input);

			os = conn.getOutputStream();
			os.write(input.getBytes("UTF-8"));
			os.flush();

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				String output = response.toString();
				log.info("OUTPUT : " + output);

				JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(output);
				String success = String.valueOf(jsonObject.get("success"));
				String failure = String.valueOf(jsonObject.get("failure"));

				result.put("tokenId", tokenId);
				if (success.equals("1") && failure.equals("0")) {
					result.put("resultCode", "1");
					result.put("resultMsg", "SUCCESS");
				} else {
					result.put("resultCode", "0");
					result.put("resultMsg", "FAILURE COUNT IS 1");
				}
			} else {
				result.put("resultCode", "0");
				result.put("resultMsg", "ERROR RESPONSE CODE : " + String.valueOf(responseCode));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			result.put("resultCode", "0");
			result.put("resultMsg", "UNKNOWN ERROR");
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
				}
			}
		}

		return result;
	}
	
	// 제품 일련번호 이관
	public Map<String, Integer> migrationProduct(String outdate) {
		log.info("outdate : " + outdate);
		Map<String, Integer> output = new Hashtable<String, Integer>();
		BtsInfo param = new BtsInfo();
		param.setOutdate(outdate);
		// 그날 데이터가 있을수도 있으니까 먼저 삭제
		int deleteCnt = schedulerMapper_Ryxrs.deleteBtsInfo(param);
		// MSSQL 리스트 조회
		List<BtsInfo> mssqlList = schedulerMapper_Mssql.selectBtsInfoList(param);
		int totalResultCnt = 0;
		for (int j = 0; j < mssqlList.size(); j++) {
			BtsInfo info = mssqlList.get(j);
			// ORACLE 등록
			int resultCnt = schedulerMapper_Ryxrs.insertBtsInfo(info);
			if (resultCnt == 1) {
				log.info("[SUC] " + info.getOutdate() + "\t" + info.getCustcode() + "\t" + info.getEcustcode() + "\t" + info.getItemcode() + "\t" + info.getOutqty() + "\t" + info.getOrderno() + "\t" + info.getSeq() + "\t" + info.getSerialno() + "\t" + info.getAgg1() + "\t" + info.getAgg2() + "\t" + info.getAgg4() + "\t" + info.getJumnNo() + "\t" + info.getJpmSeq());
				totalResultCnt++;
			} else {
				log.error("[ERR] " + info.getOutdate() + "\t" + info.getCustcode() + "\t" + info.getEcustcode() + "\t" + info.getItemcode() + "\t" + info.getOutqty() + "\t" + info.getOrderno() + "\t" + info.getSeq() + "\t" + info.getSerialno() + "\t" + info.getAgg1() + "\t" + info.getAgg2() + "\t" + info.getAgg4() + "\t" + info.getJumnNo() + "\t" + info.getJpmSeq());
			}
		}
		output.put("deleteSize", deleteCnt);
		output.put("originSize", mssqlList.size());
		output.put("copySize", totalResultCnt);
		return output;
	}
	
	// 메일 전송 (그룹웨어용)
	public void sendNotifyEmailByGw(String id, String subject, StringBuffer contents) {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String rcvDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		String mfrom = Constants.configProp.getProperty(Constants.SMTP_ACCOUNT);
		String mto = id + "@reyonpharm.co.kr";
		GwMailInfo info = new GwMailInfo();
		info.setMfrom(mfrom);
		info.setMfromName("이연제약관리시스템");
		info.setMto(mto);
		info.setRcvDate(rcvDate);
		info.setSubject(subject);
		info.setContents(contents.toString());
		int resultCnt = schedulerMapper_Gw.insertGwMailInfo(info);
		if (resultCnt == 1) {
			log.info("[SUCC] [" + mto + "] [" + subject + "]");
		} else {
			log.info("[FAIL] [" + mto + "] [" + subject + "]");
		}
	}

	// 미리보기에 사용한 임시 폴더 내 파일 삭제
	public void deleteTempData() {
		log.info("[deleteTempData] BEG");
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			String today = null;
			Date date = new Date();
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, -10);
			today = sdformat.format(cal.getTime());
			String path = Constants.configProp.getProperty(Constants.SYSTEM_TEMP_DIR);
			File dirFile = new File(path);
			File[] fileList = dirFile.listFiles();
			for (File tempFile : fileList) {
				if (tempFile.isFile()) {
					String tempFileName = tempFile.getName();
					if (Long.valueOf(tempFileName.substring(0, tempFileName.lastIndexOf('.'))).longValue() <= Long.valueOf(today).longValue()) {
						log.info("[deleteTempData] [" + today + "] deleted file : " + path + tempFileName);
						FileUtils.deleteQuietly(new File(path + tempFileName));
					}
				}
			}
		} else {
			log.info("[deleteTempData] SCHEDULER PASS");
		}
		log.info("[deleteTempData] END");
	}

	// 내선번호 연동
	public void changeGroupwarePhonenum() {
		log.info("[changeGroupwarePhonenum] BEG");
		String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
		// 상용인 경우만 배치 실행
		if (serverType.equals("real")) {
			PhoneInfo param = new PhoneInfo();
			List<PhoneInfo> list = schedulerMapper_Ryhr.selectPhonenumberList(param);

			for (int i = 0; i < list.size(); i++) {
				PhoneInfo info = list.get(i);
				int resultCnt = schedulerMapper_Gw.updatePhonenumberInfo(info);
				log.info("[changeGroupwarePhonenum] [" + resultCnt + "] [" + info.getSabun() + "] [" + info.getKname() + "] [" + info.getPhonenum() + "]");
			}

		} else {
			log.info("[changeGroupwarePhonenum] SCHEDULER PASS");
		}
		log.info("[changeGroupwarePhonenum] END");
	}
	
}
