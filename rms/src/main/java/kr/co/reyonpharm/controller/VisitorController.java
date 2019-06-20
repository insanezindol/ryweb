package kr.co.reyonpharm.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.service.MeetingService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/visitor/")
public class VisitorController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	MeetingService meetingService;

	@Autowired
	CommonService commonService;

	/* 방문자 관리 메뉴 */

	// 방문자 관리 목록
	@RequestMapping(value = "visitorList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorList(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "s_visitCompany");
		String s_visitName = StringUtil.reqNullCheckHangulUTF8(request, "s_visitName");
		String s_meetingName = StringUtil.reqNullCheckHangulUTF8(request, "s_meetingName");
		String s_deptCode = StringUtil.reqNullCheckHangulUTF8(request, "s_deptCode");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_meetingPlace = StringUtil.reqNullCheckHangulUTF8(request, "s_meetingPlace");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());

		// 사업장 코드 선택 했을경우
		String saupCookie = CommonUtils.getCookiesByRequest(request, "saupcode");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_visitCompany(s_visitCompany);
		pageParam.setS_visitName(s_visitName);
		pageParam.setS_meetingName(s_meetingName);
		pageParam.setS_deptCode(s_deptCode);
		pageParam.setS_regName(s_regName);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_meetingPlace(s_meetingPlace);
		pageParam.setS_status(s_status);
		pageParam.setMeetingType("02");

		// 결재자 정보
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setSabun(sabun);
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			// SUPERUSER가 아니면
			if (roleName.equals("ROLE_JINCHEONADMIN")) {
				// SUPER USER 이지만 본인 사업장만 권한이 있으면 해당 사업장만 보이도록 변경
				pageParam.setSaupCode("20");
			} else {
				if (deptCode.equals("1040")) {
					// 본사 (총무팀) 리스트 본사 전체보기 권한 추가
					pageParam.setSaupCode("10");
				} else if (deptCode.equals("7100") || deptCode.equals("7010")) {
					// 공장 (생산관리팀, 관리파트) 리스트 공장 전체보기 권한 추가
					pageParam.setSaupCode("20");
				} else {
					// 받은 인수 리스트
					TakeOverInfo receiveParam = new TakeOverInfo();
					receiveParam.setReceiveSabun(sabun);
					receiveParam.setStatus("AA");
					List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);
					List<String> sabunList = new ArrayList<String>();
					for (int i = 0; i < receiveList.size(); i++) {
						sabunList.add(receiveList.get(i).getGiveSabun());
					}
					sabunList.add(sabun);

					if (respon.equals("10") || respon.equals("20")) {
						// 본부장이거나 팀장이면
						DeptInfo deptInfo = new DeptInfo();
						deptInfo.setDeptParco(deptCode);
						List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);
						List<String> deptCodeList = new ArrayList<String>();
						for (int i = 0; i < deptInfoList.size(); i++) {
							deptCodeList.add(deptInfoList.get(i).getDeptCode());
						}
						for (int i = 0; i < confirmPersonList.size(); i++) {
							deptCodeList.add(confirmPersonList.get(i).getDeptCode());
						}
						deptCodeList.add(deptCode);
						// pageParam.setAttSabun(sabun);
						pageParam.setAttSabunList(sabunList);
						pageParam.setDeptCodeList(deptCodeList);
					} else {
						if (confirmPersonList.size() != 0) {
							// 결재자
							List<String> deptCodeList = new ArrayList<String>();
							for (int i = 0; i < confirmPersonList.size(); i++) {
								deptCodeList.add(confirmPersonList.get(i).getDeptCode());
							}
							deptCodeList.add(deptCode);
							// pageParam.setAttSabun(sabun);
							pageParam.setAttSabunList(sabunList);
							pageParam.setDeptCodeList(deptCodeList);
						} else {
							// 팀원
							// pageParam.setAttSabun(sabun);
							// pageParam.setSabun(sabun);
							pageParam.setSabunList(sabunList);
						}
					}
				}
			}
		}

		// 사업장 코드 설정
		if (!saupCookie.equals("")) {
			pageParam.setSaupCode(saupCookie);
		}

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = meetingService.getMeetingListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<MeetingInfo> list = meetingService.getMeetingList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("visitor/visitorList");
		return mav;
	}

	// 방문자 관리 상세
	@RequestMapping(value = "visitorView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorView(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "meetingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		MeetingInfo param = new MeetingInfo();
		param.setMeetingSeq(seq);
		MeetingInfo info = meetingService.getMeeting(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("02");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 권한 부여
		boolean isPassFlag = false;
		boolean isMineDept = false;
		boolean isMine = false;

		for (int i = 0; i < attList1.size(); i++) {
			AttendantInfo tmp = attList1.get(i);
			if (sabun.equals(tmp.getAttendantSabun())) {
				isPassFlag = true;
				break;
			}
		}

		for (int i = 0; i < attList2.size(); i++) {
			AttendantInfo tmp = attList2.get(i);
			if (sabun.equals(tmp.getAttendantSabun())) {
				isPassFlag = true;
				break;
			}
		}

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			if (respon.equals("10") || respon.equals("20")) {
				// 본부장이거나 팀장이면
				DeptInfo deptInfo = new DeptInfo();
				deptInfo.setDeptParco(deptCode);
				List<DeptInfo> deptInfoList = commonService.getTotalDeptList(deptInfo);

				for (int i = 0; i < deptInfoList.size(); i++) {
					if (deptInfoList.get(i).getDeptCode().equals(info.getDeptCode())) {
						isPassFlag = true;
						break;
					}
				}
			} else {
				// 팀원
			}
		} else {
			isPassFlag = true;
		}

		// 결재자이면 PASS
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setDeptCode(info.getDeptCode());
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		ConfirmInfo confirmPerson = new ConfirmInfo();
		for (int i = 0; i < confirmPersonList.size(); i++) {
			confirmPerson = confirmPersonList.get(i);
			if (confirmPerson.getSabun().equals(sabun)) {
				isPassFlag = true;
				isMineDept = true;
			}
		}

		// 본인인지 확인
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
			isPassFlag = true;
		}

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);
		List<String> sabunList = new ArrayList<String>();
		sabunList.add(sabun);
		for (int i = 0; i < receiveList.size(); i++) {
			sabunList.add(receiveList.get(i).getGiveSabun());
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				isPassFlag = true;
			}
		}

		for (int i = 0; i < attList1.size(); i++) {
			AttendantInfo tmp = attList1.get(i);
			for (int j = 0; j < receiveList.size(); j++) {
				if (receiveList.get(j).getGiveSabun().equals(tmp.getAttendantSabun())) {
					isPassFlag = true;
					break;
				}
			}
		}

		for (int i = 0; i < attList2.size(); i++) {
			AttendantInfo tmp = attList2.get(i);
			for (int j = 0; j < receiveList.size(); j++) {
				if (receiveList.get(j).getGiveSabun().equals(tmp.getAttendantSabun())) {
					isPassFlag = true;
					break;
				}
			}
		}

		// 진천공장인 경우 권대원 이사, 장동현 대리 PASS
		if (info.getSaupCode().equals("20")) {
			// 권대원, 장동현
			if (sabun.equals("01071801") || sabun.equals("07090303")) {
				isPassFlag = true;
			}
		}

		if (!isPassFlag) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.addObject("confirmPerson", confirmPerson);
		mav.addObject("isMine", isMine);
		mav.addObject("isMineDept", isMineDept);
		mav.setViewName("visitor/visitorView");
		return mav;
	}

	// 방문자 관리 등록
	@RequestMapping(value = "visitorAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String saupCode = SpringSecurityUtil.getSaupcode();
		String deptCode = SpringSecurityUtil.getDeptCode();

		// 결재자 정보
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setDeptCode(deptCode);
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		// 로그인 한 사용자가 결재자가 아닐 경우 참고인에 추가
		ConfirmInfo confirmInfo = new ConfirmInfo();
		for (int i = 0; i < confirmPersonList.size(); i++) {
			ConfirmInfo info = confirmPersonList.get(i);
			if (!sabun.equals(info.getSabun())) {
				confirmInfo = info;
			}
		}

		mav.addObject("sabun", sabun);
		mav.addObject("saupCode", saupCode);
		mav.addObject("confirmInfo", confirmInfo);
		mav.setViewName("visitor/visitorAdd");
		return mav;
	}

	// 방문자 관리 수정
	@RequestMapping(value = "visitorModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorModify(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "meetingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String sabun = SpringSecurityUtil.getUsername();
		String saupCode = SpringSecurityUtil.getSaupcode();

		MeetingInfo param = new MeetingInfo();
		param.setMeetingSeq(seq);
		MeetingInfo info = meetingService.getMeeting(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("02");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		// 인수인계 받은 회의 인지 검사
		for (int i = 0; i < receiveList.size(); i++) {
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				break;
			}
		}

		if (!isMine) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("saupCode", saupCode);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.setViewName("visitor/visitorModify");
		return mav;
	}

	// 방문자 관리 등록 액션
	@RequestMapping(value = "visitorAddAjax.json")
	public ModelAndView visitorAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String projectSeq = StringUtil.reqNullCheck(request, "projectSeq");
				String meetingName = StringUtil.reqNullCheckHangulUTF8(request, "meetingName");
				String meetingStartDate = StringUtil.reqNullCheck(request, "meetingStartDate");
				String meetingEndDate = StringUtil.reqNullCheck(request, "meetingEndDate");
				String meetingPlace = StringUtil.reqNullCheck(request, "meetingPlace");
				String meetingType = StringUtil.reqNullCheck(request, "meetingType");
				String meetingStatus = StringUtil.reqNullCheck(request, "meetingStatus");
				String attDeptCode1 = StringUtil.reqNullCheck(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheck(request, "attSabun1");
				String attDeptCode2 = StringUtil.reqNullCheck(request, "attDeptCode2");
				String attSabun2 = StringUtil.reqNullCheck(request, "attSabun2");
				String sendMailFlag = StringUtil.reqNullCheck(request, "sendMailFlag");

				if ("".equals(visitCompany) || "".equals(visitName) || "".equals(meetingName) || "".equals(meetingStartDate) || "".equals(meetingEndDate) || "".equals(meetingPlace)) {
					log.error("visitCompany : " + visitCompany + ", visitName : " + visitName + ", meetingName : " + meetingName + ", meetingStartDate : " + meetingStartDate + ", meetingEndDate : " + meetingEndDate + ", meetingPlace : " + meetingPlace);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 예약이 가능한지 체크
					boolean checkRevervation = true;
					if (!meetingPlace.equals("1")) {
						MeetingInfo param = new MeetingInfo();
						param.setMeetingPlace(meetingPlace);
						param.setMeetingStartDate(meetingStartDate);
						param.setMeetingEndDate(meetingEndDate);
						List<DateTimePickerInfo> checkList = mainService.getScheduleList(param);
						if (checkList.size() > 0) {
							checkRevervation = false;
						}
					}

					if (!checkRevervation) {
						mav.addObject("resultCode", CustomExceptionCodes.INVALID_RESERVATION.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.INVALID_RESERVATION.getMsg());
					} else {
						String saupCode = SpringSecurityUtil.getSaupcode();
						String deptCode = SpringSecurityUtil.getDeptCode();
						String deptName = SpringSecurityUtil.getDeptName();
						String regSabun = SpringSecurityUtil.getUsername();
						String regName = SpringSecurityUtil.getKname();

						// 사업장 코드 변경
						if (!meetingPlace.equals("1")) {
							CommonCodeInfo commonCodeInfo = new CommonCodeInfo();
							commonCodeInfo.setGrpCode("AG");
							commonCodeInfo.setCode(meetingPlace);
							List<CommonCodeInfo> commonCodeInfoList = commonService.getCommonCodeList(commonCodeInfo);
							CommonCodeInfo commonInfo = commonCodeInfoList.get(0);
							saupCode = commonInfo.getItem1();
						}

						MeetingInfo info = new MeetingInfo();
						info.setProjectSeq(projectSeq);
						info.setMeetingName(meetingName);
						info.setMeetingStartDate(meetingStartDate);
						info.setMeetingEndDate(meetingEndDate);
						info.setMeetingPlace(meetingPlace);
						info.setMeetingType(meetingType);
						info.setMeetingStatus(meetingStatus);
						info.setSaupCode(saupCode);
						info.setDeptCode(deptCode);
						info.setDeptName(deptName);
						info.setRegSabun(regSabun);
						info.setRegName(regName);
						info.setVisitCompany(visitCompany);
						info.setVisitName(visitName);

						int resultCnt = meetingService.addVisitor(info);
						log.debug("resultCnt : " + resultCnt);
						if (resultCnt == 0) {
							mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
							mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
						} else {
							String meetingSeq = String.valueOf(info.getCurrseq());
							info.setMeetingSeq(meetingSeq);

							String[] attDeptCode1Arr = attDeptCode1.split(",");
							String[] attSabun1Arr = attSabun1.split(",");
							String[] attDeptCode2Arr = attDeptCode2.split(",");
							String[] attSabun2Arr = attSabun2.split(",");

							AttendantInfo attendantInfo = new AttendantInfo();
							attendantInfo.setMeetingSeq(meetingSeq);
							attendantInfo.setRegSabun(regSabun);

							int orderSeq1 = 1;
							for (int i = 0; i < attDeptCode1Arr.length; i++) {
								// 참석자
								if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
									attendantInfo.setAttendantSabun(attSabun1Arr[i]);
									attendantInfo.setAttendantType("01");
									attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
									attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
									attendantInfo.setHiddenGb("N");
									mainService.addAttendant(attendantInfo);
								}
							}

							int orderSeq2 = 1;
							for (int i = 0; i < attDeptCode2Arr.length; i++) {
								// 참고인
								if (!attSabun2Arr[i].equals("") && !attDeptCode2Arr[i].equals("")) {
									attendantInfo.setAttendantSabun(attSabun2Arr[i]);
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept(attDeptCode2Arr[i]);
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("N");
									mainService.addAttendant(attendantInfo);
								}
							}

							// 진천공장인 경우 권대원 이사, 장동현 대리 숨은 참고인으로 넣음
							if (saupCode.equals("20")) {
								boolean insertFlag01071801 = false;
								boolean insertFlag07090303 = false;
								for (int i = 0; i < attSabun1Arr.length; i++) {
									if (attSabun1Arr[i].equals("01071801")) {
										insertFlag01071801 = true;
									}
									if (attSabun1Arr[i].equals("07090303")) {
										insertFlag07090303 = true;
									}
								}
								for (int i = 0; i < attSabun2Arr.length; i++) {
									if (attSabun2Arr[i].equals("01071801")) {
										insertFlag01071801 = true;
									}
									if (attSabun2Arr[i].equals("07090303")) {
										insertFlag07090303 = true;
									}
								}

								if (!insertFlag01071801) {
									attendantInfo.setAttendantSabun("01071801"); // 권대원
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept("7100");
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("Y");
									mainService.addAttendant(attendantInfo);
								}

								if (!insertFlag07090303) {
									attendantInfo.setAttendantSabun("07090303"); // 장동현
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept("7010");
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("Y");
									mainService.addAttendant(attendantInfo);
								}
							}
							
							// 등록 완료 안내 메일 발송
							if(sendMailFlag.equals("on")) {
								try {
									commonService.sendApplyAddMail(info.getMeetingSeq());
								} catch (Exception e) {
									log.error("visitorAddAjax 메일 발송 실패");
								}
							}

							mav.addObject("resultCode", resultCnt);
							mav.addObject("resultMsg", "success");
						}
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문자 관리 수정 액션
	@RequestMapping(value = "visitorModifyAjax.json")
	public ModelAndView visitorModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String meetingSeq = StringUtil.reqNullCheck(request, "meetingSeq");
				String projectSeq = StringUtil.reqNullCheck(request, "projectSeq");
				String meetingName = StringUtil.reqNullCheckHangulUTF8(request, "meetingName");
				String meetingStartDate = StringUtil.reqNullCheck(request, "meetingStartDate");
				String meetingEndDate = StringUtil.reqNullCheck(request, "meetingEndDate");
				String meetingPlace = StringUtil.reqNullCheck(request, "meetingPlace");
				String meetingType = StringUtil.reqNullCheck(request, "meetingType");
				String meetingStatus = StringUtil.reqNullCheck(request, "meetingStatus");
				String attDeptCode1 = StringUtil.reqNullCheck(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheck(request, "attSabun1");
				String attDeptCode2 = StringUtil.reqNullCheck(request, "attDeptCode2");
				String attSabun2 = StringUtil.reqNullCheck(request, "attSabun2");
				String sendMailFlag = StringUtil.reqNullCheck(request, "sendMailFlag");

				if ("".equals(visitCompany) || "".equals(visitName) || "".equals(meetingName) || "".equals(meetingStartDate) || "".equals(meetingEndDate) || "".equals(meetingPlace)) {
					log.error("visitCompany : " + visitCompany + ", visitName : " + visitName + ", meetingName : " + meetingName + ", meetingStartDate : " + meetingStartDate + ", meetingEndDate : " + meetingEndDate + ", meetingPlace : " + meetingPlace);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 예약이 가능한지 체크
					boolean checkRevervation = true;
					if (!meetingPlace.equals("1")) {
						MeetingInfo param = new MeetingInfo();
						param.setMeetingSeq(meetingSeq);
						param.setMeetingPlace(meetingPlace);
						param.setMeetingStartDate(meetingStartDate);
						param.setMeetingEndDate(meetingEndDate);
						List<DateTimePickerInfo> checkList = mainService.getScheduleList(param);
						if (checkList.size() > 0) {
							checkRevervation = false;
						}
					}

					if (!checkRevervation) {
						mav.addObject("resultCode", CustomExceptionCodes.INVALID_RESERVATION.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.INVALID_RESERVATION.getMsg());
					} else {
						String saupCode = SpringSecurityUtil.getSaupcode();
						String deptCode = SpringSecurityUtil.getDeptCode();
						String deptName = SpringSecurityUtil.getDeptName();
						String regSabun = SpringSecurityUtil.getUsername();
						String regName = SpringSecurityUtil.getKname();

						// 사업장 코드 변경
						if (!meetingPlace.equals("1")) {
							CommonCodeInfo commonCodeInfo = new CommonCodeInfo();
							commonCodeInfo.setGrpCode("AG");
							commonCodeInfo.setCode(meetingPlace);
							List<CommonCodeInfo> commonCodeInfoList = commonService.getCommonCodeList(commonCodeInfo);
							CommonCodeInfo commonInfo = commonCodeInfoList.get(0);
							saupCode = commonInfo.getItem1();
						}

						MeetingInfo info = new MeetingInfo();
						info.setMeetingSeq(meetingSeq);
						info.setProjectSeq(projectSeq);
						info.setMeetingName(meetingName);
						info.setMeetingStartDate(meetingStartDate);
						info.setMeetingEndDate(meetingEndDate);
						info.setMeetingPlace(meetingPlace);
						info.setMeetingType(meetingType);
						info.setMeetingStatus(meetingStatus);
						info.setSaupCode(saupCode);
						info.setDeptCode(deptCode);
						info.setDeptName(deptName);
						info.setUpdSabun(regSabun);
						info.setUpdName(regName);
						info.setVisitCompany(visitCompany);
						info.setVisitName(visitName);

						int resultCnt = meetingService.modifyVisitor(info);
						log.debug("resultCnt : " + resultCnt);
						if (resultCnt == 0) {
							mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
							mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
						} else {
							// 참석자, 참고인 삭제
							AttendantInfo attendantInfo = new AttendantInfo();
							attendantInfo.setMeetingSeq(meetingSeq);
							attendantInfo.setRegSabun(regSabun);
							attendantInfo.setAttendantType("meeting");
							mainService.deleteAttendant(attendantInfo);

							String[] attDeptCode1Arr = attDeptCode1.split(",");
							String[] attSabun1Arr = attSabun1.split(",");
							String[] attDeptCode2Arr = attDeptCode2.split(",");
							String[] attSabun2Arr = attSabun2.split(",");

							int orderSeq1 = 1;
							for (int i = 0; i < attDeptCode1Arr.length; i++) {
								// 참석자
								if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
									attendantInfo.setAttendantSabun(attSabun1Arr[i]);
									attendantInfo.setAttendantType("01");
									attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
									attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
									attendantInfo.setHiddenGb("N");
									mainService.addAttendant(attendantInfo);
								}
							}

							int orderSeq2 = 1;
							for (int i = 0; i < attDeptCode2Arr.length; i++) {
								// 참고인
								if (!attSabun2Arr[i].equals("") && !attDeptCode2Arr[i].equals("")) {
									attendantInfo.setAttendantSabun(attSabun2Arr[i]);
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept(attDeptCode2Arr[i]);
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("N");
									mainService.addAttendant(attendantInfo);
								}
							}

							// 진천공장인 경우 권대원 이사, 장동현 대리 숨은 참고인으로 넣음
							if (saupCode.equals("20")) {
								boolean insertFlag01071801 = false;
								boolean insertFlag07090303 = false;
								for (int i = 0; i < attSabun1Arr.length; i++) {
									if (attSabun1Arr[i].equals("01071801")) {
										insertFlag01071801 = true;
									}
									if (attSabun1Arr[i].equals("07090303")) {
										insertFlag07090303 = true;
									}
								}
								for (int i = 0; i < attSabun2Arr.length; i++) {
									if (attSabun2Arr[i].equals("01071801")) {
										insertFlag01071801 = true;
									}
									if (attSabun2Arr[i].equals("07090303")) {
										insertFlag07090303 = true;
									}
								}

								if (!insertFlag01071801) {
									attendantInfo.setAttendantSabun("01071801"); // 권대원
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept("7100");
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("Y");
									mainService.addAttendant(attendantInfo);
								}

								if (!insertFlag07090303) {
									attendantInfo.setAttendantSabun("07090303"); // 장동현
									attendantInfo.setAttendantType("02");
									attendantInfo.setAttendantDept("7010");
									attendantInfo.setOrderSeq(String.valueOf(orderSeq2++));
									attendantInfo.setHiddenGb("Y");
									mainService.addAttendant(attendantInfo);
								}
							}
							
							// 수정 완료 안내 메일 발송
							if(sendMailFlag.equals("on")) {
								try {
									commonService.sendApplyModifyMail(info.getMeetingSeq());
								} catch (Exception e) {
									log.error("visitorModifyAjax 메일 발송 실패");
								}
							}

							mav.addObject("resultCode", resultCnt);
							mav.addObject("resultMsg", "success");
						}
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문자 관리 삭제 액션
	@RequestMapping(value = "visitorDeleteAjax.json")
	public ModelAndView visitorDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");

				if ("".equals(seq)) {
					log.error("seq : " + seq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					AttendantInfo attInfo = new AttendantInfo();
					attInfo.setMeetingSeq(seq);
					attInfo.setAttendantType("meeting");
					int attResultCnt = mainService.deleteAttendant(attInfo);
					log.info("attResultCnt : " + attResultCnt);

					MeetingInfo info = new MeetingInfo();
					info.setMeetingSeq(seq);
					int resultCnt = meetingService.deleteMeeting(info);

					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문결과 등록
	@RequestMapping(value = "visitorResultAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorResultAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorResultAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "meetingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String sabun = SpringSecurityUtil.getUsername();

		MeetingInfo param = new MeetingInfo();
		param.setMeetingSeq(seq);
		MeetingInfo info = meetingService.getMeeting(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("02");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		// 인수인계 받은 회의 인지 검사
		for (int i = 0; i < receiveList.size(); i++) {
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				break;
			}
		}

		if (!isMine) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.setViewName("visitor/visitorResultAdd");
		return mav;
	}

	// 방문결과 수정
	@RequestMapping(value = "visitorResultModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorResultModify(HttpServletRequest request, ModelAndView mav) {
		log.info("visitorResultModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "meetingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String sabun = SpringSecurityUtil.getUsername();

		MeetingInfo param = new MeetingInfo();
		param.setMeetingSeq(seq);
		MeetingInfo info = meetingService.getMeeting(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		attParam.setAttendantType("02");
		List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		receiveParam.setStatus("AA");
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		// 인수인계 받은 회의 인지 검사
		for (int i = 0; i < receiveList.size(); i++) {
			if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
				isMine = true;
				break;
			}
		}

		if (!isMine) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("attList2", attList2);
		mav.setViewName("visitor/visitorResultModify");
		return mav;
	}

	// 방문결과 등록 액션
	@RequestMapping(value = "visitorResultAddAjax.json")
	public ModelAndView visitorResultAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorResultAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");
				String meetingContents = StringUtil.reqNullCheckHangulUTF8(request, "meetingContents");
				String decisionContents = StringUtil.reqNullCheckHangulUTF8(request, "decisionContents");
				String planContents = StringUtil.reqNullCheckHangulUTF8(request, "planContents");
				String issueContents = StringUtil.reqNullCheckHangulUTF8(request, "issueContents");

				if ("".equals(seq) || "".equals(meetingContents) || "".equals(decisionContents) || "".equals(planContents) || "".equals(issueContents)) {
					log.error("seq : " + seq + ", meetingContents : " + meetingContents + ", decisionContents : " + decisionContents + ", planContents : " + planContents + ", issueContents : " + issueContents);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String sabun = SpringSecurityUtil.getUsername();
					String name = SpringSecurityUtil.getKname();
					String saupCode = SpringSecurityUtil.getSaupcode();
					String deptCode = SpringSecurityUtil.getDeptCode();
					String respon = SpringSecurityUtil.getRespon();
					log.info("saup : " + saupCode + ", dept : " + deptCode + ", respon : " + respon);

					MeetingInfo info = new MeetingInfo();
					info.setMeetingSeq(seq);
					// 임원회의 인경우 결재 생략을 위해 바로 결재완료 처리
					MeetingInfo infoParam = new MeetingInfo();
					infoParam.setMeetingSeq(seq);
					MeetingInfo infoResult = meetingService.getMeeting(infoParam);
					if (infoResult.getMeetingName().contains("임원회의")) {
						info.setMeetingStatus("AA");
						info.setSactionSabun(sabun);
						info.setSactionName(name);
						info.setConfirmSabun(sabun);
						info.setConfirmName(name);
					} else {
						info.setMeetingStatus("03");
					}
					info.setMeetingContents(meetingContents);
					info.setDecisionContents(decisionContents);
					info.setPlanContents(planContents);
					info.setIssueContents(issueContents);
					info.setUpdSabun(sabun);
					info.setUpdName(name);

					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
					int fileIdx = 0;
					while (fileNames.hasNext()) {
						fileIdx++;
						MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
						String fileName = fileIdx + "_" + uploadfile.getOriginalFilename();

						String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DIR);
						StringBuffer fileFullPath = new StringBuffer(filePath);
						fileFullPath.append(seq);
						fileFullPath.append("/");

						File targetDir = new File(fileFullPath.toString());
						if (!targetDir.exists()) {
							targetDir.mkdirs();
						}

						log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
						File convFile = new File(fileFullPath.toString() + fileName);
						uploadfile.transferTo(convFile);

						long attachFilesize = 0;
						if (convFile.exists()) {
							attachFilesize = convFile.length();
						}

						if (fileIdx == 1) {
							info.setAttachFilepath1(fileFullPath.toString());
							info.setAttachFilename1(fileName);
							info.setAttachFilesize1(String.valueOf(attachFilesize));
						} else if (fileIdx == 2) {
							info.setAttachFilepath2(fileFullPath.toString());
							info.setAttachFilename2(fileName);
							info.setAttachFilesize2(String.valueOf(attachFilesize));
						} else if (fileIdx == 3) {
							info.setAttachFilepath3(fileFullPath.toString());
							info.setAttachFilename3(fileName);
							info.setAttachFilesize3(String.valueOf(attachFilesize));
						} else if (fileIdx == 4) {
							info.setAttachFilepath4(fileFullPath.toString());
							info.setAttachFilename4(fileName);
							info.setAttachFilesize4(String.valueOf(attachFilesize));
						} else if (fileIdx == 5) {
							info.setAttachFilepath5(fileFullPath.toString());
							info.setAttachFilename5(fileName);
							info.setAttachFilesize5(String.valueOf(attachFilesize));
						}
					}

					int resultCnt = meetingService.addMeetingResult(info);
					log.debug("resultCnt : " + resultCnt);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문결과 수정 액션
	@RequestMapping(value = "visitorResultModifyAjax.json")
	public ModelAndView visitorResultModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorResultModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");
				String meetingContents = StringUtil.reqNullCheckHangulUTF8(request, "meetingContents");
				String decisionContents = StringUtil.reqNullCheckHangulUTF8(request, "decisionContents");
				String planContents = StringUtil.reqNullCheckHangulUTF8(request, "planContents");
				String issueContents = StringUtil.reqNullCheckHangulUTF8(request, "issueContents");
				String attFileType = StringUtil.reqNullCheck(request, "attFileType");

				if ("".equals(seq) || "".equals(meetingContents) || "".equals(decisionContents) || "".equals(planContents) || "".equals(issueContents) || "".equals(attFileType)) {
					log.error("seq : " + seq + ", meetingContents : " + meetingContents + ", decisionContents : " + decisionContents + ", planContents : " + planContents + ", issueContents : " + issueContents + ", attFileType : " + attFileType);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String sabun = SpringSecurityUtil.getUsername();
					String name = SpringSecurityUtil.getKname();

					MeetingInfo info = new MeetingInfo();
					info.setMeetingSeq(seq);
					info.setMeetingStatus("03");
					info.setMeetingContents(meetingContents);
					info.setDecisionContents(decisionContents);
					info.setPlanContents(planContents);
					info.setIssueContents(issueContents);
					info.setUpdSabun(sabun);
					info.setUpdName(name);
					info.setAttFileType(attFileType);

					MeetingInfo attParam = new MeetingInfo();
					attParam.setMeetingSeq(seq);
					MeetingInfo attInfo = meetingService.getMeeting(attParam);
					String attFilepath1 = attInfo.getAttachFilepath1();
					String attFilename1 = attInfo.getAttachFilename1();
					String attFilesize1 = attInfo.getAttachFilesize1();
					String attFilepath2 = attInfo.getAttachFilepath2();
					String attFilename2 = attInfo.getAttachFilename2();
					String attFilesize2 = attInfo.getAttachFilesize2();
					String attFilepath3 = attInfo.getAttachFilepath3();
					String attFilename3 = attInfo.getAttachFilename3();
					String attFilesize3 = attInfo.getAttachFilesize3();
					String attFilepath4 = attInfo.getAttachFilepath4();
					String attFilename4 = attInfo.getAttachFilename4();
					String attFilesize4 = attInfo.getAttachFilesize4();
					String attFilepath5 = attInfo.getAttachFilepath5();
					String attFilename5 = attInfo.getAttachFilename5();
					String attFilesize5 = attInfo.getAttachFilesize5();

					if (attFileType.equals("01")) {
						// 첨부파일 변경없음
						if (attFilepath1 != null) {
							info.setAttachFilepath1(attFilepath1);
						}
						if (attFilename1 != null) {
							info.setAttachFilename1(attFilename1);
						}
						if (attFilesize1 != null) {
							info.setAttachFilesize1(attFilesize1);
						}
						if (attFilepath2 != null) {
							info.setAttachFilepath2(attFilepath2);
						}
						if (attFilename2 != null) {
							info.setAttachFilename2(attFilename2);
						}
						if (attFilesize2 != null) {
							info.setAttachFilesize2(attFilesize2);
						}
						if (attFilepath3 != null) {
							info.setAttachFilepath3(attFilepath3);
						}
						if (attFilename3 != null) {
							info.setAttachFilename3(attFilename3);
						}
						if (attFilesize3 != null) {
							info.setAttachFilesize3(attFilesize3);
						}
						if (attFilepath4 != null) {
							info.setAttachFilepath4(attFilepath4);
						}
						if (attFilename4 != null) {
							info.setAttachFilename4(attFilename4);
						}
						if (attFilesize4 != null) {
							info.setAttachFilesize4(attFilesize4);
						}
						if (attFilepath5 != null) {
							info.setAttachFilepath5(attFilepath5);
						}
						if (attFilename5 != null) {
							info.setAttachFilename5(attFilename5);
						}
						if (attFilesize5 != null) {
							info.setAttachFilesize5(attFilesize5);
						}
					} else if (attFileType.equals("02")) {
						// 새로운 첨부파일 사용
						if (attFilepath1 != null) {
							FileIOUtils.deleteAllFiles(attFilepath1);
						}
						if (attFilepath2 != null) {
							FileIOUtils.deleteAllFiles(attFilepath2);
						}
						if (attFilepath3 != null) {
							FileIOUtils.deleteAllFiles(attFilepath3);
						}
						if (attFilepath4 != null) {
							FileIOUtils.deleteAllFiles(attFilepath4);
						}
						if (attFilepath5 != null) {
							FileIOUtils.deleteAllFiles(attFilepath5);
						}

						MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
						java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
						int fileIdx = 0;
						while (fileNames.hasNext()) {
							fileIdx++;
							MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
							String fileName = fileIdx + "_" + uploadfile.getOriginalFilename();

							String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DIR);
							StringBuffer fileFullPath = new StringBuffer(filePath);
							fileFullPath.append(seq);
							fileFullPath.append("/");

							File targetDir = new File(fileFullPath.toString());
							if (!targetDir.exists()) {
								targetDir.mkdirs();
							}

							log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
							File convFile = new File(fileFullPath.toString() + fileName);
							uploadfile.transferTo(convFile);

							long attachFilesize = 0;
							if (convFile.exists()) {
								attachFilesize = convFile.length();
							}

							if (fileIdx == 1) {
								info.setAttachFilepath1(fileFullPath.toString());
								info.setAttachFilename1(fileName);
								info.setAttachFilesize1(String.valueOf(attachFilesize));
							} else if (fileIdx == 2) {
								info.setAttachFilepath2(fileFullPath.toString());
								info.setAttachFilename2(fileName);
								info.setAttachFilesize2(String.valueOf(attachFilesize));
							} else if (fileIdx == 3) {
								info.setAttachFilepath3(fileFullPath.toString());
								info.setAttachFilename3(fileName);
								info.setAttachFilesize3(String.valueOf(attachFilesize));
							} else if (fileIdx == 4) {
								info.setAttachFilepath4(fileFullPath.toString());
								info.setAttachFilename4(fileName);
								info.setAttachFilesize4(String.valueOf(attachFilesize));
							} else if (fileIdx == 5) {
								info.setAttachFilepath5(fileFullPath.toString());
								info.setAttachFilename5(fileName);
								info.setAttachFilesize5(String.valueOf(attachFilesize));
							}
						}
					} else if (attFileType.equals("03")) {
						// 현재 첨부파일 삭제
						if (attFilepath1 != null) {
							FileIOUtils.deleteAllFiles(attFilepath1);
						}
						if (attFilepath2 != null) {
							FileIOUtils.deleteAllFiles(attFilepath2);
						}
						if (attFilepath3 != null) {
							FileIOUtils.deleteAllFiles(attFilepath3);
						}
						if (attFilepath4 != null) {
							FileIOUtils.deleteAllFiles(attFilepath4);
						}
						if (attFilepath5 != null) {
							FileIOUtils.deleteAllFiles(attFilepath5);
						}
					}

					int resultCnt = meetingService.modifyMeetingResult(info);
					log.debug("resultCnt : " + resultCnt);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문결과 삭제 액션
	@RequestMapping(value = "visitorResultDeleteAjax.json")
	public ModelAndView visitorResultDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorResultDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");

				if ("".equals(seq)) {
					log.error("seq : " + seq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String sabun = SpringSecurityUtil.getUsername();
					String name = SpringSecurityUtil.getKname();

					MeetingInfo info = new MeetingInfo();
					info.setMeetingSeq(seq);
					MeetingInfo attInfo = meetingService.getMeeting(info);
					String attFilepath1 = attInfo.getAttachFilepath1();

					if (attFilepath1 != null) {
						FileIOUtils.deleteAllFiles(attFilepath1);
					}

					info.setMeetingStatus("02");
					info.setUpdSabun(sabun);
					info.setUpdName(name);
					int resultCnt = meetingService.deleteMeetingResult(info);
					log.debug("resultCnt : " + resultCnt);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 방문결과 확인 액션
	@RequestMapping(value = "visitorResultConfirmAjax.json")
	public ModelAndView visitorResultConfirmAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("visitorResultConfirmAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");
				String comment = StringUtil.reqNullCheckHangulUTF8(request, "comment");
				String meetingStatus = StringUtil.reqNullCheck(request, "meetingStatus");

				if ("".equals(seq) && "".equals(meetingStatus)) {
					log.error("seq : " + seq + ", meetingStatus : " + meetingStatus);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String sabun = SpringSecurityUtil.getUsername();
					String kname = SpringSecurityUtil.getKname();
					String deptCode = SpringSecurityUtil.getDeptCode();

					MeetingInfo param = new MeetingInfo();
					param.setMeetingSeq(seq);
					MeetingInfo info = meetingService.getMeeting(param);

					// 결재자 정보
					boolean isMineDept = false;

					// 결재자이면 PASS
					ConfirmInfo confirmInfoParam = new ConfirmInfo();
					confirmInfoParam.setDeptCode(info.getDeptCode());
					List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

					ConfirmInfo confirmPerson = new ConfirmInfo();
					for (int i = 0; i < confirmPersonList.size(); i++) {
						confirmPerson = confirmPersonList.get(i);
						if (confirmPerson.getSabun().equals(sabun)) {
							isMineDept = true;
						}
					}

					// 받은 인수 리스트
					TakeOverInfo receiveParam = new TakeOverInfo();
					receiveParam.setReceiveSabun(sabun);
					receiveParam.setStatus("AA");
					List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

					// 내가 등록한 회의 인지 검사
					boolean isMine = false;
					if (sabun.equals(info.getRegSabun())) {
						isMine = true;
					}

					// 인수인계 받은 회의 인지 검사
					for (int i = 0; i < receiveList.size(); i++) {
						if (receiveList.get(i).getGiveSabun().equals(info.getRegSabun())) {
							isMine = true;
							break;
						}
					}

					// 본인이거나 물려 받았거나 결재자이면
					if (isMine || isMineDept) {
						param.setMeetingStatus(meetingStatus);
						if (meetingStatus.equals("04")) {
							// 상신
							param.setSactionSabun(sabun);
							param.setSactionName(kname);
							param.setSactionComment(comment);
						} else if (meetingStatus.equals("99")) {
							// 반려
							param.setReturnSabun(sabun);
							param.setReturnName(kname);
							param.setReturnComment(comment);
						} else if (meetingStatus.equals("AA")) {
							// 결재
							param.setConfirmSabun(sabun);
							param.setConfirmName(kname);
							param.setConfirmComment(comment);
						}

						int resultCnt = meetingService.confirmMeetingResult(param);
						log.debug("resultCnt : " + resultCnt);

						String serverType = Constants.configProp.getProperty(Constants.SERVER_TYPE);
						// 상신인 경우 결재자에게 메일 발송 (상용 환경인 경우만 발송)
						if (serverType.equals("real")) {
							MeetingInfo result = meetingService.getMeeting(param);
							if (meetingStatus.equals("04")) {
								commonService.sendConfirmMail(result);
							} else if (meetingStatus.equals("99")) {
								commonService.sendConfirmFailMail(result);
							} else if (meetingStatus.equals("AA")) {
								commonService.sendConfirmSuccessMail(result);
							}
						}

						mav.addObject("resultCode", resultCnt);
						mav.addObject("resultMsg", "success");
					} else {
						log.error("sabun : " + sabun + ", dept : " + deptCode);
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					}
				}
			} else {
				log.error("auth : " + auth);
				mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return mav;
	}

	// 파일 다운로드
	@RequestMapping(value = "visitorFileDownload.do")
	public ModelAndView visitorFileDownload(HttpServletRequest request) {
		log.info("/visitorFileDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String auth = StringUtil.reqNullCheck(request, "dwAuth");
		String seq = StringUtil.reqNullCheck(request, "dwSeq");
		String filename = StringUtil.reqNullCheckHangulUTF8(request, "dwFilename");

		ModelAndView mav = new ModelAndView("downloadView");

		if (auth.equals("reyon")) {
			MeetingInfo param = new MeetingInfo();
			param.setMeetingSeq(seq);
			MeetingInfo info = meetingService.getMeeting(param);

			if (!filename.equals(info.getAttachFilename1()) && !filename.equals(info.getAttachFilename2()) && !filename.equals(info.getAttachFilename3()) && !filename.equals(info.getAttachFilename4()) && !filename.equals(info.getAttachFilename5())) {
				log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
				throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
			}

			String filePath = "";
			if (filename.equals(info.getAttachFilename1())) {
				filePath = info.getAttachFilepath1() + info.getAttachFilename1();
			} else if (filename.equals(info.getAttachFilename2())) {
				filePath = info.getAttachFilepath2() + info.getAttachFilename2();
			} else if (filename.equals(info.getAttachFilename3())) {
				filePath = info.getAttachFilepath3() + info.getAttachFilename3();
			} else if (filename.equals(info.getAttachFilename4())) {
				filePath = info.getAttachFilepath4() + info.getAttachFilename4();
			} else if (filename.equals(info.getAttachFilename5())) {
				filePath = info.getAttachFilepath5() + info.getAttachFilename5();
			}

			if (filePath == null || filePath.equals("")) {
				log.error(CustomExceptionCodes.FILE_NOT_EXIST.getMsg());
				throw new CustomException(CustomExceptionCodes.FILE_NOT_EXIST);
			}

			mav.addObject("filePath", filePath);

		} else {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		return mav;
	}

	// 방문자 엑셀 다운로드
	@RequestMapping(value = "visitorExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitorExcelDownload(HttpServletRequest request) {
		log.info("visitorExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("meetingExcelView");
		String seq = StringUtil.reqNullCheck(request, "seq");

		if (seq.equals("")) {
			log.error("seq : " + seq);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		MeetingInfo param = new MeetingInfo();
		param.setMeetingSeq(seq);
		MeetingInfo info = meetingService.getMeeting(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		String fileStr = "";
		if (info.getAttachFilename1() != null) {
			fileStr += info.getAttachFilename1();
		}
		if (info.getAttachFilename2() != null) {
			fileStr += "\r\n" + info.getAttachFilename2();
		}
		if (info.getAttachFilename3() != null) {
			fileStr += "\r\n" + info.getAttachFilename3();
		}
		if (info.getAttachFilename4() != null) {
			fileStr += "\r\n" + info.getAttachFilename4();
		}
		if (info.getAttachFilename5() != null) {
			fileStr += "\r\n" + info.getAttachFilename5();
		}

		AttendantInfo attParam = new AttendantInfo();
		attParam.setMeetingSeq(seq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = mainService.getAttendantList(attParam);
		String attStr = "";
		for (int i = 0; i < attList1.size(); i++) {
			AttendantInfo tmp = attList1.get(i);
			attStr += tmp.getAttendantName();
			if (i != attList1.size() - 1) {
				attStr += ", ";
			}
		}

		// attParam.setAttendantType("02");
		// List<AttendantInfo> attList2 = mainService.getAttendantList(attParam);

		String fileName = "회의록";
		String templateFileName = "/meeting.xlsx";

		mav.addObject("fileName", fileName);
		mav.addObject("templateFileName", templateFileName);
		mav.addObject("info", info);
		mav.addObject("attStr", attStr);
		mav.addObject("fileStr", fileStr);
		// mav.addObject("attList1", attList1);
		// mav.addObject("attList2", attList2);
		return mav;
	}

}
