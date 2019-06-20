package kr.co.reyonpharm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import kr.co.reyonpharm.models.SalesInfo;
import kr.co.reyonpharm.service.SfeService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sfe/")
public class SfeController extends CustomExceptionHandler {

	@Autowired
	SfeService sfeService;

	/* SFE 관리 */
	
	// 부서 가져오기
	@RequestMapping(value = "salesDeptAjax.json")
	public ModelAndView salesDeptAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesDeptAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				Map paramMap = new HashMap();
				paramMap.put("gubun", "9");
				paramMap.put("deptCd", "");
				paramMap.put("userDeptCd", "19110");
				paramMap.put("userEmpNo", "17041701");
				paramMap.put("userAuth", "A");
				Map result = sfeService.getSalesDept(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 담당자 가져오기
	@RequestMapping(value = "salesUserAjax.json")
	public ModelAndView salesUserAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesUserAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String deptNo = StringUtil.reqNullCheckHangulUTF8(request, "deptNo");
			if (auth.equals("reyon")) {
				SalesInfo param = new SalesInfo();
				param.setDeptNo(deptNo);
				List<SalesInfo> list = sfeService.getSalesUser(param);
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
	
	// 거래처 가져오기
	@RequestMapping(value = "salesAccountAjax.json")
	public ModelAndView salesAccountAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesAccountAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String deptNo = StringUtil.reqNullCheckHangulUTF8(request, "deptNo");
			String empNo = StringUtil.reqNullCheckHangulUTF8(request, "empNo");
			if (auth.equals("reyon")) {
				Map paramMap = new HashMap();
				paramMap.put("gubun", "Y");
				paramMap.put("deptNo", deptNo);
				paramMap.put("empNo", empNo);
				paramMap.put("cusCd", "");
				Map result = sfeService.getSalesAccount(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 활동 경로
	@RequestMapping(value = "salesRouteActivity.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesRouteActivity(HttpServletRequest request, ModelAndView mav) {
		log.info("salesRouteActivity.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesRouteActivity");
		return mav;
	}
	
	// 활동 경로 - 사원 가져오기
	@RequestMapping(value = "salesRouteActivityUser.json")
	public ModelAndView salesRouteActivityUser(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesRouteActivityUser.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String deptNo = StringUtil.reqNullCheckHangulUTF8(request, "deptNo");
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			if (auth.equals("reyon")) {
				SalesInfo param = new SalesInfo();
				param.setDeptNo(deptNo);
				param.setStandardDate(standardDate);
				List<SalesInfo> list = sfeService.getSalesRouteActivityUser(param);
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
	
	// 활동 경로 - 경로 가져오기
	@RequestMapping(value = "salesRouteActivityRoute.json")
	public ModelAndView salesRouteActivityRoute(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesRouteActivityRoute.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String deptNo = StringUtil.reqNullCheckHangulUTF8(request, "deptNo");
			String empNo = StringUtil.reqNullCheckHangulUTF8(request, "empNo");
			String gubun = StringUtil.reqNullCheckHangulUTF8(request, "gubun");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String cust = StringUtil.reqNullCheckHangulUTF8(request, "cust");
			if (auth.equals("reyon")) {
				Map paramMap = new HashMap();
				paramMap.put("startDate", startDate);
				paramMap.put("endDate", endDate);
				paramMap.put("gubun", gubun);
				paramMap.put("deptNo", deptNo);
				paramMap.put("empNo", empNo);
				paramMap.put("cust", cust);
				Map result = sfeService.getSalesRouteActivityRoute(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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

	// 기준 좌표 변경
	@RequestMapping(value = "salesRouteChange.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesRouteChange(HttpServletRequest request, ModelAndView mav) {
		log.info("salesRouteChange.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesRouteChange");
		return mav;
	}
	
	// 기준 좌표 변경 - 경로 가져오기
	@RequestMapping(value = "salesRouteChangeRoute.json")
	public ModelAndView salesRouteChangeRoute(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesRouteChangeRoute.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String empNo = StringUtil.reqNullCheckHangulUTF8(request, "empNo");
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			if (auth.equals("reyon")) {
				SalesInfo param = new SalesInfo();
				param.setStandardDate(standardDate);
				param.setEmpNo(empNo);
				List<SalesInfo> list = sfeService.getSalesRouteChangeRoute(param);
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
	
	// 기준 좌표 변경 - 거래처 좌표 가져오기
	@RequestMapping(value = "salesRouteChangePosition.json")
	public ModelAndView salesRouteChangePosition(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesRouteChangePosition.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String sfaSalesNo = StringUtil.reqNullCheckHangulUTF8(request, "sfaSalesNo");
			if (auth.equals("reyon")) {
				SalesInfo param = new SalesInfo();
				param.setSfaSalesNo(sfaSalesNo);
				SalesInfo info = sfeService.getSalesRouteChangePosition(param);
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
	
	// 기준 좌표 변경 - 거래처 좌표 변경
	@RequestMapping(value = "salesRouteChangeAccount.json")
	public ModelAndView salesRouteChangeAccount(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesRouteChangeAccount.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String sfaSalesNo = StringUtil.reqNullCheckHangulUTF8(request, "sfaSalesNo");
			String humanX = StringUtil.reqNullCheckHangulUTF8(request, "humanX");
			String humanY = StringUtil.reqNullCheckHangulUTF8(request, "humanY");
			if (auth.equals("reyon")) {
				if (sfaSalesNo.equals("") || humanX.equals("") || humanY.equals("")) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					Map paramMap = new HashMap();
					paramMap.put("sfaSalesNo", sfaSalesNo);
					paramMap.put("humanX", humanX);
					paramMap.put("humanY", humanY);
					Map result = sfeService.getSalesRouteChangeAccount(paramMap);
					mav.addObject("resultCode", result.get("errNum"));
					mav.addObject("resultMsg", result.get("errMsg"));
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
	
	// 콜매출 Trend
	@RequestMapping(value = "salesTrendList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesTrendList(HttpServletRequest request, ModelAndView mav) {
		log.info("salesTrendList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesTrendList");
		return mav;
	}
	
	// 콜매출 Trend - 리스트 가져오기
	@RequestMapping(value = "salesTrendListAjax.json")
	public ModelAndView salesTrendListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesTrendListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			if (auth.equals("reyon")) {
				// procedure parameter
				Map paramMap = new HashMap();
				paramMap.put("standardDate", standardDate);
				paramMap.put("s_jijum", s_jijum);
				paramMap.put("s_user", s_user);
				paramMap.put("s_account", s_account);
				paramMap.put("userDeptCd", "19110");
				paramMap.put("userEmpNo", "17041701");
				paramMap.put("userAuth", "A");
				Map result = sfeService.getSalesTrendList(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 거래처 방문 활동
	@RequestMapping(value = "salesAccountList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesAccountList(HttpServletRequest request, ModelAndView mav) {
		log.info("salesAccountList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesAccountList");
		return mav;
	}
	
	// 거래처 방문 활동 - 리스트 가져오기
	@RequestMapping(value = "salesAccountListAjax.json")
	public ModelAndView salesAccountListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesAccountListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			if (auth.equals("reyon")) {
				// procedure parameter
				Map paramMap = new HashMap();
				paramMap.put("startDate", startDate);
				paramMap.put("endDate", endDate);
				paramMap.put("s_jijum", s_jijum);
				paramMap.put("s_user", s_user);
				paramMap.put("s_account", s_account);
				paramMap.put("userDeptCd", "19110");
				paramMap.put("userEmpNo", "17041701");
				paramMap.put("userAuth", "A");
				Map result = sfeService.getSalesAccountList(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 지점별 방문 활동
	@RequestMapping(value = "salesPointList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesPointList(HttpServletRequest request, ModelAndView mav) {
		log.info("salesPointList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesPointList");
		return mav;
	}
	
	// 지점별 방문 활동 - 리스트 가져오기
	@RequestMapping(value = "salesPointListAjax.json")
	public ModelAndView salesPointListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesPointListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			if (auth.equals("reyon")) {
				// procedure parameter
				Map paramMap = new HashMap();
				paramMap.put("standardDate", standardDate);
				paramMap.put("s_jijum", s_jijum);
				paramMap.put("s_user", s_user);
				paramMap.put("userDeptCd", "19110");
				paramMap.put("userEmpNo", "17041701");
				paramMap.put("userAuth", "A");
				Map result = sfeService.getSalesPointList(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 활동 내역 조회
	@RequestMapping(value = "salesActivityList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesActivityList(HttpServletRequest request, ModelAndView mav) {
		log.info("salesActivityList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesActivityList");
		return mav;
	}
	
	// 방문 매출 현황
	@RequestMapping(value = "salesProfitList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salesProfitList(HttpServletRequest request, ModelAndView mav) {
		log.info("salesProfitList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("sfe/salesProfitList");
		return mav;
	}
	
	// 방문 매출 현황 - 리스트 가져오기
	@RequestMapping(value = "salesProfitListAjax.json")
	public ModelAndView salesProfitListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("salesProfitListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			if (auth.equals("reyon")) {
				// procedure parameter
				Map paramMap = new HashMap();
				paramMap.put("startDate", startDate);
				paramMap.put("endDate", endDate);
				paramMap.put("s_jijum", s_jijum);
				paramMap.put("s_user", s_user);
				paramMap.put("s_account", s_account);
				paramMap.put("userDeptCd", "19110");
				paramMap.put("userEmpNo", "17041701");
				paramMap.put("userAuth", "A");
				Map result = sfeService.getSalesProfitList(paramMap);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", result.get("resultList"));
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
	
	// 조회 결과 엑셀 다운로드
	@RequestMapping(value = "sfeDownloadExcel.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sfeDownloadExcel(HttpServletRequest request) {
		log.info("sfeDownloadExcel.do");

		ModelAndView mav = new ModelAndView("sfeDownloadExcelView");
		String auth = StringUtil.reqNullCheck(request, "auth");
		String excelType = StringUtil.reqNullCheck(request, "excelType");

		if (!auth.equals("reyon") || excelType.equals("")) {
			log.error("auth : " + auth + ", gubun : " + excelType);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String fileName = "";
		List<String> listColumn = new ArrayList<String>();
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		if (excelType.equals("10")) {
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			// procedure parameter
			Map paramMap = new HashMap();
			paramMap.put("standardDate", standardDate);
			paramMap.put("s_jijum", s_jijum);
			paramMap.put("s_user", s_user);
			paramMap.put("s_account", s_account);
			paramMap.put("userDeptCd", "19110");
			paramMap.put("userEmpNo", "17041701");
			paramMap.put("userAuth", "A");
			Map result = sfeService.getSalesTrendList(paramMap);
			list = (List<HashMap<String,String>>) result.get("resultList");
			
			fileName = "콜매출TREND";
			listColumn.add("지점");
			listColumn.add("담당자");
			listColumn.add("거래처");
			listColumn.add("구분");
			listColumn.add("C-2");
			listColumn.add("C-1");
			listColumn.add("CALL");
			listColumn.add("매출-2");
			listColumn.add("매출-1");
			listColumn.add("매출");
			listColumn.add("계획-2");
			listColumn.add("계획-1");
			listColumn.add("계획");
			listColumn.add("달성-2");
			listColumn.add("달성-1");
			listColumn.add("달성률");
		} else if (excelType.equals("20")) {
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			// procedure parameter
			Map paramMap = new HashMap();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("s_jijum", s_jijum);
			paramMap.put("s_user", s_user);
			paramMap.put("s_account", s_account);
			paramMap.put("userDeptCd", "19110");
			paramMap.put("userEmpNo", "17041701");
			paramMap.put("userAuth", "A");
			Map result = sfeService.getSalesAccountList(paramMap);
			list = (List<HashMap<String,String>>) result.get("resultList");
			
			fileName = "거래처방문활동";
			listColumn.add("지점");
			listColumn.add("담당자");
			listColumn.add("거래처");
			listColumn.add("구분");
			listColumn.add("개시일");
			listColumn.add("계획계");
			listColumn.add("정상계획");
			listColumn.add("점유율");
			listColumn.add("추가계획");
			listColumn.add("점유율");
			listColumn.add("방문계");
			listColumn.add("정상계획방문");
			listColumn.add("점유율");
			listColumn.add("추가계획방문");
			listColumn.add("점유율");
		} else if (excelType.equals("30")) {
			String standardDate = StringUtil.reqNullCheckHangulUTF8(request, "standardDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			// procedure parameter
			Map paramMap = new HashMap();
			paramMap.put("standardDate", standardDate);
			paramMap.put("s_jijum", s_jijum);
			paramMap.put("s_user", s_user);
			paramMap.put("userDeptCd", "19110");
			paramMap.put("userEmpNo", "17041701");
			paramMap.put("userAuth", "A");
			Map result = sfeService.getSalesPointList(paramMap);
			list = (List<HashMap<String,String>>) result.get("resultList");
			
			fileName = "지점별방문활동";
			listColumn.add("지점");
			listColumn.add("담당자");
			listColumn.add("정상-2");
			listColumn.add("정상-1");
			listColumn.add("정상계획");
			listColumn.add("점유율-2");
			listColumn.add("점유율-1");
			listColumn.add("점유율");
			listColumn.add("월평균");
			listColumn.add("추가-2");
			listColumn.add("추가-1");
			listColumn.add("추가계획");
			listColumn.add("점유율-2");
			listColumn.add("점유율-1");
			listColumn.add("점유율");
			listColumn.add("월평균");
			listColumn.add("정상방문-2");
			listColumn.add("정상방문-1");
			listColumn.add("정상계획방문");
			listColumn.add("점유율-2");
			listColumn.add("점유율-1");
			listColumn.add("점유율");
			listColumn.add("월평균");
			listColumn.add("추가방문-2");
			listColumn.add("추가방문-1");
			listColumn.add("추가계획방문");
			listColumn.add("점유율-2");
			listColumn.add("점유율-1");
			listColumn.add("점유율");
			listColumn.add("월평균");
		} else if (excelType.equals("40")) {
			String deptNo = StringUtil.reqNullCheckHangulUTF8(request, "deptNo");
			String empNo = StringUtil.reqNullCheckHangulUTF8(request, "empNo");
			String gubun = StringUtil.reqNullCheckHangulUTF8(request, "gubun");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String cust = StringUtil.reqNullCheckHangulUTF8(request, "cust");
			
			Map paramMap = new HashMap();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("gubun", gubun);
			paramMap.put("deptNo", deptNo);
			paramMap.put("empNo", empNo);
			paramMap.put("cust", cust);
			Map result = sfeService.getSalesRouteActivityRoute(paramMap);
			list = (List<HashMap<String,String>>) result.get("resultList");
			
			fileName = "활동내역조회";
			listColumn.add("지점");
			listColumn.add("담당자");
			listColumn.add("거래처");
			listColumn.add("개시일");
			listColumn.add("구분");
			listColumn.add("활동일");
			listColumn.add("시작일시");
			listColumn.add("종료일시");
			listColumn.add("활동(분)");
			listColumn.add("활동코드");
			listColumn.add("활동유형");
			listColumn.add("활동내역");
			listColumn.add("위도");
			listColumn.add("경도");
			listColumn.add("결과");
			listColumn.add("모의");
			listColumn.add("루팅");
			listColumn.add("계획구분");
		} else if (excelType.equals("50")) {
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String s_jijum = StringUtil.reqNullCheckHangulUTF8(request, "s_jijum");
			String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
			String s_account = StringUtil.reqNullCheckHangulUTF8(request, "s_account");
			// procedure parameter
			Map paramMap = new HashMap();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("s_jijum", s_jijum);
			paramMap.put("s_user", s_user);
			paramMap.put("s_account", s_account);
			paramMap.put("userDeptCd", "19110");
			paramMap.put("userEmpNo", "17041701");
			paramMap.put("userAuth", "A");
			Map result = sfeService.getSalesProfitList(paramMap);
			list = (List<HashMap<String,String>>) result.get("resultList");
			
			fileName = "방문매출현황";
			listColumn.add("지점");
			listColumn.add("담당자");
			listColumn.add("거래처");
			listColumn.add("진료과");
			listColumn.add("제품");
			listColumn.add("CALL");
			listColumn.add("매출");
		}
		
		mav.addObject("excelType", excelType);
		mav.addObject("fileName", fileName);
		mav.addObject("listColumn", listColumn);
		mav.addObject("list", list);
		return mav;
	}

}
