package kr.co.reyonpharm.controller;

import java.util.ArrayList;
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
import kr.co.reyonpharm.models.PunctualityInfo;
import kr.co.reyonpharm.service.PunctualityService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/punctuality/")
public class PunctualityController extends CustomExceptionHandler {

	@Autowired
	PunctualityService punctualityService;
	
	// 근태 확인
	@RequestMapping(value = "punctualityList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView punctualityList(HttpServletRequest request, ModelAndView mav) {
		log.info("punctualityList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		/*String loginSabun = SpringSecurityUtil.getUsername();
		PunctualityInfo param = new PunctualityInfo();
		param.setLoginSabun(loginSabun);
		PunctualityInfo authInfo = punctualityService.getPunctualityAuth(param);
		
		// 기본 권한 (본인의 결재가능 부서만 조회)
		String type = "D"; 
		if (authInfo != null) {
			if (authInfo.getAuthView().equals("A")) {
				type = "A";
			} else if (authInfo.getAuthView().equals("B")) {
				type = "B";
			}
		}
		
		mav.addObject("type", type);*/
		mav.setViewName("punctuality/punctualityList");
		return mav;
	}
	
	// 근태 정보 조회
	@RequestMapping(value = "getPunctualityListAjax.json")
	public ModelAndView getPunctualityListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getPunctualityListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
				String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
				String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
				String capsDept = StringUtil.reqNullCheckHangulUTF8(request, "capsDept");
				String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
				String startWorkNull = StringUtil.reqNullCheckHangulUTF8(request, "startWorkNull");
				String startWorkAfter = StringUtil.reqNullCheckHangulUTF8(request, "startWorkAfter");
				String endWorkAfter = StringUtil.reqNullCheckHangulUTF8(request, "endWorkAfter");
				
				String loginSabun = SpringSecurityUtil.getUsername();
				/*Map paramMap = new HashMap();
				paramMap.put("workGbn", workGbn);
				paramMap.put("startDate", startDate);
				paramMap.put("endDate", endDate);
				paramMap.put("loginSabun", loginSabun);
				Map result = punctualityService.getAdtCapsList(paramMap);*/
				
				PunctualityInfo param = new PunctualityInfo();
				param.setLoginSabun(loginSabun);
				param.setStartDate(startDate);
				param.setEndDate(endDate);
				param.setDeptName(deptName);
				param.setCapsDept(capsDept);
				param.setKname(kname);
				param.setStartWorkNull(startWorkNull);
				param.setStartWorkAfter(startWorkAfter);
				param.setEndWorkAfter(endWorkAfter);
				
				//PunctualityInfo authInfo = punctualityService.getPunctualityAuth(param);
				
				// 기본 권한 (본인의 결재가능 부서만 조회)
				/*String type = "D";
				if (authInfo != null) {
					if (authInfo.getAuthView().equals("A")) {
						type = "A";
					} else if (authInfo.getAuthView().equals("B")) {
						type = "B";
					}
				}*/
				param.setType("B");
				
				List<PunctualityInfo> list = punctualityService.getPunctualityList(param);
				
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
	
	// 엑셀 다운로드
	@RequestMapping(value = "punctualityExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView punctualityExcelDownload(HttpServletRequest request) {
		log.info("punctualityExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("punctualityExcelView");
		
		String auth = StringUtil.reqNullCheck(request, "auth");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
		String capsDept = StringUtil.reqNullCheckHangulUTF8(request, "capsDept");
		String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
		String startWorkNull = StringUtil.reqNullCheckHangulUTF8(request, "startWorkNull");
		String startWorkAfter = StringUtil.reqNullCheckHangulUTF8(request, "startWorkAfter");
		String endWorkAfter = StringUtil.reqNullCheckHangulUTF8(request, "endWorkAfter");
		
		// 로그인 정보
		String loginSabun = SpringSecurityUtil.getUsername();

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		if (startDate.equals("") || endDate.equals("")) {
			log.error("startDate : " + startDate + " , endDate : " + endDate);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		PunctualityInfo param = new PunctualityInfo();
		param.setLoginSabun(loginSabun);
		param.setStartDate(startDate);
		param.setEndDate(endDate);
		param.setDeptName(deptName);
		param.setCapsDept(capsDept);
		param.setKname(kname);
		param.setStartWorkNull(startWorkNull);
		param.setStartWorkAfter(startWorkAfter);
		param.setEndWorkAfter(endWorkAfter);
		
		/*PunctualityInfo authInfo = punctualityService.getPunctualityAuth(param);
		
		String type = "D"; // 기본 권한 (본인의 결재가능 부서만 조회)
		if (authInfo != null) {
			if (authInfo.getAuthView().equals("A")) {
				type = "A";
			} else if (authInfo.getAuthView().equals("B")) {
				type = "B";
			}
		}*/
		param.setType("B");
		
		List<PunctualityInfo> listData = punctualityService.getPunctualityList(param);

		// 파일이름, 시트이름
		String fileName = "진천공장근태조회("+startDate + "-" + endDate+")";
		String sheetName = startDate + " ~ " + endDate;

		// 컬럼 데이터, 내용 데이터
		List<String> listColumn = new ArrayList<String>();
		listColumn.add("번호");
		listColumn.add("부서명");
		listColumn.add("이름");
		listColumn.add("직위");
		listColumn.add("근태일자");
		listColumn.add("근무일");
		listColumn.add("출근시간");
		listColumn.add("퇴근시간");
		listColumn.add("비고");
		mav.addObject("listColumn", listColumn);
		mav.addObject("listData", listData);

		mav.addObject("fileName", fileName);
		mav.addObject("sheetName", sheetName);
		return mav;
	}

}
