package kr.co.reyonpharm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.DevService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/dev/")
public class DevController extends CustomExceptionHandler {

	@Autowired
	DevService devService;

	// 스마트폰 알람 페이지
	@RequestMapping(value = "alarmList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView alarmList(HttpServletRequest request, ModelAndView mav) {
		log.info("alarmList.do");
		mav.setViewName("dev/alarmList");
		return mav;
	}

	// 스마트폰 알람 전송 이력
	@RequestMapping(value = "getAlarmHistoryListAjax.json")
	public ModelAndView getAlarmHistoryListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getAlarmHistoryListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				List<AlarmInfo> list = devService.getAlarmHistoryList();
				mav.addObject("list", list);
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

	// 스마트폰 사용자 리스트 가져오기
	@RequestMapping(value = "getAlarmUserListAjax.json")
	public ModelAndView getAlarmUserListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getAlarmUserListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				List<UserInfo> list = devService.getAlarmUserList();
				mav.addObject("list", list);
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

	// 스마트폰 알람 전송 등록
	@RequestMapping(value = "alarmAddAjax.json")
	public ModelAndView alarmAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("alarmAddAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String title = StringUtil.reqNullCheckHangulUTF8(request, "title");
			String msg = StringUtil.reqNullCheckHangulUTF8(request, "msg");
			String sendDate = StringUtil.reqNullCheck(request, "sendDate");
			String sabunList = StringUtil.reqNullCheck(request, "sabunList");
			String nameList = StringUtil.reqNullCheck(request, "nameList");

			String sabun = SpringSecurityUtil.getUsername();
			String name = SpringSecurityUtil.getKname();

			if (auth.equals("reyon")) {
				if ("".equals(title) || "".equals(msg) || "".equals(sendDate) || "".equals(sabunList) || "".equals(nameList)) {
					log.error("title : " + title + ", msg : " + msg + ", sabunList : " + sabunList + ", nameList : " + nameList);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String[] sabunArr = sabunList.split(",");
					String[] nameArr = nameList.split(",");

					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < sabunArr.length; i++) {
						AlarmInfo param = new AlarmInfo();
						param.setAlarmSabun(sabunArr[i]);
						param.setAlarmDate(sendDate);
						param.setAlarmYn("N");
						param.setAlarmTitle(title);
						param.setAlarmContents(msg);
						param.setRegSabun(sabun);
						param.setRegName(name);

						int resultCnt = devService.addAlarm(param);

						sb.append("<tr>");
						sb.append("<td class=\"text-center\">" + (i + 1) + "</td>");
						sb.append("<td class=\"text-center\">" + sabunArr[i] + "</td>");
						sb.append("<td class=\"text-center\">" + nameArr[i] + "</td>");
						sb.append("<td class=\"text-center\">" + resultCnt + "</td>");
						if (resultCnt == 1) {
							sb.append("<td class=\"text-center\">발송 등록 성공</td>");
						} else {
							sb.append("<td class=\"text-center\">발송 등록 실패</td>");
						}
						Date today = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						sb.append("<td class=\"text-center\">" + sdf.format(today) + "</td>");
						sb.append("</tr>");
					}
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", sb.toString());
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

	// 스마트폰 알람 전송 삭제
	@RequestMapping(value = "alarmDeleteAjax.json")
	public ModelAndView alarmDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("alarmDeleteAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String seq = StringUtil.reqNullCheck(request, "seq");

			if (auth.equals("reyon")) {
				if ("".equals(seq)) {
					log.error("seq : " + seq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					AlarmInfo param = new AlarmInfo();
					param.setAlarmSeq(seq);
					int resultCnt = devService.deleteAlarm(param);
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

	// 사진 조회 페이지
	@RequestMapping(value = "photoView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView photoView(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("photoView.do");
		mav.setViewName("dev/photoView");
		return mav;
	}

	// 사진 조회 액션
	@RequestMapping(value = "userPhotoView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView userPhotoView(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		String sabun = StringUtil.reqNullCheck(request, "sabun");
		if ("".equals(sabun)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}
		return new ModelAndView("rawDataImage", "sabun", sabun);
	}
	
	// 지도 테스트
	@RequestMapping(value = "map.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView map(HttpServletRequest request, ModelAndView mav) {
		log.info("map.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("dev/map");
		return mav;
	}

}
