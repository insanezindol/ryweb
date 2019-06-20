package kr.co.reyonpharm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.SaleInfo;
import kr.co.reyonpharm.service.SalesService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sales/")
public class SalesController {
	
	@Autowired
	private SalesService salesService;
	
	// 판매/수금 목표대비 실적 조회 화면
	@RequestMapping(value = "salesPerformanceView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView salaryView(HttpServletRequest request, ModelAndView mav) {
		log.info("salaryView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		List<SaleInfo> totalDateList = salesService.getSalesPerformanceInfoDateList();
		mav.addObject("totalDateList", totalDateList);
		
		mav.setViewName("sales/salesPerformanceView");
		return mav;
	}
	
	// 판매/수금 목표대비 실적 조회 액션
	@RequestMapping(value = "getSalesPerformanceInfoAjax.json")
	public ModelAndView getSalesPerformanceInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getSalesPerformanceInfoAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yyyymm = StringUtil.reqNullCheck(request, "yyyymm");
			if (auth.equals("reyon")) {
				SaleInfo param = new SaleInfo();
				param.setYyyymm(yyyymm);
				List<SaleInfo> list = salesService.getSalesPerformanceInfoList(param);
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
