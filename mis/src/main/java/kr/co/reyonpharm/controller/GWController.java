package kr.co.reyonpharm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.ApproInfo;
import kr.co.reyonpharm.models.HolidayInfo;
import kr.co.reyonpharm.models.OvertimeInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.GWService;
import kr.co.reyonpharm.service.HRService;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/gw/")
public class GWController {
	
	@Autowired
	private GWService gWService;
	
	@Autowired
	private HRService hRService;
	
	// 급여명세서 조회 화면
	// http://192.168.1.84/gw/gwSalaryView.do?userId=MTgwMjEyMDE=
	// http://192.168.1.84/gw/gwSalaryView.do?userId=MTgwMjEyMDE%3D
	// https://ryweb.reyonpharm.co.kr/gw/gwSalaryView.do?userId=MTgwMjEyMDE%3D
	@RequestMapping(value = "gwSalaryView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView gwSalaryView(HttpServletRequest request, ModelAndView mav) {
		log.info("gwSalaryView.do");
		String userId = StringUtil.reqNullCheck(request, "userId");
		log.info("[ENC] userId : " + userId);
		if (userId.equals("")) {
			return new ModelAndView("redirect:/main.do");
		}
		byte[] decoded = Base64.decodeBase64(userId.getBytes());
		userId = new String(decoded);
		log.info("[DEC] userId : " + userId);
		mav.addObject("userId", userId);
		mav.setViewName("gw/gwSalaryView");
		return mav;
	}
	
	// 전자결재 연동 화면
	// 휴가신청서
	// http://192.168.1.84/gw/gwApproval.do?type=MIS003&userId=MTgwMjEyMDE%3D
	// 초과근무신청서
	// http://192.168.1.84/gw/gwApproval.do?type=MIS004&userId=MTgwMjEyMDE%3D
	// 휴가신청서
	// https://ryweb.reyonpharm.co.kr/gw/gwApproval.do?type=MIS003&userId=MTgwMjEyMDE%3D
	// 초과근무신청서
	// https://ryweb.reyonpharm.co.kr/gw/gwApproval.do?type=MIS004&userId=MTgwMjEyMDE%3D
	@RequestMapping(value = "gwApproval.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView gwApproval(HttpServletRequest request, ModelAndView mav) {
		log.info("gwApproval.do");
		String userId = StringUtil.reqNullCheck(request, "userId");
		String type = StringUtil.reqNullCheck(request, "type");
		log.info("[ENC] userId : " + userId);
		if (userId.equals("")) {
			return new ModelAndView("redirect:/main.do");
		}
		byte[] decoded = Base64.decodeBase64(userId.getBytes());
		userId = new String(decoded);
		log.info("[DEC] userId : " + userId);
		
		UserInfo param = new UserInfo();
		param.setSabun(userId);
		UserInfo info = gWService.getUserInfo(param);
		
		mav.addObject("info", info);
		if(type.equals("MIS003")) {
			// 휴가신청서
			mav.setViewName("gw/MIS003");
		} else if(type.equals("MIS004")) {
			// 초과근무신청서
			mav.setViewName("gw/MIS004");
		} else {
			return new ModelAndView("redirect:/main.do");
		}
		return mav;
	}
	
