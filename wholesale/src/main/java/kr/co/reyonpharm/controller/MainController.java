package kr.co.reyonpharm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController extends CustomExceptionHandler {

	// home 접속
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView home(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("home");
		return mav;
	}

	// 로그인 페이지
	@RequestMapping(value = "login.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(HttpServletRequest request, ModelAndView mav) {
		log.info("login.do");

		HttpSession session = (HttpSession) request.getSession(false);
		if (session == null) {
			mav.setViewName("login");
		} else {
			String adminId = (String) session.getAttribute("adminId");
			if (adminId == null) {
				mav.setViewName("login");
			} else {
				mav = new ModelAndView("redirect:/main.do");
			}
		}
		return mav;
	}

	// 로그인 체크
	@RequestMapping(value = "loginCheckAjax.json")
	public ModelAndView loginCheckAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("loginCheckAjax.json");
		mav.addObject("resultCode", 0);
		return mav;
	}

	// 세션 동시 접속 방지
	@RequestMapping(value = "concurrentSessionExceed.do")
	public ModelAndView concurrentSessionExceed(HttpServletRequest request, ModelAndView mav) {
		log.info("concurrentSessionExceed.do");
		mav.addObject("resultCode", CustomExceptionCodes.CONCURRENT_SESSION_EXCEED.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.CONCURRENT_SESSION_EXCEED.getMsg());

		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/errorDuplication");
		}

		return mav;
	}

	// 메인 페이지
	@RequestMapping(value = "main.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest request, ModelAndView mav) {
		log.info("main.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		mav.setViewName("main");
		return mav;
	}
	
	// 마이 페이지
	@RequestMapping(value = "myPage.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myPage(HttpServletRequest request, ModelAndView mav) {
		log.info("myPage.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		mav.setViewName("myPage");
		return mav;
	}

	// 에러페이지
	@RequestMapping(value = "error400.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error400(HttpServletRequest request, ModelAndView mav) {
		log.error("error400.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_400.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_400.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error401.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error401(HttpServletRequest request, ModelAndView mav) {
		log.error("error401.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_401.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_401.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error403.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error403(HttpServletRequest request, ModelAndView mav) {
		log.error("error403.do");
		// 시스템 기본이 아닌 서비스에서 직접 코드와 메시지를 실어보낸 경우 (팝업창에서 세션 만료된 경우)
		String resultCode = StringUtil.reqNullCheck(request, "resultCode");
		String resultMsg = StringUtil.reqNullCheck(request, "resultMsg");
		mav.addObject("resultCode", "".equals(resultCode) ? CustomExceptionCodes.ERROR_403.getId() : resultCode);
		mav.addObject("resultMsg", "".equals(resultMsg) ? CustomExceptionCodes.ERROR_403.getMsg() : resultMsg);
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error404.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error404(HttpServletRequest request, ModelAndView mav) {
		log.error("error404.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_404.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_404.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error404");
		}
		return mav;
	}

	@RequestMapping(value = "error405.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error405(HttpServletRequest request, ModelAndView mav) {
		log.error("error405.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_405.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_405.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error406.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error406(HttpServletRequest request, ModelAndView mav) {
		log.error("error406.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_406.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_406.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error408.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error408(HttpServletRequest request, ModelAndView mav) {
		log.error("error408.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_408.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_408.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error409.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error409(HttpServletRequest request, ModelAndView mav) {
		log.error("error409.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_409.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_409.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error412.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error412(HttpServletRequest request, ModelAndView mav) {
		log.error("error412.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_412.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_412.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error500.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error500(HttpServletRequest request, ModelAndView mav) {
		log.error("error500.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_500.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_500.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error501.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error501(HttpServletRequest request, ModelAndView mav) {
		log.error("error501.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_501.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_501.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error502.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error502(HttpServletRequest request, ModelAndView mav) {
		log.error("error502.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_502.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_502.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

	@RequestMapping(value = "error503.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView error503(HttpServletRequest request, ModelAndView mav) {
		log.error("error503.do");
		mav.addObject("resultCode", CustomExceptionCodes.ERROR_503.getId());
		mav.addObject("resultMsg", CustomExceptionCodes.ERROR_503.getMsg());
		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (request.getHeader("accept").indexOf("text/html") >= 0) {
			mav.setViewName("error/error");
		}
		return mav;
	}

}
