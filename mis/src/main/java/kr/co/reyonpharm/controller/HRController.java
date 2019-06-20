package kr.co.reyonpharm.controller;

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
import kr.co.reyonpharm.models.HolidayInfo;
import kr.co.reyonpharm.models.OvertimeInfo;
import kr.co.reyonpharm.models.SalaryInfo;
import kr.co.reyonpharm.service.HRService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/hr/")
public class HRController {
	
	// 결재상태 (-1=반려, 0=미결(진행중), 1=승인,2=미제출, 3=기안취소)
	
	@Autowired
	private HRService hRService;
	
	// 급여명세서 조회 화면
	@RequestMapping(value = "salaryView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salaryView(HttpServletRequest request, ModelAndView mav) {
		log.info("salaryView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("hr/salaryView");
		return mav;
	}
	
	// 급여명세서 전체 구분 가져오기 액션 
	@RequestMapping(value = "salaryTotalPayGbAjax.json")
	public ModelAndView salaryTotalPayGbAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salaryTotalPayGbAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String sabun = SpringSecurityUtil.getUsername();
				//String deptCode = SpringSecurityUtil.getDeptCode();
				String saupcode = SpringSecurityUtil.getSaupcode();
				
				//UserInfo userinfo = new UserInfo();
				//userinfo.setDeptCode(deptCode);
				//userinfo.setSaupcode(saupcode);
				//int isSales = hRService.isSalesCheck(userinfo);
				// 2018-09-28 인사팀 문희영과장
				// 상여(2)는 진천공장만 조회 가능
				SalaryInfo param = new SalaryInfo();
				param.setSabun(sabun);
				param.setSaupcode(saupcode);
				//param.setIsSales(isSales);
				List<SalaryInfo> list = hRService.getTotalPayGb(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 급여명세서 전체 년월 가져오기 액션 
	@RequestMapping(value = "salaryTotalPayDateAjax.json")
	public ModelAndView salaryTotalPayDateAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salaryTotalPayDateAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String payGb = StringUtil.reqNullCheck(request, "payGb");
				String sabun = SpringSecurityUtil.getUsername();
				
				SalaryInfo param = new SalaryInfo();
				param.setPayGb(payGb);
				param.setSabun(sabun);
				// 2018-09-28 인사팀 문희영과장
				// 특별상여(4)는 2017-12-01 이후부터만 조회
				List<SalaryInfo> list = hRService.getTotalPayDate(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 급여명세서 조회 액션
	@RequestMapping(value = "salaryViewAjax.json")
	public ModelAndView salaryViewAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salaryViewAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String payGb = StringUtil.reqNullCheck(request, "payGb");
				String yymm = StringUtil.reqNullCheck(request, "yymm");
				String sabun = SpringSecurityUtil.getUsername();
				
				SalaryInfo param = new SalaryInfo();
				param.setSabun(sabun);
				param.setPayGb(payGb);
				param.setYymm(yymm);
				
				SalaryInfo info = hRService.getSalaryInfo(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("info", info);
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
	
	// 급여명세서 엑셀 다운로드
	@RequestMapping(value = "salaryExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salaryExcelDownload(HttpServletRequest request) {
		log.info("salaryExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("salaryExcelView");
		String payGb = StringUtil.reqNullCheck(request, "payGb");
		String yymm = StringUtil.reqNullCheck(request, "yymm");
		String sabun = SpringSecurityUtil.getUsername();

		if (payGb.equals("") || yymm.equals("")) {
			log.error("payGb : " + payGb + ", yymm : " + yymm);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		SalaryInfo param = new SalaryInfo();
		param.setSabun(sabun);
		param.setPayGb(payGb);
		param.setYymm(yymm);
		
		SalaryInfo info = hRService.getSalaryInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}
		
		String fileName = "";
		if(payGb.equals("1")) {
			fileName = "급여";
		} else if(payGb.equals("2")) {
			// 진천공장만 조회 가능
			// 2018-09-28 인사팀 문희영과장
			fileName = "상여";
		} else if(payGb.equals("3")) {
			fileName = "연월차정기수당";
		} else if(payGb.equals("4")) {
			// 2017-12-01 이후부터만 조회
			// 2018-09-28 인사팀 문희영과장
			fileName = "특별상여";
		}
		fileName += "명세서";

		mav.addObject("fileName", fileName);
		mav.addObject("payGb", payGb);
		mav.addObject("info", info);
		return mav;
	}
	
	// 휴가 관리 리스트 화면
	@RequestMapping(value = "holidayList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView holidayList(HttpServletRequest request, ModelAndView mav) {
		log.info("holidayList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("hr/holidayList");
		return mav;
	}
	
	// 조회년도 불러오기 액션 
	@RequestMapping(value = "getHolidayYYMMAjax.json")
	public ModelAndView getHolidayYYMMAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getHolidayYYMMAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				HolidayInfo param = new HolidayInfo();
				
				String type = StringUtil.reqNullCheck(request, "type");
				if(type.equals("ONE")) {
					String sabun = SpringSecurityUtil.getUsername();
					param.setSabun(sabun);
				}
				
				List<HolidayInfo> list = hRService.getHolidayYYMM(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 휴가내역 불러오기 액션 
	@RequestMapping(value = "getHolidayListAjax.json")
	public ModelAndView getHolidayListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getHolidayListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymm = StringUtil.reqNullCheck(request, "yymm");
				String sabun = StringUtil.reqNullCheck(request, "sabun");
				
				HolidayInfo param = new HolidayInfo();
				param.setSabun(sabun);
				param.setYymm(yymm);
				
				HolidayInfo info = hRService.getHolidayInfo(param);
				List<HolidayInfo> list = hRService.getHolidayList(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("info", info);
				mav.addObject("list", list);
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
	
	// 휴가 마스터 관리 리스트 화면
	@RequestMapping(value = "holidayMasterList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView holidayMasterList(HttpServletRequest request, ModelAndView mav) {
		log.info("holidayMasterList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("hr/holidayMasterList");
		return mav;
	}
	
	// 휴가 마스터 불러오기 액션 
	@RequestMapping(value = "getHolidayMasterListAjax.json")
	public ModelAndView getHolidayMasterListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getHolidayMasterListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymm = StringUtil.reqNullCheck(request, "yymm");
				String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
				String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
				
				HolidayInfo param = new HolidayInfo();
				param.setYymm(yymm);
				param.setDeptName(deptName);
				param.setKname(kname);
				
				List<HolidayInfo> list = hRService.getHolidayMasterList(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 휴가 마스터 상세 불러오기 액션 
	@RequestMapping(value = "getHolidayMasterDetailListAjax.json")
	public ModelAndView getHolidayMasterDetailListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getHolidayMasterDetailListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymm = StringUtil.reqNullCheck(request, "yymm");
				String sabun = StringUtil.reqNullCheck(request, "sabun");
				
				HolidayInfo param = new HolidayInfo();
				param.setYymm(yymm);
				param.setSabun(sabun);
				
				List<HolidayInfo> list = hRService.getHolidayMasterDetailList(param);
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 휴가 마스터 등록 액션
	@RequestMapping(value = "holidayMasterAddAjax.json")
	public ModelAndView holidayMasterAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("holidayMasterAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
				String sabun = StringUtil.reqNullCheckHangulUTF8(request, "sabun");
				String addDayType = StringUtil.reqNullCheck(request, "addDayType");
				String addDay = StringUtil.reqNullCheck(request, "addDay");
				String addDayComment = StringUtil.reqNullCheckHangulUTF8(request, "addDayComment");

				if ("".equals(yymm) || "".equals(sabun) || "".equals(addDayType) || "".equals(addDay) || "".equals(addDayComment)) {
					log.error("yymm : " + yymm + ", sabun : " + sabun + ", addDayType : " + addDayType + ", addDay : " + addDay + ", addDayComment : " + addDayComment);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String regSabun = SpringSecurityUtil.getUsername();
					
					HolidayInfo param = new HolidayInfo();
					param.setYymm(yymm);
					param.setSabun(sabun);
					param.setAddDay(addDay);
					param.setAddDayType(addDayType);
					param.setAddDayComment(addDayComment);
					param.setStatus("1");
					param.setRegSabun(regSabun);

					int resultCnt = hRService.addHolidayMaster(param);

					mav.addObject("resultCode", resultCnt);
					mav.addObject("resultMsg", "success");
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
	
	// 금주 초과근무 내역 불러오기 액션 
	@RequestMapping(value = "getOvertimeInfoAjax.json")
	public ModelAndView getOvertimeInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getOvertimeInfoAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymmdd = StringUtil.reqNullCheck(request, "yymmdd");
				String sabun = StringUtil.reqNullCheck(request, "sabun");
				OvertimeInfo param = new OvertimeInfo();
				param.setSabun(sabun);
				param.setYymmdd(yymmdd);
				OvertimeInfo info = hRService.getOvertimeInfo(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("info", info);
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
	
	// 금주 초과근무 내역 리스트 불러오기 액션 
	@RequestMapping(value = "getOvertimeListAjax.json")
	public ModelAndView getOvertimeListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getOvertimeListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymmdd = StringUtil.reqNullCheck(request, "yymmdd");
				String sabun = StringUtil.reqNullCheck(request, "sabun");
				OvertimeInfo param = new OvertimeInfo();
				param.setSabun(sabun);
				param.setYymmdd(yymmdd);
				List<OvertimeInfo> list = hRService.getOvertimeList(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 근무자 정보 불러오기 액션
	@RequestMapping(value = "getUserinfoListAjax.json")
	public ModelAndView getUserinfoListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getUserinfoListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				OvertimeInfo param = new OvertimeInfo();
				List<OvertimeInfo> list = hRService.getUserinfoList(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 초과근무 관리 리스트 화면
	@RequestMapping(value = "overtimeList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView overtimeList(HttpServletRequest request, ModelAndView mav) {
		log.info("overtimeList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("hr/overtimeList");
		return mav;
	}
	
	// 초과근무 마스터 관리 리스트 화면
	@RequestMapping(value = "overtimeMasterList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView overtimeMasterList(HttpServletRequest request, ModelAndView mav) {
		log.info("overtimeMasterList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("hr/overtimeMasterList");
		return mav;
	}
	
	// 초과근무 마스터 불러오기 액션 
	@RequestMapping(value = "getOvertimeMasterListAjax.json")
	public ModelAndView getOvertimeMasterListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getOvertimeMasterListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yymmdd = StringUtil.reqNullCheck(request, "yymmdd");
				String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
				String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
				
				OvertimeInfo param = new OvertimeInfo();
				param.setYymmdd(yymmdd);
				param.setDeptName(deptName);
				param.setKname(kname);
				
				List<OvertimeInfo> list = hRService.getOvertimeMasterList(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
