package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.PcMessageInfo;
import kr.co.reyonpharm.models.PcOffInfo;
import kr.co.reyonpharm.models.PcProgramInfo;
import kr.co.reyonpharm.models.PcReportInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.AppService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;

@Controller
@RequestMapping("/app/")
public class AppController extends CustomExceptionHandler {

	private static Logger log = LoggerFactory.getLogger("AppFileLog");

	@Autowired
	AppService appService;

	// 이연제약 PC 제어 통신 액션
	@RequestMapping(value = "reyonApp.json")
	@ResponseBody
	public Map reyonApp(HttpServletRequest request, HttpServletResponse response) {
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String type = StringUtil.reqNullCheck(request, "type");
				String username = StringUtil.reqNullCheck(request, "username");
				String ip = StringUtil.reqNullCheck(request, "ip");
				log.info("type : " + type + ", username : " + username + ", ip : " + ip);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Calendar c1 = Calendar.getInstance();
				String today = sdf.format(c1.getTime());

				if (type.equals("report")) {
					// 실시간 접속 보고
					PcReportInfo param = new PcReportInfo();
					param.setIp(ip);
					param.setUsername(username);
					param.setReportDay(today);
					PcReportInfo pcReportInfo = appService.getPcReportInfoList(param);

					int resultCnt = 0;
					if (pcReportInfo != null) {
						resultCnt = appService.modifyPcReportInfo(pcReportInfo);
					} else {
						resultCnt = appService.addPcReportInfo(param);
					}

					result.put("resultCode", resultCnt);
					result.put("resultMsg", "success");
				} else if (type.equals("alert")) {
					// 메시지 노출
					PcMessageInfo param = new PcMessageInfo();
					param.setUsername(username);
					List<PcMessageInfo> list = appService.getPcMessageInfoList(param);

					if (list != null) {
						if (list.size() != 0) {
							PcMessageInfo info = list.get(0);
							int resultCnt = appService.modifyPcMessageInfo(info);
							result.put("resultCode", resultCnt);
							result.put("resultMsg", info.getContents());
						} else {
							result.put("resultCode", 0);
							result.put("resultMsg", "message length is zero");
						}
					} else {
						result.put("resultCode", 0);
						result.put("resultMsg", "message list is null");
					}
				} else if (type.equals("message")) {
					// PC 종료 안내 메시지 노출
					PcOffInfo param = new PcOffInfo();
					// param.setUsername(username);
					// param.setOffDay(today);

					param.setSabun(username);
					param.setReqDay(today);
					param.setGbn("2");
					param.setStatus("1");

					List<PcOffInfo> list = appService.getPcOffInfo(param);

					if (list.size() == 0) {
						// 특근 신청 정보가 없을때

						// 사업장 정보
						UserInfo userParam = new UserInfo();
						userParam.setSabun(username);
						UserInfo userInfo = appService.getUserInfo(userParam);

						// 기준시간 18:00 , 공장 17:30
						String time = "1800";
						String timeStr = "18:00";
						if (userInfo != null) {
							if (userInfo.getSaupcode().equals("20")) {
								time = "1730";
								timeStr = "17:30";
							}
						}
						String standardTime = today + time;

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
						Date reqDate = dateFormat.parse(standardTime);
						long reqDateTime = reqDate.getTime();
						Date curDate = new Date();
						curDate = dateFormat.parse(dateFormat.format(curDate));
						long curDateTime = curDate.getTime();
						long minute = (reqDateTime - curDateTime) / 60000;

						if (minute == 30 || minute == 20 || minute == 10 || minute == 5) {
							result.put("resultCode", 1);
							result.put("resultMsg", timeStr);
						} else if (minute <= 0) {
							result.put("resultCode", 2);
							result.put("resultMsg", "success");
						} else {
							result.put("resultCode", 0);
							result.put("resultMsg", "success");
						}
					} else if (list.size() == 1) {
						// 특근 신청 정보가 1건만 있을때
						PcOffInfo pcOffInfo = list.get(0);
						if (pcOffInfo.getLimitTime() == 30 || pcOffInfo.getLimitTime() == 20 || pcOffInfo.getLimitTime() == 10 || pcOffInfo.getLimitTime() == 5) {
							result.put("resultCode", 1);
							result.put("resultMsg", pcOffInfo.getIptTo().substring(0, 2) + ":" + pcOffInfo.getIptTo().substring(2, 4));
						} else if (pcOffInfo.getLimitTime() <= 0) {
							result.put("resultCode", 2);
							result.put("resultMsg", "success");
						} else {
							result.put("resultCode", 0);
							result.put("resultMsg", "success");
						}
					} else {
						// 특근 신청 정보가 여러건 있을때
						PcOffInfo pcOffInfoMax = list.get(0);
						for (int i = 1; i < list.size(); i++) {
							PcOffInfo pcOffInfo = list.get(i);
							if (Integer.parseInt(pcOffInfo.getIptTo()) >= Integer.parseInt(pcOffInfoMax.getIptTo())) {
								pcOffInfoMax = pcOffInfo;
							}
						}
						if (pcOffInfoMax.getLimitTime() == 30 || pcOffInfoMax.getLimitTime() == 20 || pcOffInfoMax.getLimitTime() == 10 || pcOffInfoMax.getLimitTime() == 5) {
							result.put("resultCode", 1);
							result.put("resultMsg", pcOffInfoMax.getIptTo().substring(0, 2) + ":" + pcOffInfoMax.getIptTo().substring(2, 4));
						} else if (pcOffInfoMax.getLimitTime() <= 0) {
							result.put("resultCode", 2);
							result.put("resultMsg", "success");
						} else {
							result.put("resultCode", 0);
							result.put("resultMsg", "success");
						}
					}
				} else if (type.equals("wakeup")) {
					// 프로세스 살리기/죽이기
					PcProgramInfo param = new PcProgramInfo();
					param.setUsername(username);
					PcProgramInfo info = appService.getPcProgramInfo(param);
					if (info != null) {
						if (info.getRunYn().equals("Y")) {
							// 프로그램 살리기
							result.put("resultCode", 0);
							result.put("resultMsg", "success");
						} else if (info.getRunYn().equals("N")) {
							// 프로그램 죽이기
							result.put("resultCode", 1);
							result.put("resultMsg", "success");
						}
					} else {
						// 결과가 없으므로 프로그램 살리기
						result.put("resultCode", 0);
						result.put("resultMsg", "success");
					}
				}
			} else {
				log.error("auth : " + auth);
				result.put("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
				result.put("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			result.put("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			result.put("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return result;
	}

	// PC ON/OFF 보고 목록
	@RequestMapping(value = "pcOnOffList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcOnOffList(HttpServletRequest request, ModelAndView mav) {
		log.info("pcOnOffList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_sabun = StringUtil.reqNullCheckHangulUTF8(request, "s_sabun");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String s_ip = StringUtil.reqNullCheckHangulUTF8(request, "s_ip");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_sabun(s_sabun);
		pageParam.setS_regName(s_regName);
		pageParam.setS_ip(s_ip);
		pageParam.setStartDate(startDate);
		
		int listCnt = appService.getPcReportInfoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<PcReportInfo> list = appService.getPcReportInfoListByRms(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("app/pcOnOffList");
		return mav;
	}

	// PC 메시지 전송 목록
	@RequestMapping(value = "pcMessageList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcMessageList(HttpServletRequest request, ModelAndView mav) {
		log.info("pcMessageList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		
		String s_sabun = StringUtil.reqNullCheckHangulUTF8(request, "s_sabun");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String s_contents = StringUtil.reqNullCheckHangulUTF8(request, "s_contents");
		String regDate = StringUtil.reqNullCheckHangulUTF8(request, "regDate");
		String sendDate = StringUtil.reqNullCheckHangulUTF8(request, "sendDate");
		String receiveDate = StringUtil.reqNullCheckHangulUTF8(request, "receiveDate");

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_sabun(s_sabun);
		pageParam.setS_regName(s_regName);
		pageParam.setS_contents(s_contents);
		pageParam.setRegDate(regDate);
		pageParam.setSendDate(sendDate);
		pageParam.setReceiveDate(receiveDate);

		int listCnt = appService.getPcMessageInfoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<PcMessageInfo> list = appService.getPcMessageInfoListByRms(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("app/pcMessageList");
		return mav;
	}

	// PC 메시지 전송 등록
	@RequestMapping(value = "pcMessageAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcMessageAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("pcMessageAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("app/pcMessageAdd");
		return mav;
	}

	// PC 메시지 전송 등록 액션
	@RequestMapping(value = "pcMessageAddAjax.json")
	public ModelAndView pcMessageAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcMessageAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String contents = StringUtil.reqNullCheckHangulUTF8(request, "contents");
			String sendDate = StringUtil.reqNullCheck(request, "sendDate");
			String sabunList = StringUtil.reqNullCheck(request, "sabunList");
			if (auth.equals("reyon")) {
				if ("".equals(contents) || "".equals(sendDate) || "".equals(sabunList)) {
					log.error("contents : " + contents + ", sendDate : " + sendDate + ", sabunList : " + sabunList);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String[] sabunArr = sabunList.split(",");
					int resultCnt = 0;
					for (int i = 0; i < sabunArr.length; i++) {
						PcMessageInfo param = new PcMessageInfo();
						param.setUsername(sabunArr[i]);
						param.setContents(contents);
						param.setSendDate(sendDate);
						param.setMessageYn("N");
						resultCnt += appService.addPcMessageInfo(param);
					}
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", resultCnt);
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

	// PC 메시지 전송 삭제 액션
	@RequestMapping(value = "pcMessageDeleteAjax.json")
	public ModelAndView pcMessageDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcMessageDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String messageSeq = StringUtil.reqNullCheck(request, "messageSeq");
			if (auth.equals("reyon")) {
				if ("".equals(messageSeq)) {
					log.error("messageSeq : " + messageSeq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					PcMessageInfo param = new PcMessageInfo();
					param.setMessageSeq(messageSeq);
					int resultCnt = appService.deletePcMessageInfo(param);
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

	// PC 종료 해제 목록
	@RequestMapping(value = "pcReleaseList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcReleaseList(HttpServletRequest request, ModelAndView mav) {
		log.info("pcReleaseList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_sabun = StringUtil.reqNullCheckHangulUTF8(request, "s_sabun");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String regDate = StringUtil.reqNullCheckHangulUTF8(request, "regDate");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_sabun(s_sabun);
		pageParam.setS_regName(s_regName);
		pageParam.setRegDate(regDate);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_status(s_status);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = appService.getPcProgramInfoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<PcProgramInfo> list = appService.getPcProgramInfoListByRms(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("app/pcReleaseList");
		return mav;
	}

	// PC 종료 해제 상세
	@RequestMapping(value = "pcReleaseView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcReleaseView(HttpServletRequest request, ModelAndView mav) {
		log.info("pcReleaseView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(reqSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		PcProgramInfo param = new PcProgramInfo();
		param.setReqSeq(reqSeq);
		PcProgramInfo info = appService.getPcProgramInfoByRms(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("reqSeq", reqSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("app/pcReleaseView");
		return mav;
	}

	// PC 종료 해제 등록
	@RequestMapping(value = "pcReleaseAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcReleaseAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("pcReleaseAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("app/pcReleaseAdd");
		return mav;
	}

	// PC 종료 해제 등록 액션
	@RequestMapping(value = "pcReleaseAddAjax.json")
	public ModelAndView pcReleaseAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcReleaseAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String username = StringUtil.reqNullCheck(request, "username");
			String startDate = StringUtil.reqNullCheck(request, "startDate");
			String endDate = StringUtil.reqNullCheck(request, "endDate");
			String runYn = StringUtil.reqNullCheck(request, "runYn");
			String reqComment = StringUtil.reqNullCheckHangulUTF8(request, "reqComment");
			String status = StringUtil.reqNullCheck(request, "status");
			if (auth.equals("reyon")) {
				if ("".equals(username) || "".equals(startDate) || "".equals(endDate) || "".equals(runYn) || "".equals(reqComment) || "".equals(status)) {
					log.error("username : " + username + ", startDate : " + startDate + ", endDate : " + endDate + ", runYn : " + runYn + ", reqComment : " + reqComment + ", status : " + status);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					PcProgramInfo param = new PcProgramInfo();
					param.setUsername(username);
					param.setStartDate(startDate);
					param.setEndDate(endDate);
					param.setRunYn(runYn);
					param.setReqComment(reqComment);
					param.setStatus(status);
					int resultCnt = appService.addPcProgramInfo(param);
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

	// PC 종료 해제 수정
	@RequestMapping(value = "pcReleaseModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcReleaseModify(HttpServletRequest request, ModelAndView mav) {
		log.info("pcReleaseModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(reqSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		PcProgramInfo param = new PcProgramInfo();
		param.setReqSeq(reqSeq);
		PcProgramInfo info = appService.getPcProgramInfoByRms(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("reqSeq", reqSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("app/pcReleaseModify");
		return mav;
	}

	// PC 종료 해제 수정 액션
	@RequestMapping(value = "pcReleaseModifyAjax.json")
	public ModelAndView pcReleaseModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcReleaseModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
			String username = StringUtil.reqNullCheck(request, "username");
			String startDate = StringUtil.reqNullCheck(request, "startDate");
			String endDate = StringUtil.reqNullCheck(request, "endDate");
			String runYn = StringUtil.reqNullCheck(request, "runYn");
			String reqComment = StringUtil.reqNullCheckHangulUTF8(request, "reqComment");
			String status = StringUtil.reqNullCheck(request, "status");
			if (auth.equals("reyon")) {
				if ("".equals(reqSeq) || "".equals(username) || "".equals(startDate) || "".equals(endDate) || "".equals(runYn) || "".equals(reqComment) || "".equals(status)) {
					log.error("username : " + username + ", startDate : " + startDate + ", endDate : " + endDate + ", runYn : " + runYn);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					PcProgramInfo param = new PcProgramInfo();
					param.setReqSeq(reqSeq);
					param.setUsername(username);
					param.setStartDate(startDate);
					param.setEndDate(endDate);
					param.setRunYn(runYn);
					param.setReqComment(reqComment);
					param.setStatus(status);
					int resultCnt = appService.modifyPcProgramInfo(param);
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

	// PC 종료 해제 승인/거절 목록
	@RequestMapping(value = "pcReleaseConfirm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView pcReleaseConfirm(HttpServletRequest request, ModelAndView mav) {
		log.info("pcReleaseConfirm.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("app/pcReleaseConfirm");
		return mav;
	}

	// PC 종료 해제 승인/거절 목록
	@RequestMapping(value = "getPcReleaseConfirmAjax.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getPcReleaseConfirmAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getPcReleaseConfirmAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				PcProgramInfo param = new PcProgramInfo();
				List<PcProgramInfo> list = appService.getPcProgramInfoConfirmList(param);
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

	// PC 종료 해제 승인/거절 액션
	@RequestMapping(value = "pcReleaseConfirmAjax.json")
	public ModelAndView pcReleaseConfirmAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcReleaseConfirmAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
			String status = StringUtil.reqNullCheck(request, "status");
			String confirmComment = StringUtil.reqNullCheckHangulUTF8(request, "confirmComment");
			if (auth.equals("reyon")) {
				if ("".equals(reqSeq) || "".equals(status)) {
					log.error("reqSeq : " + reqSeq + ", status : " + status);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String confirmSabun = SpringSecurityUtil.getUsername();
					String confirmName = SpringSecurityUtil.getKname();

					PcProgramInfo param = new PcProgramInfo();
					param.setReqSeq(reqSeq);
					param.setStatus(status);
					param.setConfirmComment(confirmComment);
					param.setConfirmSabun(confirmSabun);
					param.setConfirmName(confirmName);
					int resultCnt = appService.confirmPcProgramInfo(param);
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

	// PC 종료 해제 삭제 액션
	@RequestMapping(value = "pcReleaseDeleteAjax.json")
	public ModelAndView pcReleaseDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pcReleaseDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
			if (auth.equals("reyon")) {
				if ("".equals(reqSeq)) {
					log.error("reqSeq : " + reqSeq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					PcProgramInfo param = new PcProgramInfo();
					param.setReqSeq(reqSeq);
					int resultCnt = appService.deletePcProgramInfo(param);
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

}
