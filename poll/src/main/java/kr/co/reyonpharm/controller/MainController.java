package kr.co.reyonpharm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.PollInfo;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	MainService mainService;
	
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
				mav = new ModelAndView("redirect:./main.do");
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
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		PollInfo param = new PollInfo();
		param.setVoteDate("yes");
		param.setVoteSabun(SpringSecurityUtil.getUsername());
		List<PollInfo> list = mainService.selectPoll(param);
		if (list.size() != 0) {
			String sabun = SpringSecurityUtil.getUsername();
			if(!sabun.equals("07071803") && !sabun.equals("07031201") && !sabun.equals("07031902") && !sabun.equals("18021201") && !sabun.equals("18030502")) {
				log.info(SpringSecurityUtil.getUsername() + " - 투표 완료자");
				mav.setViewName("complete");
			} else {
				mav = new ModelAndView("redirect:admin.do");
			}
		} else {
			mav.setViewName("main");
		}
		return mav;
	}
	
	// 메인 페이지
	@RequestMapping(value = "admin.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView admin(HttpServletRequest request, ModelAndView mav) {
		log.info("admin.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		String sabun = SpringSecurityUtil.getUsername();
		if(!sabun.equals("07071803") && !sabun.equals("07031201") && !sabun.equals("07031902") && !sabun.equals("18021201") && !sabun.equals("18030502")) {
			mav = new ModelAndView("redirect:logout.do");
		} else {
			mav.setViewName("admin");
		}
		return mav;
	}
	
	// 투표 내역 가져오기 액션
	@RequestMapping(value = "pollGetAjax.json")
	public ModelAndView pollGetAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pollGetAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				PollInfo param = new PollInfo();
				param.setVoteDate("yes");
				List<PollInfo> yesList = mainService.selectPoll(param);
				param.setVoteDate("no");
				List<PollInfo> noList = mainService.selectPoll(param);
				List<PollInfo> statList = mainService.selectStatistics();
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("yesList", yesList);
				mav.addObject("noList", noList);
				mav.addObject("statList", statList);
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
	
	// 투표 내역 등록 액션
	@RequestMapping(value = "pollAddAjax.json")
	public ModelAndView pollAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pollAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				PollInfo param = new PollInfo();
				param.setVoteDate("yes");
				param.setVoteSabun(SpringSecurityUtil.getUsername());
				List<PollInfo> list = mainService.selectPoll(param);
				
				if (list.size() != 0) {
					log.error("[" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() +"] 투표 완료자");
					mav.addObject("resultCode", CustomExceptionCodes.ALREADY_VOTE.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.ALREADY_VOTE.getMsg());
				} else {
					String voteChoice = StringUtil.reqNullCheck(request, "voteChoice");
					
					if ("".equals(voteChoice)) {
						log.error(SpringSecurityUtil.getKname() + " - " + CustomExceptionCodes.MISSING_PARAMETER.getMsg());
						mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
					} else {
						PollInfo info = new PollInfo();
						info.setVoteSabun(SpringSecurityUtil.getUsername());
						info.setVoteChoice(voteChoice);
						int resultCnt = mainService.insertPoll(info);
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
