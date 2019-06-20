package kr.co.reyonpharm.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.reyonpharm.mapper.gw.CommonMapper_Gw;
import kr.co.reyonpharm.mapper.ryhr.CommonMapper_Ryhr;
import kr.co.reyonpharm.mapper.ryhr.MainMapper_Ryhr;
import kr.co.reyonpharm.mapper.ryhr.MeetingMapper_Ryhr;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.GwMailInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.util.AesUtil;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Slf4j
@Service("commonService")
public class CommonService {

	@Autowired
	private CommonMapper_Ryhr commonMapper_Ryhr;

	@Autowired
	private CommonMapper_Gw commonMapper_Gw;
	
	@Autowired
	private MainMapper_Ryhr mainMapper_Ryhr;
	
	@Autowired
	private MeetingMapper_Ryhr meetingMapper_Ryhr;

	// 사원 정보 리스트 json 파일 가져오기
	public JSONArray getTotalUserListToJson() {
		JSONArray jsonArray = null;
		try {
			String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
			String fileName = Constants.configProp.getProperty(Constants.USER_LIST_FILENAME);
			String jsonData = FileIOUtils.fileReadString(filePath + fileName);
			AesUtil aesUtil = new AesUtil(128, 1000);
			String decData = aesUtil.decryptStr(jsonData);
			jsonArray = (JSONArray) JSONSerializer.toJSON(decData);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
		return jsonArray;
	}

	// 부서 정보 리스트 json 파일 가져오기
	public JSONArray getTotalDeptListToJson() {
		JSONArray jsonArray = null;
		try {
			String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
			String fileName = Constants.configProp.getProperty(Constants.DEPT_LIST_FILENAME);
			String jsonData = FileIOUtils.fileReadString(filePath + fileName);
			AesUtil aesUtil = new AesUtil(128, 1000);
			String decData = aesUtil.decryptStr(jsonData);
			jsonArray = (JSONArray) JSONSerializer.toJSON(decData);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
		return jsonArray;
	}
	
	// 날씨 정보 리스트 json 파일 가져오기
	public JSONObject getWeatherToJson() {
		JSONObject jsonObject = null;
		try {
			String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
			String fileName = Constants.configProp.getProperty(Constants.WEATHER_LIST_FILENAME);
			String jsonData = FileIOUtils.fileReadString(filePath + fileName);
			jsonObject = (JSONObject) JSONSerializer.toJSON(jsonData);
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
		return jsonObject;
	}

	// 공통코드 리스트 가져오기
	@Transactional(value = "ryhr_transactionManager")
	public List<CommonCodeInfo> getCommonCodeList(CommonCodeInfo param) {
		return commonMapper_Ryhr.selectCommonCodeList(param);
	}

	// 결재권자 리스트 가져오기
	@Transactional(value = "ryhr_transactionManager")
	public List<ConfirmInfo> getConfirmInfoList(ConfirmInfo param) {
		return commonMapper_Ryhr.selectConfirmInfoList(param);
	}

	// 인수 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<TakeOverInfo> getTakeoverList(TakeOverInfo param) {
		return commonMapper_Ryhr.selectTakeoverList(param);
	}

	// 부서 정보 리스트 가져오기
	@Transactional(value = "ryhr_transactionManager")
	public List<DeptInfo> getTotalDeptList(DeptInfo param) {
		return commonMapper_Ryhr.selectTotalDeptList(param);
	}

	// 주차권 미등록 된 방문자 리스트 전체 건수 조회
	@Transactional(value = "ryhr_transactionManager")
	public int getActiveTicketListCount(PageParam pageParam) {
		return commonMapper_Ryhr.selectActiveTicketListCount(pageParam);
	}

	// 주차권 미등록 된 방문자 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<ComingInfo> getActiveTicketList(PageParam pageParam) {
		return commonMapper_Ryhr.selectActiveTicketList(pageParam);
	}

	// 방문자 전체 리스트 카운트
	@Transactional(value = "ryhr_transactionManager")
	public int getActiveVisitorListCount(PageParam pageParam) {
		return commonMapper_Ryhr.selectActiveVisitorListCount(pageParam);
	}

	// 방문자 전체 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getActiveVisitorList(PageParam pageParam) {
		return commonMapper_Ryhr.selectActiveVisitorList(pageParam);
	}

	// 예약가능한 회의실 리스트 가져오기
	@Transactional(value = "ryhr_transactionManager")
	public List<CommonCodeInfo> getAvailableRoomList(MeetingInfo param) {
		return commonMapper_Ryhr.selectAvailableRoomList(param);
	}

	// 조건에 해당하는 스케쥴 리스트 조회2
	@Transactional(value = "ryhr_transactionManager")
	public List<DateTimePickerInfo> getTimetableList(MeetingInfo param) {
		return commonMapper_Ryhr.selectTimetableList(param);
	}

