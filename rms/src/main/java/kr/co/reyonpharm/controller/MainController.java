package kr.co.reyonpharm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.MeetingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	CommonService commonService;

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

	// 임원 PC 자동로그인 통과 하기 위한 ajax
	@RequestMapping(value = "checkPassPcAjax.json")
	public ModelAndView checkPassPcAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("checkPassPcAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {

				String clientIp = CommonUtils.getClientIp(request);
				if (clientIp.equals("192.168.1.211")) {
					mav.addObject("resultCode", 1);
					mav.addObject("clientID", "14011701");
					mav.addObject("clientPW", "reyonceo123!@#");
				} else if (clientIp.equals("192.168.1.212")) {
					mav.addObject("resultCode", 1);
					mav.addObject("clientID", "10051401");
					mav.addObject("clientPW", "reyonceo123!@#");
				} else if (clientIp.equals("192.168.1.81")) {
					mav.addObject("resultCode", 1);
					mav.addObject("clientID", "07031201");
					mav.addObject("clientPW", "reyonceo123!@#");
				} else {
					mav.addObject("resultCode", 0);
				}
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

	// 메인 페이지
	@RequestMapping(value = "main.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest request, ModelAndView mav) {
		log.info("main.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		// 사업장 코드 선택 했을경우
		String saupCookie = CommonUtils.getCookiesByRequest(request, "saupcode");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		// 날짜계산
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 금일 날짜 계산
		String strToday = sdf.format(Calendar.getInstance().getTime());
		String todayStartDate = strToday + " 00:00";
		String todayEndDate = strToday + " 23:59";

		// 금주 날짜 계산
		Calendar mon = Calendar.getInstance();
		mon.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Calendar fri = Calendar.getInstance();
		fri.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String weekStartDate = sdf.format(mon.getTime()) + " 00:00";
		String weekEndDate = sdf.format(fri.getTime()) + " 23:59";

		// 공통 파라미터
		PageParam pageParam = new PageParam();

		// 결재자 정보
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setSabun(sabun);
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN")) {
			if (roleName.equals("ROLE_JINCHEONADMIN")) {
				// JINCHEONADMIN 인경우
				pageParam.setSaupCode("20");
			} else {
				// 그외 일반 사용자
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
					pageParam.setAttSabun(sabun);
					pageParam.setDeptCodeList(deptCodeList);
				} else {
					if (confirmPersonList.size() != 0) {
						// 결재자
						List<String> deptCodeList = new ArrayList<String>();
						for (int i = 0; i < confirmPersonList.size(); i++) {
							deptCodeList.add(confirmPersonList.get(i).getDeptCode());
						}
						deptCodeList.add(deptCode);
						pageParam.setAttSabun(sabun);
						pageParam.setDeptCodeList(deptCodeList);
					} else {
						// 팀원
						pageParam.setAttSabun(sabun);
						pageParam.setSabun(sabun);
					}
				}
			}
		}

		// 사업장 코드 설정
		if (!saupCookie.equals("")) {
			pageParam.setSaupCode(saupCookie);
		}

		// 회의 타입 설정
		pageParam.setMeetingType("01");

		// 금일 회의
		pageParam.setStartDate(todayStartDate);
		pageParam.setEndDate(todayEndDate);
		int todayMeetListCnt = mainService.getMainMeetingListCount(pageParam);
		List<MeetingInfo> todayMeetList = mainService.getMainMeetingList(pageParam);

		// 금주 회의
		pageParam.setStartDate(weekStartDate);
		pageParam.setEndDate(weekEndDate);
		int weekMeetListCnt = mainService.getMainMeetingListCount(pageParam);
		List<MeetingInfo> weekMeetList = mainService.getMainMeetingList(pageParam);

		// 방문자 타입 설정
		pageParam.setMeetingType("02");

		// 금일 방문
		pageParam.setStartDate(todayStartDate);
		pageParam.setEndDate(todayEndDate);
		int todayVisitorListCnt = mainService.getMainMeetingListCount(pageParam);
		List<MeetingInfo> todayVisitorList = mainService.getMainMeetingList(pageParam);

		// 금주 방문
		pageParam.setStartDate(weekStartDate);
		pageParam.setEndDate(weekEndDate);
		int weekVisitorListCnt = mainService.getMainMeetingListCount(pageParam);
		List<MeetingInfo> weekVisitorList = mainService.getMainMeetingList(pageParam);

		// 진행 대기 내역 리스트
		PageParam pp = new PageParam();
		if (confirmPersonList.size() != 0) {
			// 결재자
			List<String> deptCodeList = new ArrayList<String>();
			for (int i = 0; i < confirmPersonList.size(); i++) {
				deptCodeList.add(confirmPersonList.get(i).getDeptCode());
			}
			deptCodeList.add(deptCode);
			pp.setSabun(sabun);
			pp.setDeptCodeList(deptCodeList);
		} else {
			// 그 외 인원 (팀원, 본부장, SUPERUSER)
			pp.setSabun(sabun);
		}

		List<MeetingInfo> standbyList = mainService.getTotalTodoList(pp);

		mav.addObject("todayMeetListCnt", todayMeetListCnt);
		mav.addObject("todayMeetList", todayMeetList);
		mav.addObject("weekMeetListCnt", weekMeetListCnt);
		mav.addObject("weekMeetList", weekMeetList);
		mav.addObject("todayVisitorListCnt", todayVisitorListCnt);
		mav.addObject("todayVisitorList", todayVisitorList);
		mav.addObject("weekVisitorListCnt", weekVisitorListCnt);
		mav.addObject("weekVisitorList", weekVisitorList);
		mav.addObject("standbyList", standbyList);

		mav.setViewName("main");
		return mav;
	}

	// 마이 페이지
	@RequestMapping(value = "myPage.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myPage(HttpServletRequest request, ModelAndView mav) {
		log.info("myPage.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("myPage");
		return mav;
	}

	// 투두 리스트
	@RequestMapping(value = "toDo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView toDo(HttpServletRequest request, ModelAndView mav) {
		log.info("toDo.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String searchType = StringUtil.reqNullCheck(request, "searchType");
		String searchText = StringUtil.reqNullCheckHangulUTF8(request, "searchText");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();

		// 공통 파라미터
		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setSearchType(searchType);
		pageParam.setSearchText(searchText);

		// 결재자 정보
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setSabun(sabun);
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		if (confirmPersonList.size() != 0) {
			// 결재자
			List<String> deptCodeList = new ArrayList<String>();
			for (int i = 0; i < confirmPersonList.size(); i++) {
				deptCodeList.add(confirmPersonList.get(i).getDeptCode());
			}
			deptCodeList.add(deptCode);
			pageParam.setSabun(sabun);
			pageParam.setDeptCodeList(deptCodeList);
		} else {
			// 그 외 인원 (팀원, 본부장, SUPERUSER)
			pageParam.setSabun(sabun);
		}

		int listCnt = mainService.getTodoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<MeetingInfo> list = mainService.getTodoList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("toDo");
		return mav;
	}

	// 인수인계 내역
	@RequestMapping(value = "takeOver.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView takeOver(HttpServletRequest request, ModelAndView mav) {
		log.info("takeOver.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		// 받은 인수 리스트
		TakeOverInfo receiveParam = new TakeOverInfo();
		receiveParam.setReceiveSabun(sabun);
		List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);

		// 넘겨준 인계 리스트
		TakeOverInfo giveParam = new TakeOverInfo();
		giveParam.setGiveSabun(sabun);
		List<TakeOverInfo> giveList = mainService.getTakingoverList(giveParam);

		mav.addObject("sabun", sabun);
		mav.addObject("receiveList", receiveList);
		mav.addObject("giveList", giveList);
		mav.setViewName("takeOver");
		return mav;
	}

	// 인수인계 내역 등록 액션
	@RequestMapping(value = "takeOverAddAjax.json")
	public ModelAndView takeOverAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("takeOverAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String receiveSabun = StringUtil.reqNullCheck(request, "receiveSabun");

				if ("".equals(receiveSabun)) {
					log.error("receiveSabun : " + receiveSabun);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String giveSabun = SpringSecurityUtil.getUsername();

					TakeOverInfo param = new TakeOverInfo();
					param.setGiveSabun(giveSabun);
					param.setReceiveSabun(receiveSabun);
					param.setStatus("01");

					int resultCnt = mainService.addtakeOver(param);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
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

	// 인수인계 내역 수락/거절 액션
	@RequestMapping(value = "takeOverModifyAjax.json")
	public ModelAndView takeOverModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("takeOverModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");
				String status = StringUtil.reqNullCheck(request, "status");

				if ("".equals(seq) || "".equals(status)) {
					log.error("seq : " + seq + ", status : " + status);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					TakeOverInfo param = new TakeOverInfo();
					param.setGiveSeq(seq);
					param.setStatus(status);

					int resultCnt = mainService.modifyTakeOver(param);
					if (resultCnt == 0) {
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
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

	// admin 접속
	@RequestMapping(value = "admin.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView admin(HttpServletRequest request, ModelAndView mav) {
		log.info("admin.do");
		String clientIp = CommonUtils.getClientIp(request);
		if (!clientIp.equals("192.168.1.81") && !clientIp.equals("192.168.1.82") && !clientIp.equals("192.168.1.83") && !clientIp.equals("192.168.1.84")) {
			log.error(clientIp);
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}
		mav.setViewName("admin");
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
