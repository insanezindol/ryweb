package kr.co.reyonpharm.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.reyonpharm.mapper.gwif.HRMapper_Gwif;
import kr.co.reyonpharm.mapper.ryhr.HRMapper_Ryhr;
import kr.co.reyonpharm.models.ApproInfo;
import kr.co.reyonpharm.models.HolidayInfo;
import kr.co.reyonpharm.models.OvertimeInfo;
import kr.co.reyonpharm.models.SalaryInfo;
import kr.co.reyonpharm.models.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("hRService")
public class HRService {

	@Autowired
	private HRMapper_Ryhr hRMapper_Ryhr;
	
	@Autowired
	private HRMapper_Gwif hRMapper_Gwif;

	// 영업부 직원인지 확인 (의원본부, 병원본부, 도매본부)
	public int isSalesCheck(UserInfo param) {
		return hRMapper_Ryhr.selectSalesCheck(param);
	}
	
	// 급여 전체 구분 가져오기
	public List<SalaryInfo> getTotalPayGb(SalaryInfo param) {
		return hRMapper_Ryhr.selectTotalPayGb(param);
	}
	
	// 급여 전체 년월 가져오기
	public List<SalaryInfo> getTotalPayDate(SalaryInfo param) {
		return hRMapper_Ryhr.selectTotalPayDate(param);
	}

	// 급여 조회
	public SalaryInfo getSalaryInfo(SalaryInfo param) {
		return hRMapper_Ryhr.selectSalaryInfo(param);
	}

	// 조회년도 불러오기 
	public List<HolidayInfo> getHolidayYYMM(HolidayInfo param) {
		return hRMapper_Ryhr.selectHolidayYYMM(param);
	}
	
	// 휴가정보 불러오기
	public HolidayInfo getHolidayInfo(HolidayInfo param) {
		return hRMapper_Ryhr.selectHolidayInfo(param);
	}

	// 휴가내역 불러오기
	public List<HolidayInfo> getHolidayList(HolidayInfo param) {
		return hRMapper_Ryhr.selectHolidayList(param);
	}

	// 휴가 마스터 불러오기
	public List<HolidayInfo> getHolidayMasterList(HolidayInfo param) {
		return hRMapper_Ryhr.selectHolidayMasterList(param);
	}

	// 휴가 마스터 등록하기
	public int addHolidayMaster(HolidayInfo param) {
		return hRMapper_Ryhr.insertHolidayMaster(param);
	}

	// 휴가 마스터 상세 불러오기
	public List<HolidayInfo> getHolidayMasterDetailList(HolidayInfo param) {
		return hRMapper_Ryhr.selectHolidayMasterDetailList(param);
	}

	// 휴가내역 등록하기
	public int addMIS003(HolidayInfo param) {
		return hRMapper_Ryhr.insertMIS003(param);
	}
	
	// 전자결재 등록하기
	public int addApproInfo(ApproInfo param) {
		return hRMapper_Gwif.insertApproInfo(param);
	}
	
	// 초과근무정보 불러오기
	public OvertimeInfo getOvertimeInfo(OvertimeInfo param) {
		return hRMapper_Ryhr.selectOvertimeInfo(param);
	}

	// 초과근무리스트 불러오기
	public List<OvertimeInfo> getOvertimeList(OvertimeInfo param) {
		return hRMapper_Ryhr.selectOvertimeList(param);
	}
	
	// 근무자 정보 불러오기
	public List<OvertimeInfo> getUserinfoList(OvertimeInfo param) {
		return hRMapper_Ryhr.selectUserinfoList(param);
	}
	
	// 초과근무내역 등록하기
	public int addMIS004(OvertimeInfo param) {
		return hRMapper_Ryhr.insertMIS004(param);
	}
	
	// 초과근무자 등록하기
	public int addMIS004Worker(OvertimeInfo param) {
		return hRMapper_Ryhr.insertMIS004Worker(param);
	}
	