	// 진행 대기 내역 리스트
	@Transactional(value = "ryhr_transactionManager")
	public List<MeetingInfo> getGnbTodoList(PageParam pageParam) {
		return commonMapper_Ryhr.selectGnbTodoList(pageParam);
	}

	// 메인 스케쥴 리스트 조회
	@Transactional(value = "ryhr_transactionManager")
	public List<DateTimePickerInfo> getMainScheduleList(PageParam pageParam) {
		return commonMapper_Ryhr.selectMainScheduleList(pageParam);
	}

	// 결재 진행 요청 메일 발송
	@Transactional(value = "ryhr_transactionManager")
	public void sendConfirmMail(MeetingInfo info) {
		try {
			// 결재자 정보
			ConfirmInfo confirmInfoParam = new ConfirmInfo();
			confirmInfoParam.setDeptCode(info.getDeptCode());
			List<ConfirmInfo> confirmPersonList = commonMapper_Ryhr.selectConfirmInfoList(confirmInfoParam);

			if (confirmPersonList.size() != 0) {
				ConfirmInfo confirmInfo = confirmPersonList.get(0);
				UserInfo param = new UserInfo();
				param.setSabun(confirmInfo.getSabun());
				UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(param);
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
	@Transactional(value = "ryhr_transactionManager")
	public void sendConfirmFailMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(param);
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

	// 결재 성공 메일 발송
	@Transactional(value = "ryhr_transactionManager")
	public void sendConfirmSuccessMail(MeetingInfo info) {
		try {
			UserInfo param = new UserInfo();
			param.setSabun(info.getUpdSabun());
			UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(param);
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
	
	// 회의/방문자 등록 완료 메일 발송
	@Transactional(value = "ryhr_transactionManager")
	public void sendApplyAddMail(String seq) {
		try {
			MeetingInfo param = new MeetingInfo();
			param.setMeetingSeq(seq);
			MeetingInfo info = meetingMapper_Ryhr.selectMeeting(param);
			
			AttendantInfo attParam = new AttendantInfo();
			attParam.setMeetingSeq(seq);
			attParam.setAttendantType("01");
			List<AttendantInfo> attList1 = mainMapper_Ryhr.selectAttendantList(attParam);
			attParam.setAttendantType("02");
			List<AttendantInfo> attList2 = mainMapper_Ryhr.selectAttendantList(attParam);
			
			String emailTitle = "";
			StringBuffer emailContents = new StringBuffer();
			
			if (info.getMeetingType().equals("01")) {
				emailTitle = "관리시스템 회의 등록 알림";
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
				emailContents.append("등록 된 회의 정보가 있습니다.<br>");
				emailContents.append("확인해 주세요.<br><br>");
				emailContents.append("&nbsp;&nbsp;1) 회의제목 : ");
				emailContents.append(info.getMeetingName());
				emailContents.append("<br>&nbsp;&nbsp;2) 회의일시 : ");
				emailContents.append(info.getMeetingStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getMeetingEndDate());
				emailContents.append("<br>&nbsp;&nbsp;3) 장소 : ");
				emailContents.append(info.getCodeName());
				emailContents.append("<br>&nbsp;&nbsp;4) 참석자 : ");
				String attHuman1 = "";
				for (int i = 0; i < attList1.size(); i++) {
					AttendantInfo attendantInfo = attList1.get(i);
					attHuman1 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList1.size()-1) {
						attHuman1 += ", ";
					}
				}
				if(attHuman1.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman1);
				}
				emailContents.append("<br>&nbsp;&nbsp;5) 참고인 : ");
				String attHuman2 = "";
				for (int i = 0; i < attList2.size(); i++) {
					AttendantInfo attendantInfo = attList2.get(i);
					attHuman2 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList2.size()-1) {
						attHuman2 += ", ";
					}
				}
				if(attHuman2.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman2);
				}
				emailContents.append("<br><br>");
			} else {
				emailTitle = "관리시스템 방문 등록 알림";
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
				emailContents.append("등록 된 방문 정보가 있습니다.<br>");
				emailContents.append("확인해 주세요.<br><br>");
				emailContents.append("&nbsp;&nbsp;1) 방문목적 : ");
				emailContents.append(info.getMeetingName());
				emailContents.append("<br>&nbsp;&nbsp;2) 방문일시 : ");
				emailContents.append(info.getMeetingStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getMeetingEndDate());
				emailContents.append("<br>&nbsp;&nbsp;3) 장소 : ");
				emailContents.append(info.getCodeName());
				emailContents.append("<br>&nbsp;&nbsp;4) 방문 업체명 : ");
				emailContents.append(info.getVisitCompany());
				emailContents.append("<br>&nbsp;&nbsp;5) 방문자 성함 : ");
				emailContents.append(info.getVisitName());
				emailContents.append("<br>&nbsp;&nbsp;6) 참석자 : ");
				String attHuman1 = "";
				for (int i = 0; i < attList1.size(); i++) {
					AttendantInfo attendantInfo = attList1.get(i);
					attHuman1 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList1.size()-1) {
						attHuman1 += ", ";
					}
				}
				if(attHuman1.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman1);
				}
				emailContents.append("<br>&nbsp;&nbsp;7) 참고인 : ");
				String attHuman2 = "";
				for (int i = 0; i < attList2.size(); i++) {
					AttendantInfo attendantInfo = attList2.get(i);
					attHuman2 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList2.size()-1) {
						attHuman2 += ", ";
					}
				}
				if(attHuman2.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman2);
				}
				emailContents.append("<br><br>");
			}
			
			for (int i = 0; i < attList1.size(); i++) {
				AttendantInfo attendantInfo = attList1.get(i);
				UserInfo userParam = new UserInfo();
				userParam.setSabun(attendantInfo.getAttendantSabun());
				UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(userParam);
				String emailAddress = userInfo.getReyonMail();
				log.info("emailAddress : " + emailAddress);
				if (emailAddress != null) {
					if(!emailAddress.equals("")) {
						StringBuffer tmpContents = new StringBuffer(emailContents.toString());
						tmpContents.append(CommonUtils.shortcutLinkMaker(attendantInfo.getAttendantSabun()));
						tmpContents.append("<br><br>");
						tmpContents.append("</body>");
						tmpContents.append("</html>");
						sendNotifyEmailByGw(emailAddress, emailTitle, tmpContents);
					}
				} else {
					log.info("[sabun : " + attendantInfo.getAttendantSabun() + "] - mail address not exist");
				}
			}
			
			for (int i = 0; i < attList2.size(); i++) {
				AttendantInfo attendantInfo = attList2.get(i);
				UserInfo userParam = new UserInfo();
				userParam.setSabun(attendantInfo.getAttendantSabun());
				UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(userParam);
				String emailAddress = userInfo.getReyonMail();
				log.info("emailAddress : " + emailAddress);
				if (emailAddress != null) {
					if(!emailAddress.equals("")) {
						StringBuffer tmpContents = new StringBuffer(emailContents.toString());
						tmpContents.append(CommonUtils.shortcutLinkMaker(attendantInfo.getAttendantSabun()));
						tmpContents.append("<br><br>");
						tmpContents.append("</body>");
						tmpContents.append("</html>");
						sendNotifyEmailByGw(emailAddress, emailTitle, tmpContents);
					}
				} else {
					log.info("[sabun : " + attendantInfo.getAttendantSabun() + "] - mail address not exist");
				}
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}
	
	// 회의/방문자 수정 완료 메일 발송
	@Transactional(value = "ryhr_transactionManager")
	public void sendApplyModifyMail(String seq) {
		try {
			MeetingInfo param = new MeetingInfo();
			param.setMeetingSeq(seq);
			MeetingInfo info = meetingMapper_Ryhr.selectMeeting(param);
			
			AttendantInfo attParam = new AttendantInfo();
			attParam.setMeetingSeq(seq);
			attParam.setAttendantType("01");
			List<AttendantInfo> attList1 = mainMapper_Ryhr.selectAttendantList(attParam);
			attParam.setAttendantType("02");
			List<AttendantInfo> attList2 = mainMapper_Ryhr.selectAttendantList(attParam);
			
			String emailTitle = "";
			StringBuffer emailContents = new StringBuffer();
			
			if (info.getMeetingType().equals("01")) {
				emailTitle = "관리시스템 회의 수정 알림";
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
				emailContents.append("수정 된 회의 정보가 있습니다.<br>");
				emailContents.append("확인해 주세요.<br><br>");
				emailContents.append("&nbsp;&nbsp;1) 회의제목 : ");
				emailContents.append(info.getMeetingName());
				emailContents.append("<br>&nbsp;&nbsp;2) 회의일시 : ");
				emailContents.append(info.getMeetingStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getMeetingEndDate());
				emailContents.append("<br>&nbsp;&nbsp;3) 장소 : ");
				emailContents.append(info.getCodeName());
				emailContents.append("<br>&nbsp;&nbsp;4) 참석자 : ");
				String attHuman1 = "";
				for (int i = 0; i < attList1.size(); i++) {
					AttendantInfo attendantInfo = attList1.get(i);
					attHuman1 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList1.size()-1) {
						attHuman1 += ", ";
					}
				}
				if(attHuman1.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman1);
				}
				emailContents.append("<br>&nbsp;&nbsp;5) 참고인 : ");
				String attHuman2 = "";
				for (int i = 0; i < attList2.size(); i++) {
					AttendantInfo attendantInfo = attList2.get(i);
					attHuman2 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList2.size()-1) {
						attHuman2 += ", ";
					}
				}
				if(attHuman2.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman2);
				}
				emailContents.append("<br><br>");
			} else {
				emailTitle = "관리시스템 방문 수정 알림";
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
				emailContents.append("수정 된 방문 정보가 있습니다.<br>");
				emailContents.append("확인해 주세요.<br><br>");
				emailContents.append("&nbsp;&nbsp;1) 방문목적 : ");
				emailContents.append(info.getMeetingName());
				emailContents.append("<br>&nbsp;&nbsp;2) 방문일시 : ");
				emailContents.append(info.getMeetingStartDate());
				emailContents.append(" ~ ");
				emailContents.append(info.getMeetingEndDate());
				emailContents.append("<br>&nbsp;&nbsp;3) 장소 : ");
				emailContents.append(info.getCodeName());
				emailContents.append("<br>&nbsp;&nbsp;4) 방문 업체명 : ");
				emailContents.append(info.getVisitCompany());
				emailContents.append("<br>&nbsp;&nbsp;5) 방문자 성함 : ");
				emailContents.append(info.getVisitName());
				emailContents.append("<br>&nbsp;&nbsp;6) 참석자 : ");
				String attHuman1 = "";
				for (int i = 0; i < attList1.size(); i++) {
					AttendantInfo attendantInfo = attList1.get(i);
					attHuman1 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList1.size()-1) {
						attHuman1 += ", ";
					}
				}
				if(attHuman1.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman1);
				}
				emailContents.append("<br>&nbsp;&nbsp;7) 참고인 : ");
				String attHuman2 = "";
				for (int i = 0; i < attList2.size(); i++) {
					AttendantInfo attendantInfo = attList2.get(i);
					attHuman2 += attendantInfo.getAttendantDeptName() + " " + attendantInfo.getAttendantName();
					if(i != attList2.size()-1) {
						attHuman2 += ", ";
					}
				}
				if(attHuman2.equals("")) {
					emailContents.append("없음");
				} else {
					emailContents.append(attHuman2);
				}
				emailContents.append("<br><br>");
			}
			
			for (int i = 0; i < attList1.size(); i++) {
				AttendantInfo attendantInfo = attList1.get(i);
				UserInfo userParam = new UserInfo();
				userParam.setSabun(attendantInfo.getAttendantSabun());
				UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(userParam);
				String emailAddress = userInfo.getReyonMail();
				log.info("emailAddress : " + emailAddress);
				if (emailAddress != null) {
					if(!emailAddress.equals("")) {
						StringBuffer tmpContents = new StringBuffer(emailContents.toString());
						tmpContents.append(CommonUtils.shortcutLinkMaker(attendantInfo.getAttendantSabun()));
						tmpContents.append("<br><br>");
						tmpContents.append("</body>");
						tmpContents.append("</html>");
						sendNotifyEmailByGw(emailAddress, emailTitle, tmpContents);
					}
				} else {
					log.info("[sabun : " + attendantInfo.getAttendantSabun() + "] - mail address not exist");
				}
			}
			
			for (int i = 0; i < attList2.size(); i++) {
				AttendantInfo attendantInfo = attList2.get(i);
				UserInfo userParam = new UserInfo();
				userParam.setSabun(attendantInfo.getAttendantSabun());
				UserInfo userInfo = commonMapper_Ryhr.selectUserEmail(userParam);
				String emailAddress = userInfo.getReyonMail();
				log.info("emailAddress : " + emailAddress);
				if (emailAddress != null) {
					if(!emailAddress.equals("")) {
						StringBuffer tmpContents = new StringBuffer(emailContents.toString());
						tmpContents.append(CommonUtils.shortcutLinkMaker(attendantInfo.getAttendantSabun()));
						tmpContents.append("<br><br>");
						tmpContents.append("</body>");
						tmpContents.append("</html>");
						sendNotifyEmailByGw(emailAddress, emailTitle, tmpContents);
					}
				} else {
					log.info("[sabun : " + attendantInfo.getAttendantSabun() + "] - mail address not exist");
				}
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	// 메일 전송 (그룹웨어용)
	@Transactional(value = "gw_transactionManager")
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
		int resultCnt = commonMapper_Gw.insertGwMailInfo(info);
		if (resultCnt == 1) {
			log.info("[SUCC] [" + mto + "] [" + subject + "]");
		} else {
			log.info("[FAIL] [" + mto + "] [" + subject + "]");
		}
	}

	// 회의/방문자 관리로 보내기
	@Transactional(value = "ryhr_transactionManager")
	public int changeMeetingType(MeetingInfo param) {
		return commonMapper_Ryhr.updateChangeMeetingType(param);
	}

}
