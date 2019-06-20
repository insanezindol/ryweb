package kr.co.reyonpharm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/vision/")
public class VisionController {
	
	// best till 2023 화면
	@RequestMapping(value = "besttill2023.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView besttill2023(HttpServletRequest request, ModelAndView mav) {
		log.info("besttill2023.do");
		mav.setViewName("vision/besttill2023");
		return mav;
	}

	// best till 2023 화면 ver.2
	@RequestMapping(value = "besttill2023_bk.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView besttill2023_bk(HttpServletRequest request, ModelAndView mav) {
		log.info("besttill2023_bk.do");
		mav.setViewName("vision/besttill2023_bk");
		return mav;
	}
	
	// best till 2023 화면 ver.3
	@RequestMapping(value = "besttill2023v3.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView besttill2023v3(HttpServletRequest request, ModelAndView mav) {
		log.info("besttill2023v3.do");
		mav.setViewName("vision/besttill2023v3");
		return mav;
	}
	
}