	// 초과근무 마스터 불러오기
	public List<OvertimeInfo> getOvertimeMasterList(OvertimeInfo param) {
		return hRMapper_Ryhr.selectOvertimeMasterList(param);
	}

	// 휴가 신청서 전자결재 HTML 빌드
	public String buildMIS003Html(HolidayInfo info) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        Calendar c1 = Calendar.getInstance();
        String today = sdf.format(c1.getTime());
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 700px; border-collapse: collapse; font-size: 20pt;\">");
		sb.append("<tr>");
		sb.append("<td style=\"text-align: center; font-weight: bold;\">휴가 신청서</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<div style=\"width: 700px; height: 30px;\"></div>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 700px; border-collapse: collapse; font-size: 10pt; border: 2px solid black;\">");
		sb.append("<colgroup>");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 140px;\">");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 130px;\">");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 130px;\">");
		sb.append("</colgroup>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">소 속</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getDeptName());
		sb.append("</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">직 위</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getPosLog());
		sb.append("</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">성 명</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getKname());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">분 류</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getHolidayGbnTxt());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">일자/기간</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getStartdate());
		sb.append(" ~ ");
		sb.append(info.getEnddate());
		sb.append(" ( ");
		sb.append(info.getViewMinusCnt());
		sb.append("일간 )");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">사유</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getReason());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">업무인계자</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getTakeover());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<div style=\"width: 700px; height: 30px;\"></div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px; margin-bottom: 5px;\">■ <span style=\"font-weight:bold;\">특별휴가</span> : 취업규칙 제35조에 따른 경조휴가 및 공가 (공민권 행사 및 징병검사 및 단기소집)</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px;\">■ <span style=\"font-weight:bold;\">장기근속휴가</span></div>");
		sb.append("<div style=\"width: 662px; font-size: 10pt; margin-left: 38px; margin-bottom: 5px;\">5년근속–2일, 10년근속–3일, 15년근속–4일, 20년근속–5일, 25년근속–5일, 30년근속–5일 (미사용 보상X)</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px; margin-bottom: 20px;\">■ <span style=\"font-weight:bold;\">병가</span> : 업무 외 재해 시 진단서 첨부하여 신청. 연 30일 한도- 최초 3일 유급, 3일 초과일수 무급.</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px;\">※ 경조비 등 경조사 지원은 사내근로복지기금 운영규정에 의해 별도 사내근로복지기금 신청을 통하여</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 34px;\">지급이 이루어집니다. (휴가신청과 별도)</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px; margin-bottom: 30px; color:red;\">※ 휴가 신청일 1일 전까지는 최종 결재를 득하여 주시기 바랍니다. </div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:center; margin-bottom: 30px;\">위와 같은 사유로 휴가신청서를 제출합니다.</div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:right;margin-bottom: 10px;\">");
		sb.append(today);
		sb.append("</div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:right;\">신 청 인 : ");
		sb.append(info.getKname());
		sb.append(" (인)</div>");
		sb.append("<div style=\"width: 700px; text-align:right; margin-top: 30px;\">");
		sb.append("<div><img src=\"http://rygw.reyonpharm.co.kr/images/reyon_logo.gif\" style=\"width: 120px;\"></div>");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");

		return sb.toString();
	}

	// 초과 근무 신청서 전자결재 HTML 빌드
	public String buildMIS004Html(OvertimeInfo info) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        Calendar c1 = Calendar.getInstance();
        String today = sdf.format(c1.getTime());
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 700px; border-collapse: collapse; font-size: 20pt;\">");
		sb.append("<tr>");
		sb.append("<td style=\"text-align: center; font-weight: bold;\">초과근무신청서</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<div style=\"width: 700px; height: 30px;\"></div>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 700px; border-collapse: collapse; font-size: 10pt; border: 2px solid black;\">");
		sb.append("<colgroup>");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 140px;\">");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 130px;\">");
		sb.append("<col style=\"width: 100px;\">");
		sb.append("<col style=\"width: 130px;\">");
		sb.append("</colgroup>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">소 속</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getDeptName());
		sb.append("</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">직 위</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getPosLog());
		sb.append("</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">성 명</td>");
		sb.append("<td style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getKname());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">근무구분</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getOvertimeGbnTxt());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">일자/시간</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\"> 근무시간 : ");
		sb.append(info.getStartdate());
		sb.append(" ~ ");
		sb.append(info.getEnddate());
		if(!info.getRestStarttime1().equals("") && !info.getRestEndtime1().equals("")) {
			sb.append("<br>휴게시간(1) : ");
			sb.append(info.getRestStarttime1());
			sb.append(" ~ ");
			sb.append(info.getRestEndtime1());
		}
		if(!info.getRestStarttime2().equals("") && !info.getRestEndtime2().equals("")) {
			sb.append("<br>휴게시간(2) : ");
			sb.append(info.getRestStarttime2());
			sb.append(" ~ ");
			sb.append(info.getRestEndtime2());
		}
		if(!info.getRestStarttime3().equals("") && !info.getRestEndtime3().equals("")) {
			sb.append("<br>휴게시간(3) : ");
			sb.append(info.getRestStarttime3());
			sb.append(" ~ ");
			sb.append(info.getRestEndtime3());
		}
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">근무자</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		String[] workerDeptNameArr = info.getWorkerDeptNameArr();
		String[] workerSabunArr = info.getWorkerSabunArr();
		String[] workerKnameArr = info.getWorkerKnameArr();
		String[] workerPosLogArr = info.getWorkerPosLogArr();
		for (int i = 0; i < workerSabunArr.length; i++) {
			String worker = workerDeptNameArr[i] + " " +  workerKnameArr[i] + " " + workerPosLogArr[i];
			if(i != 0) {
				sb.append(", ");
			}
			sb.append(worker);
		}
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">사유</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getReason());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style=\"height: 50px;\">");
		sb.append("<td style=\"border: 1px solid black; text-align: center; font-weight: bold; background-color: #EFEFEF;\">증빙자료</td>");
		sb.append("<td colspan=\"5\" style=\"border: 1px solid black; text-align: center;\">");
		sb.append(info.getEvidence());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<div style=\"width: 700px; height: 30px;\"></div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px;\"><span style=\"font-weight:bold; color:red;\">※ 본 신청서는 신청 당일 17시 전까지 소속 부서장 결재를 득한 후 인사팀 최종 결재를 득하여야 하며,</span></div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 35px; margin-bottom: 5px;\"><span style=\"font-weight:bold; color:red;\">사전 승인을 받지 아니한 연장/휴일/야간근로에 대하여는 근로시간으로 인정하지 않습니다.</span></div>");
		sb.append("<div style=\"width: 662px; font-size: 10pt; margin-left: 20px;\">- 결재라인은 \"신청인 본인 → 소속 부서장(팀장)\" 합의라인은 \"인사팀 유동희 선임 → 인사팀 최승환 수석\"으로</div>");
		sb.append("<div style=\"width: 662px; font-size: 10pt; margin-left: 31px; margin-bottom: 10px;\">지정해주시기 바랍니다.</div>");
		sb.append("<div style=\"width: 680px; font-size: 10pt; margin-left: 20px; margin-bottom: 30px;\"><span style=\"font-weight:bold;\">※ 본사의 경우 본 신청서 제출 시 별도 사무실 조명연장신청은 필요하지 않습니다.</span></div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:center; margin-bottom: 30px;\">위와 같은 사유로 초과근무신청서를 제출합니다.</div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:right;margin-bottom: 10px;\">");
		sb.append(today);
		sb.append("</div>");
		sb.append("<div style=\"width: 700px; font-size: 10pt; text-align:right;\">신 청 인 : ");
		sb.append(info.getKname());
		sb.append(" (인)</div>");
		sb.append("<div style=\"width: 700px; text-align:right; margin-top: 30px;\">");
		sb.append("<div><img src=\"http://rygw.reyonpharm.co.kr/images/reyon_logo.gif\" style=\"width: 120px;\"></div>");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		
		return sb.toString();
	}

}
