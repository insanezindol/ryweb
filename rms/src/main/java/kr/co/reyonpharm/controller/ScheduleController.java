package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.AttendantInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.DateTimePickerInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ScheduleInfo;
import kr.co.reyonpharm.service.ScheduleService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedule/")
public class ScheduleController extends CustomExceptionHandler {

	@Autowired
	ScheduleService scheduleService;

	/* 일정 관리 */
	
	// 일정 관리 목록
	@RequestMapping(value = "scheduleList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView scheduleList(HttpServletRequest request, ModelAndView mav) {
		log.info("scheduleList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_scheduleType = StringUtil.reqNullCheckHangulUTF8(request, "s_scheduleType");
		String s_scheduleName = StringUtil.reqNullCheckHangulUTF8(request, "s_scheduleName");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_remark = StringUtil.reqNullCheckHangulUTF8(request, "s_remark");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_scheduleType(s_scheduleType);
		pageParam.setS_scheduleName(s_scheduleName);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_remark(s_remark);
		pageParam.setS_regName(s_regName);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = scheduleService.getScheduleListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ScheduleInfo> list = scheduleService.getScheduleList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		
		mav.setViewName("schedule/scheduleList");
		return mav;
	}
	
	// 일정 관리 보기
	@RequestMapping(value = "scheduleView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView scheduleView(HttpServletRequest request, ModelAndView mav) {
		log.info("scheduleView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		String scheduleSeq = StringUtil.reqNullCheck(request, "scheduleSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(scheduleSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ScheduleInfo param = new ScheduleInfo();
		param.setScheduleSeq(scheduleSeq);
		ScheduleInfo info = scheduleService.getSchedule(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}
		
		// 참석/동행자 정보
		AttendantInfo attParam = new AttendantInfo();
		attParam.setScheduleSeq(scheduleSeq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = scheduleService.getAttendantList(attParam);

		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		mav.addObject("scheduleSeq", scheduleSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.addObject("isMine", isMine);
		mav.setViewName("schedule/scheduleView");
		return mav;
	}
	
	// 일정 관리 등록
	@RequestMapping(value = "scheduleAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView scheduleAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("scheduleAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("schedule/scheduleAdd");
		return mav;
	}
	
	// 일정 관리 수정
	@RequestMapping(value = "scheduleModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView scheduleModify(HttpServletRequest request, ModelAndView mav) {
		log.info("scheduleModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		String scheduleSeq = StringUtil.reqNullCheck(request, "scheduleSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(scheduleSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}
		
		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ScheduleInfo param = new ScheduleInfo();
		param.setScheduleSeq(scheduleSeq);
		ScheduleInfo info = scheduleService.getSchedule(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}
		
		// 참석/동행자 정보
		AttendantInfo attParam = new AttendantInfo();
		attParam.setScheduleSeq(scheduleSeq);
		attParam.setAttendantType("01");
		List<AttendantInfo> attList1 = scheduleService.getAttendantList(attParam);
		
		if (!sabun.equals(info.getRegSabun())) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("scheduleSeq", scheduleSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("attList1", attList1);
		mav.setViewName("schedule/scheduleModify");
		return mav;
	}
	
	// 일정 관리 등록 액션
	@RequestMapping(value = "scheduleAddAjax.json")
	public ModelAndView scheduleAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("scheduleAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String scheduleType = StringUtil.reqNullCheckHangulUTF8(request, "scheduleType");
				String scheduleName = StringUtil.reqNullCheckHangulUTF8(request, "scheduleName");
				String scheduleStarttime = StringUtil.reqNullCheckHangulUTF8(request, "scheduleStarttime");
				String scheduleEndtime = StringUtil.reqNullCheckHangulUTF8(request, "scheduleEndtime");
				String scheduleRemark = StringUtil.reqNullCheckHangulUTF8(request, "scheduleRemark");
				String scheduleStatus = StringUtil.reqNullCheckHangulUTF8(request, "scheduleStatus");
				String attDeptCode1 = StringUtil.reqNullCheckHangulUTF8(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheckHangulUTF8(request, "attSabun1");

				if ("".equals(scheduleType) || "".equals(scheduleName) || "".equals(scheduleStarttime) || "".equals(scheduleEndtime) || "".equals(scheduleStatus)) {
					log.error("scheduleType : " + scheduleType + ", scheduleName : " + scheduleName + ", scheduleStarttime : " + scheduleStarttime + ", scheduleEndtime : " + scheduleEndtime + ", scheduleStatus : " + scheduleStatus);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					ScheduleInfo info = new ScheduleInfo();
					info.setScheduleType(scheduleType);
					info.setScheduleName(scheduleName);
					info.setScheduleStarttime(scheduleStarttime);
					info.setScheduleEndtime(scheduleEndtime);
					info.setScheduleRemark(scheduleRemark);
					info.setScheduleStatus(scheduleStatus);
					info.setRegSabun(regSabun);
					info.setRegName(regName);
					info.setUpdSabun(regSabun);
					info.setUpdName(regName);
					
					int resultCnt = scheduleService.addSchedule(info);
					
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
					} else {
						String scheduleSeq = String.valueOf(info.getCurrseq());
						info.setScheduleSeq(scheduleSeq);

						String[] attDeptCode1Arr = attDeptCode1.split(",");
						String[] attSabun1Arr = attSabun1.split(",");

						AttendantInfo attendantInfo = new AttendantInfo();
						attendantInfo.setScheduleSeq(scheduleSeq);
						attendantInfo.setRegSabun(regSabun);

						int orderSeq1 = 1;
						for (int i = 0; i < attDeptCode1Arr.length; i++) {
							// 참석/동행자
							if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun1Arr[i]);
								attendantInfo.setAttendantType("01");
								attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
								attendantInfo.setHiddenGb("N");
								scheduleService.addAttendant(attendantInfo);
							}
						}
						
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

	// 일정 관리 수정 액션
	@RequestMapping(value = "scheduleModifyAjax.json")
	public ModelAndView scheduleModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("scheduleModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String scheduleSeq = StringUtil.reqNullCheck(request, "scheduleSeq");
				String scheduleType = StringUtil.reqNullCheckHangulUTF8(request, "scheduleType");
				String scheduleName = StringUtil.reqNullCheckHangulUTF8(request, "scheduleName");
				String scheduleStarttime = StringUtil.reqNullCheckHangulUTF8(request, "scheduleStarttime");
				String scheduleEndtime = StringUtil.reqNullCheckHangulUTF8(request, "scheduleEndtime");
				String scheduleRemark = StringUtil.reqNullCheckHangulUTF8(request, "scheduleRemark");
				String scheduleStatus = StringUtil.reqNullCheckHangulUTF8(request, "scheduleStatus");
				String attDeptCode1 = StringUtil.reqNullCheck(request, "attDeptCode1");
				String attSabun1 = StringUtil.reqNullCheck(request, "attSabun1");

				if ("".equals(scheduleSeq) || "".equals(scheduleType) || "".equals(scheduleName) || "".equals(scheduleStarttime) || "".equals(scheduleEndtime) || "".equals(scheduleStatus)) {
					log.error("scheduleSeq : " + scheduleSeq + ", scheduleType : " + scheduleType + ", scheduleName : " + scheduleName + ", scheduleStarttime : " + scheduleStarttime + ", scheduleEndtime : " + scheduleEndtime + ", scheduleStatus : " + scheduleStatus);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String updSabun = SpringSecurityUtil.getUsername();
					String updName = SpringSecurityUtil.getKname();

					ScheduleInfo info = new ScheduleInfo();
					info.setScheduleSeq(scheduleSeq);
					info.setScheduleType(scheduleType);
					info.setScheduleName(scheduleName);
					info.setScheduleStarttime(scheduleStarttime);
					info.setScheduleEndtime(scheduleEndtime);
					info.setScheduleRemark(scheduleRemark);
					info.setScheduleStatus(scheduleStatus);
					info.setUpdSabun(updSabun);
					info.setUpdName(updName);

					int resultCnt = scheduleService.modifySchedule(info);

					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						// 참석자, 참고인 삭제
						AttendantInfo attendantInfo = new AttendantInfo();
						attendantInfo.setScheduleSeq(scheduleSeq);
						attendantInfo.setRegSabun(updSabun);
						scheduleService.deleteAttendant(attendantInfo);

						String[] attDeptCode1Arr = attDeptCode1.split(",");
						String[] attSabun1Arr = attSabun1.split(",");

						int orderSeq1 = 1;
						for (int i = 0; i < attDeptCode1Arr.length; i++) {
							// 참석자
							if (!attSabun1Arr[i].equals("") && !attDeptCode1Arr[i].equals("")) {
								attendantInfo.setAttendantSabun(attSabun1Arr[i]);
								attendantInfo.setAttendantType("01");
								attendantInfo.setAttendantDept(attDeptCode1Arr[i]);
								attendantInfo.setOrderSeq(String.valueOf(orderSeq1++));
								attendantInfo.setHiddenGb("N");
								scheduleService.addAttendant(attendantInfo);
							}
						}
						
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

	// 일정 관리 삭제 액션
	@RequestMapping(value = "scheduleDeleteAjax.json")
	public ModelAndView scheduleDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("scheduleDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String scheduleSeq = StringUtil.reqNullCheck(request, "scheduleSeq");

				if ("".equals(scheduleSeq)) {
					log.error("scheduleSeq : " + scheduleSeq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 참석자, 참고인 삭제
					AttendantInfo attendantInfo = new AttendantInfo();
					attendantInfo.setScheduleSeq(scheduleSeq);
					int attResultCnt = scheduleService.deleteAttendant(attendantInfo);
					log.info("attResultCnt : " + attResultCnt);
					
					ScheduleInfo info = new ScheduleInfo();
					info.setScheduleSeq(scheduleSeq);

					int resultCnt = scheduleService.deleteSchedule(info);

					if (resultCnt == 0) {
						log.error("resultCnt : " + resultCnt);
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
	
	// 일정 관리 팝업
	@RequestMapping(value = "schedulePopup.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView schedulePopup(HttpServletRequest request, ModelAndView mav) {
		log.info("schedulePopup.do");
		//log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		// 1층 스마트 TV, 정보관리팀만 접속가능하도록 처리
		String clientIp = CommonUtils.getClientIp(request);
		if (! (clientIp.equals("10.10.1.213") || clientIp.equals("192.168.1.82") || clientIp.equals("192.168.1.83") || clientIp.equals("192.168.1.84") || clientIp.equals("192.168.1.88"))) {
			throw new CustomException(CustomExceptionCodes.NOT_ALLOWED_IP);
		}
		
		mav.setViewName("schedule/schedulePopup");
		return mav;
	}
	
	// 달력 스케쥴 리스트
	@RequestMapping(value = "getScheduleTableListAjax.json")
	public ModelAndView getScheduleTableListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getScheduleTableListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String startDate = StringUtil.reqNullCheck(request, "startDate");
				String endDate = StringUtil.reqNullCheck(request, "endDate");
				if ("".equals(startDate) || "".equals(endDate)) {
					log.error("startDate : " + startDate + ", endDate : " + endDate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 이달 예약 리스트 조회
					PageParam pageParam = new PageParam();
					pageParam.setStartDate(startDate);
					pageParam.setEndDate(endDate);

					List<DateTimePickerInfo> list = scheduleService.getScheduleTableList(pageParam);

					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("list", list);
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

	// 시간표 리스트
	@RequestMapping(value = "getScheduleTimeTableListAjax.json")
	public ModelAndView getScheduleTimeTableListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getScheduleTimeTableListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String searchDate = StringUtil.reqNullCheck(request, "searchDate");
				if ("".equals(searchDate)) {
					log.error("searchDate : " + searchDate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					PageParam param = new PageParam();
					param.setSendDate(searchDate);
					
					List<DateTimePickerInfo> list = scheduleService.getScheduleTimeTableList(param);

					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("list", list);
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
	
}
