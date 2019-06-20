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

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.ReportInfo;
import kr.co.reyonpharm.models.VoteInfo;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController extends CommonController {

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

	// 투표 메인 페이지
	@RequestMapping(value = "main.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest request, ModelAndView mav) {
		log.info("main.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		VoteInfo param = new VoteInfo();
		param.setVoteSabun(SpringSecurityUtil.getUsername());
		VoteInfo info = mainService.getVote(param);
		
		if (info != null) {
			String sabun = SpringSecurityUtil.getUsername();
			if(!sabun.equals("07071803") && !sabun.equals("07031201") && !sabun.equals("07031902") && !sabun.equals("18021201")) {
				log.error(SpringSecurityUtil.getUsername() + " - 투표 완료자");
				mav.setViewName("complete");
			} else {
				mav = new ModelAndView("redirect:admin.do");
			}
		} else {
			mav.setViewName("main");
		}
		
		return mav;
	}
	
	// 투표 관리자 페이지
	@RequestMapping(value = "admin.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView admin(HttpServletRequest request, ModelAndView mav) {
		log.info("admin.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		String sabun = SpringSecurityUtil.getUsername();
		
		if(!sabun.equals("07071803") && !sabun.equals("07031201") && !sabun.equals("07031902") && !sabun.equals("18021201")) {
			log.error(sabun + " - ACCESS DENY");
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}
		
		mav.setViewName("admin");
		return mav;
	}
	
	// 투표 내역 가져오기 액션
	@RequestMapping(value = "voteGetAjax.json")
	public ModelAndView voteGetAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("voteGetAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				List<ReportInfo> list = mainService.getReportList();
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

	// 투표 내역 등록 액션
	@RequestMapping(value = "voteAddAjax.json")
	public ModelAndView voteAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("voteAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				VoteInfo param = new VoteInfo();
				param.setVoteSabun(SpringSecurityUtil.getUsername());
				VoteInfo vi = mainService.getVote(param);
				
				if (vi != null) {
					log.error(SpringSecurityUtil.getUsername() + " - 투표 완료자");
					mav.addObject("resultCode", CustomExceptionCodes.ALREADY_VOTE.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.ALREADY_VOTE.getMsg());
				} else {
					String voteN01 = StringUtil.reqNullCheck(request, "voteN01");
					String voteN02 = StringUtil.reqNullCheck(request, "voteN02");
					String voteN03 = StringUtil.reqNullCheck(request, "voteN03");
					String voteN04 = StringUtil.reqNullCheck(request, "voteN04");
					String voteN05 = StringUtil.reqNullCheck(request, "voteN05");
					String voteN06 = StringUtil.reqNullCheck(request, "voteN06");
					String voteN07 = StringUtil.reqNullCheck(request, "voteN07");
					String voteN08 = StringUtil.reqNullCheck(request, "voteN08");
					String voteN09 = StringUtil.reqNullCheck(request, "voteN09");
					String voteN10 = StringUtil.reqNullCheck(request, "voteN10");
					String voteN11 = StringUtil.reqNullCheck(request, "voteN11");
					String voteN12 = StringUtil.reqNullCheck(request, "voteN12");
					String voteN13 = StringUtil.reqNullCheck(request, "voteN13");
					String voteN14 = StringUtil.reqNullCheck(request, "voteN14");
					String voteN15 = StringUtil.reqNullCheck(request, "voteN15");
					String voteN16 = StringUtil.reqNullCheck(request, "voteN16");
					String voteN17 = StringUtil.reqNullCheck(request, "voteN17");
					String voteN18 = StringUtil.reqNullCheck(request, "voteN18");
					String voteN19 = StringUtil.reqNullCheck(request, "voteN19");
					String voteN20 = StringUtil.reqNullCheck(request, "voteN20");
					String voteN21 = StringUtil.reqNullCheck(request, "voteN21");
					String voteN22 = StringUtil.reqNullCheck(request, "voteN22");
					String voteN23 = StringUtil.reqNullCheck(request, "voteN23");
					String voteN24 = StringUtil.reqNullCheck(request, "voteN24");
					String voteN25 = StringUtil.reqNullCheck(request, "voteN25");
					String voteN26 = StringUtil.reqNullCheck(request, "voteN26");
					String voteN27 = StringUtil.reqNullCheck(request, "voteN27");
					String voteN28 = StringUtil.reqNullCheck(request, "voteN28");
					String voteN29 = StringUtil.reqNullCheck(request, "voteN29");
					String voteN30 = StringUtil.reqNullCheck(request, "voteN30");
					String voteN31 = StringUtil.reqNullCheck(request, "voteN31");
					String voteN32 = StringUtil.reqNullCheck(request, "voteN32");
					String voteN33 = StringUtil.reqNullCheck(request, "voteN33");
					String voteN34 = StringUtil.reqNullCheck(request, "voteN34");
					String voteN35 = StringUtil.reqNullCheck(request, "voteN35");
					String voteN36 = StringUtil.reqNullCheck(request, "voteN36");
					String voteN37 = StringUtil.reqNullCheck(request, "voteN37");
					String voteN38 = StringUtil.reqNullCheck(request, "voteN38");
					String voteN39 = StringUtil.reqNullCheck(request, "voteN39");
					String voteN40 = StringUtil.reqNullCheck(request, "voteN40");
					String voteN41 = StringUtil.reqNullCheck(request, "voteN41");
					String voteN42 = StringUtil.reqNullCheck(request, "voteN42");
					String voteN43 = StringUtil.reqNullCheck(request, "voteN43");
					String voteN44 = StringUtil.reqNullCheck(request, "voteN44");
					String voteN45 = StringUtil.reqNullCheck(request, "voteN45");
					String voteN46 = StringUtil.reqNullCheck(request, "voteN46");
					String voteN47 = StringUtil.reqNullCheck(request, "voteN47");
					String voteN48 = StringUtil.reqNullCheck(request, "voteN48");
					
					if ("".equals(voteN01) || "".equals(voteN02) || "".equals(voteN03) || "".equals(voteN04) || "".equals(voteN05) || 
						"".equals(voteN06) || "".equals(voteN07) || "".equals(voteN08) || "".equals(voteN09) || "".equals(voteN10) || 
						"".equals(voteN11) || "".equals(voteN12) || "".equals(voteN13) || "".equals(voteN14) || "".equals(voteN15) || 
						"".equals(voteN16) || "".equals(voteN17) || "".equals(voteN18) || "".equals(voteN19) || "".equals(voteN20) || 
						"".equals(voteN21) || "".equals(voteN22) || "".equals(voteN23) || "".equals(voteN24) || "".equals(voteN25) || 
						"".equals(voteN26) || "".equals(voteN27) || "".equals(voteN28) || "".equals(voteN29) || "".equals(voteN30) || 
						"".equals(voteN31) || "".equals(voteN32) || "".equals(voteN33) || "".equals(voteN34) || "".equals(voteN35) || 
						"".equals(voteN36) || "".equals(voteN37) || "".equals(voteN38) || "".equals(voteN39) || "".equals(voteN40) || 
						"".equals(voteN41) || "".equals(voteN42) || "".equals(voteN43) || "".equals(voteN44) || "".equals(voteN45) ||
						"".equals(voteN46) || "".equals(voteN47) || "".equals(voteN48)) {
						log.error(SpringSecurityUtil.getKname() + " - " + CustomExceptionCodes.MISSING_PARAMETER.getMsg());
						mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
					} else {
						VoteInfo info = new VoteInfo();
						info.setVoteSabun(SpringSecurityUtil.getUsername());
						info.setVoteN01(voteN01);
						info.setVoteN02(voteN02);
						info.setVoteN03(voteN03);
						info.setVoteN04(voteN04);
						info.setVoteN05(voteN05);
						info.setVoteN06(voteN06);
						info.setVoteN07(voteN07);
						info.setVoteN08(voteN08);
						info.setVoteN09(voteN09);
						info.setVoteN10(voteN10);
						info.setVoteN11(voteN11);
						info.setVoteN12(voteN12);
						info.setVoteN13(voteN13);
						info.setVoteN14(voteN14);
						info.setVoteN15(voteN15);
						info.setVoteN16(voteN16);
						info.setVoteN17(voteN17);
						info.setVoteN18(voteN18);
						info.setVoteN19(voteN19);
						info.setVoteN20(voteN20);
						info.setVoteN21(voteN21);
						info.setVoteN22(voteN22);
						info.setVoteN23(voteN23);
						info.setVoteN24(voteN24);
						info.setVoteN25(voteN25);
						info.setVoteN26(voteN26);
						info.setVoteN27(voteN27);
						info.setVoteN28(voteN28);
						info.setVoteN29(voteN29);
						info.setVoteN30(voteN30);
						info.setVoteN31(voteN31);
						info.setVoteN32(voteN32);
						info.setVoteN33(voteN33);
						info.setVoteN34(voteN34);
						info.setVoteN35(voteN35);
						info.setVoteN36(voteN36);
						info.setVoteN37(voteN37);
						info.setVoteN38(voteN38);
						info.setVoteN39(voteN39);
						info.setVoteN40(voteN40);
						info.setVoteN41(voteN41);
						info.setVoteN42(voteN42);
						info.setVoteN43(voteN43);
						info.setVoteN44(voteN44);
						info.setVoteN45(voteN45);
						info.setVoteN46(voteN46);
						info.setVoteN47(voteN47);
						info.setVoteN48(voteN48);
						
						int resultCnt = mainService.addVote(info);
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
	
	// 투표 내역 가져오기 액션
	@RequestMapping(value = "voteUserListAjax.json")
	public ModelAndView voteUserListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("voteUserListAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String type = StringUtil.reqNullCheck(request, "type");
				
				if(type.equals("1")){
					List<VoteInfo> list = mainService.getNotParticipationList();
					mav.addObject("list", list);
				} else if(type.equals("2")){
					List<VoteInfo> list = mainService.getParticipationList();
					mav.addObject("list", list);
				} else if(type.equals("3")){
					List<ReportInfo> list = mainService.getVoteStatistics();
					mav.addObject("list", list);
				}
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
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