	// 휴가신청서, 초과근무신청서 등록 액션
	@RequestMapping(value = "gwToMisAddAjax.json")
	public ModelAndView gwToMisAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("gwToMisAddAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String type = StringUtil.reqNullCheck(request, "type");
				
				if (type.equals("MIS003")) {
					// 휴가신청서
					String sabun = StringUtil.reqNullCheck(request, "sabun");
					String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
					String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
					String posLog = StringUtil.reqNullCheckHangulUTF8(request, "posLog");
					String holidayGbn = StringUtil.reqNullCheck(request, "holidayGbn");
					String holidayGbnTxt = StringUtil.reqNullCheck(request, "holidayGbnTxt");
					String holidayTotalViewCnt = StringUtil.reqNullCheck(request, "holidayTotalViewCnt");
					String holidayTotalCnt = StringUtil.reqNullCheck(request, "holidayTotalCnt");
					String startdate = StringUtil.reqNullCheck(request, "startdate");
					String enddate = StringUtil.reqNullCheck(request, "enddate");
					String reason = StringUtil.reqNullCheckHangulUTF8(request, "reason");
					String takeover = StringUtil.reqNullCheckHangulUTF8(request, "takeover");
					String status = "1";
					String gwStatus = "2";
					
					HolidayInfo param = new HolidayInfo();
					param.setSabun(sabun);
					param.setKname(kname);
					param.setDeptName(deptName);
					param.setPosLog(posLog);
					param.setHolidayGbn(holidayGbn);
					param.setHolidayGbnTxt(holidayGbnTxt);
					param.setViewMinusCnt(holidayTotalViewCnt);
					param.setHolidayTotalCnt(holidayTotalCnt);
					param.setStartdate(startdate);
					param.setEnddate(enddate);
					param.setReason(reason);
					param.setTakeover(takeover);
					param.setStatus(status);
					param.setGwStatus(gwStatus);
					
					int resultCode = hRService.addMIS003(param);
					if(resultCode == 1) {
						String approKey = type + "-" +String.valueOf(param.getCurrseq());
						String fcode = type;
						String subject = "휴가신청서";
						String contents = hRService.buildMIS003Html(param);
						
						ApproInfo approInfo = new ApproInfo();
						approInfo.setApproKey(approKey);
						approInfo.setFcode(fcode);
						approInfo.setSubject(subject);
						approInfo.setContents(contents);
						
						resultCode = hRService.addApproInfo(approInfo);
						
						if (resultCode == 1) {
							mav.addObject("approKey", approKey);
							mav.addObject("resultCode", resultCode);
							mav.addObject("resultMsg", "success");
						} else {
							mav.addObject("resultCode", -1);
							mav.addObject("resultMsg", "전자결재 TABLE 등록 실패");
						}
					} else {
						mav.addObject("resultCode", -1);
						mav.addObject("resultMsg", "휴가 TABLE 등록 실패");
					}
				} else {
					// 초과근무신청서
					String sabun = StringUtil.reqNullCheck(request, "sabun");
					String kname = StringUtil.reqNullCheckHangulUTF8(request, "kname");
					String deptName = StringUtil.reqNullCheckHangulUTF8(request, "deptName");
					String posLog = StringUtil.reqNullCheckHangulUTF8(request, "posLog");
					String overtimeGbn = StringUtil.reqNullCheck(request, "overtimeGbn");
					String overtimeGbnTxt = StringUtil.reqNullCheckHangulUTF8(request, "overtimeGbnTxt");
					String workingMinute = StringUtil.reqNullCheck(request, "workingMinute");
					String startdate = StringUtil.reqNullCheck(request, "startdate");
					String enddate = StringUtil.reqNullCheck(request, "enddate");
					String restStarttime1 = StringUtil.reqNullCheck(request, "restStarttime1");
					String restEndtime1 = StringUtil.reqNullCheck(request, "restEndtime1");
					String restStarttime2 = StringUtil.reqNullCheck(request, "restStarttime2");
					String restEndtime2 = StringUtil.reqNullCheck(request, "restEndtime2");
					String restStarttime3 = StringUtil.reqNullCheck(request, "restStarttime3");
					String restEndtime3 = StringUtil.reqNullCheck(request, "restEndtime3");
					String reason = StringUtil.reqNullCheckHangulUTF8(request, "reason");
					String evidence = StringUtil.reqNullCheckHangulUTF8(request, "evidence");
					String workerDeptNameStr = StringUtil.reqNullCheckHangulUTF8(request, "workerDeptNameArr");
					String workerSabunStr = StringUtil.reqNullCheckHangulUTF8(request, "workerSabunArr");
					String workerKnameStr = StringUtil.reqNullCheckHangulUTF8(request, "workerKnameArr");
					String workerPosLogStr = StringUtil.reqNullCheckHangulUTF8(request, "workerPosLogArr");
					String[] workerDeptNameArr = workerDeptNameStr.split(",");
					String[] workerSabunArr = workerSabunStr.split(",");
					String[] workerKnameArr = workerKnameStr.split(",");
					String[] workerPosLogArr = workerPosLogStr.split(",");
					String status = "1";
					String gwStatus = "2";
					
					OvertimeInfo param = new OvertimeInfo();
					param.setOvertimeGbn(overtimeGbn);
					param.setOvertimeGbnTxt(overtimeGbnTxt);
					param.setStartdate(startdate);
					param.setEnddate(enddate);
					param.setRestStarttime1(restStarttime1);
					param.setRestEndtime1(restEndtime1);
					param.setRestStarttime2(restStarttime2);
					param.setRestEndtime2(restEndtime2);
					param.setRestStarttime3(restStarttime3);
					param.setRestEndtime3(restEndtime3);
					param.setWorkingMinute(workingMinute);
					param.setReason(reason);
					param.setEvidence(evidence);
					param.setStatus(status);
					param.setRegSabun(sabun);
					param.setUpdSabun(sabun);
					param.setGwStatus(gwStatus);
					param.setWorkerDeptNameArr(workerDeptNameArr);
					param.setWorkerSabunArr(workerSabunArr);
					param.setWorkerKnameArr(workerKnameArr);
					param.setWorkerPosLogArr(workerPosLogArr);
					param.setKname(kname);
					param.setDeptName(deptName);
					param.setPosLog(posLog);
					
					int resultCode = hRService.addMIS004(param);
					if(resultCode == 1) {
						String overtimeSeq = String.valueOf(param.getCurrseq());
						param.setOvertimeSeq(overtimeSeq);
						int resultCodeByWorker = 0;
						for (int i = 0; i < workerSabunArr.length; i++) {
							// 근무자 정보
							param.setSabun(workerSabunArr[i]);
							param.setOrderSeq((i+1));
							resultCodeByWorker += hRService.addMIS004Worker(param);
							log.info("[" + overtimeSeq + "] [" + workerSabunArr[i] + "]");
						}
						
						if(resultCodeByWorker != workerSabunArr.length) {
							mav.addObject("resultCode", -1);
							mav.addObject("resultMsg", "근무자 TABLE 등록 실패");
						} else {
							String approKey = type + "-" +overtimeSeq;
							String fcode = type;
							String subject = "초과근무신청서";
							String contents = hRService.buildMIS004Html(param);
							
							ApproInfo approInfo = new ApproInfo();
							approInfo.setApproKey(approKey);
							approInfo.setFcode(fcode);
							approInfo.setSubject(subject);
							approInfo.setContents(contents);
							
							resultCode = hRService.addApproInfo(approInfo);
							
							if (resultCode == 1) {
								mav.addObject("approKey", approKey);
								mav.addObject("resultCode", resultCode);
								mav.addObject("resultMsg", "success");
							} else {
								mav.addObject("resultCode", -1);
								mav.addObject("resultMsg", "전자결재 TABLE 등록 실패");
							}
						}
					} else {
						mav.addObject("resultCode", -1);
						mav.addObject("resultMsg", "초과근무신청서 TABLE 등록 실패");
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
	
}
