package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import kr.co.reyonpharm.models.ConfirmInfo;
import kr.co.reyonpharm.models.DeptInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TakeOverInfo;
import kr.co.reyonpharm.models.TicketInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.service.TicketService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ticket/")
public class TicketController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	TicketService ticketService;

	@Autowired
	CommonService commonService;

	/* 주차권 관리 메뉴 */

	// 주차권 관리 목록
	@RequestMapping(value = "ticketList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ticketList(HttpServletRequest request, ModelAndView mav) {
		log.info("ticketList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "s_visitCompany");
		String s_visitName = StringUtil.reqNullCheckHangulUTF8(request, "s_visitName");
		String s_meetingName = StringUtil.reqNullCheckHangulUTF8(request, "s_meetingName");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_refdeptCode = StringUtil.reqNullCheckHangulUTF8(request, "s_refdeptCode");
		String s_deptCode = StringUtil.reqNullCheckHangulUTF8(request, "s_deptCode");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();
		String respon = SpringSecurityUtil.getRespon();
		String roleName = SpringSecurityUtil.getRoleName();

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_visitCompany(s_visitCompany);
		pageParam.setS_visitName(s_visitName);
		pageParam.setS_meetingName(s_meetingName);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_refdeptCode(s_refdeptCode);
		pageParam.setS_deptCode(s_deptCode);
		pageParam.setS_regName(s_regName);
		pageParam.setS_status(s_status);

		// 결재자 정보
		ConfirmInfo confirmInfoParam = new ConfirmInfo();
		confirmInfoParam.setSabun(sabun);
		List<ConfirmInfo> confirmPersonList = commonService.getConfirmInfoList(confirmInfoParam);

		if (!roleName.equals("ROLE_SUPERUSER") && !roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CHONGMU")) {
			// SUPERUSER가 아니면서 총무팀이 아니면

			// 받은 인수 리스트
			TakeOverInfo receiveParam = new TakeOverInfo();
			receiveParam.setReceiveSabun(sabun);
			receiveParam.setStatus("AA");
			List<TakeOverInfo> receiveList = commonService.getTakeoverList(receiveParam);
			List<String> sabunList = new ArrayList<String>();
			for (int i = 0; i < receiveList.size(); i++) {
				sabunList.add(receiveList.get(i).getGiveSabun());
			}
			sabunList.add(sabun);

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
				// pageParam.setAttSabun(sabun);
				pageParam.setAttSabunList(sabunList);
				pageParam.setDeptCodeList(deptCodeList);
			} else {
				if (confirmPersonList.size() != 0) {
					// 결재자
					List<String> deptCodeList = new ArrayList<String>();
					for (int i = 0; i < confirmPersonList.size(); i++) {
						deptCodeList.add(confirmPersonList.get(i).getDeptCode());
					}
					deptCodeList.add(deptCode);
					// pageParam.setAttSabun(sabun);
					pageParam.setAttSabunList(sabunList);
					pageParam.setDeptCodeList(deptCodeList);
				} else {
					// 팀원
					pageParam.setSabun(sabun);
					pageParam.setSabunList(sabunList);
				}
			}
		}

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = ticketService.getTicketListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<TicketInfo> list = ticketService.getTicketList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("ticket/ticketList");
		return mav;
	}

	// 주차권 관리 상세
	@RequestMapping(value = "ticketView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ticketView(HttpServletRequest request, ModelAndView mav) {
		log.info("ticketView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "parkingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();
		String deptCode = SpringSecurityUtil.getDeptCode();

		TicketInfo param = new TicketInfo();
		param.setParkingSeq(seq);
		TicketInfo info = ticketService.getTicket(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		// 총무팀
		boolean isGeneralMagager = false;
		if (deptCode.equals("1040")) {
			isGeneralMagager = true;
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isMine", isMine);
		mav.addObject("isGeneralMagager", isGeneralMagager);
		mav.setViewName("ticket/ticketView");
		return mav;
	}

	// 주차권 관리 등록
	@RequestMapping(value = "ticketAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ticketAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("ticketAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String deptCode = SpringSecurityUtil.getDeptCode();
		String deptName = SpringSecurityUtil.getDeptName();
		// 총무팀
		boolean isGeneralMagager = false;
		if (deptCode.equals("1040")) {
			isGeneralMagager = true;
		}
		mav.addObject("isGeneralMagager", isGeneralMagager);
		mav.addObject("deptCode", deptCode);
		mav.addObject("deptName", deptName);
		mav.setViewName("ticket/ticketAdd");
		return mav;
	}

	// 주차권 관리 수정
	@RequestMapping(value = "ticketModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ticketModify(HttpServletRequest request, ModelAndView mav) {
		log.info("ticketModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "parkingSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		String deptCode = SpringSecurityUtil.getDeptCode();

		// 총무팀
		boolean isGeneralMagager = false;
		if (deptCode.equals("1040")) {
			isGeneralMagager = true;
		}

		// 총무팀만 수정 가능
		/*
		 * if (!deptCode.equals("1040")) { throw new
		 * CustomException(CustomExceptionCodes.NOT_AUTHORIZED); }
		 */

		TicketInfo param = new TicketInfo();
		param.setParkingSeq(seq);
		TicketInfo info = ticketService.getTicket(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		// 본인만 수정 가능
		/*
		 * String sabun = SpringSecurityUtil.getUsername(); if
		 * (!sabun.equals(info.getRegSabun())) { throw new
		 * CustomException(CustomExceptionCodes.NOT_AUTHORIZED); }
		 */

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isGeneralMagager", isGeneralMagager);
		mav.setViewName("ticket/ticketModify");
		return mav;
	}

	// 주차권 관리 등록 액션
	@RequestMapping(value = "ticketAddAjax.json")
	public ModelAndView ticketAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("ticketAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String visitSeq = StringUtil.reqNullCheck(request, "visitSeq");
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String visitPurpose = StringUtil.reqNullCheckHangulUTF8(request, "visitPurpose");
				String refDeptCode = StringUtil.reqNullCheck(request, "refDeptCode");
				String refDeptName = StringUtil.reqNullCheckHangulUTF8(request, "refDeptName");
				// String giveDate = StringUtil.reqNullCheck(request, "giveDate");
				String countHour2 = StringUtil.reqNullCheck(request, "countHour2");
				String countHour3 = StringUtil.reqNullCheck(request, "countHour3");
				String countHour4 = StringUtil.reqNullCheck(request, "countHour4");
				String countHour6 = StringUtil.reqNullCheck(request, "countHour6");
				String countHour24 = StringUtil.reqNullCheck(request, "countHour24");
				String isWebSale = StringUtil.reqNullCheck(request, "isWebSale");
				String webSalePrice = StringUtil.reqNullCheck(request, "webSalePrice");

				if ("".equals(visitCompany) || "".equals(visitName) || "".equals(visitPurpose) || "".equals(refDeptCode) || "".equals(refDeptName)) {
					log.error("visitCompany : " + visitCompany + ", visitName : " + visitName + ", visitPurpose : " + visitPurpose + ", refDeptCode : " + refDeptCode + ", refDeptName : " + refDeptName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String deptCode = SpringSecurityUtil.getDeptCode();
					String deptName = SpringSecurityUtil.getDeptName();
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					TicketInfo info = new TicketInfo();
					info.setVisitSeq(visitSeq);
					info.setVisitCompany(visitCompany);
					info.setVisitName(visitName);
					info.setVisitPurpose(visitPurpose);
					info.setRefDeptCode(refDeptCode);
					info.setRefDeptName(refDeptName);
					// info.setGiveDate(giveDate);
					info.setCountHour2(countHour2);
					info.setCountHour3(countHour3);
					info.setCountHour4(countHour4);
					info.setCountHour6(countHour6);
					info.setCountHour24(countHour24);
					info.setIsWebSale(isWebSale);
					info.setWebSalePrice(webSalePrice);
					info.setStatus("01");
					info.setDeptCode(deptCode);
					info.setDeptName(deptName);
					info.setRegSabun(regSabun);
					info.setRegName(regName);

					if (deptCode.equals("1040")) {
						info.setStatus("AA");
						info.setConfirmSabun(regSabun);
						info.setConfirmName(regName);
					}

					int resultCnt = ticketService.addTicket(info);

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

	// 주차권 관리 수정 액션
	@RequestMapping(value = "ticketModifyAjax.json")
	public ModelAndView ticketModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("ticketModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String parkingSeq = StringUtil.reqNullCheck(request, "parkingSeq");
				String visitSeq = StringUtil.reqNullCheck(request, "visitSeq");
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String visitPurpose = StringUtil.reqNullCheckHangulUTF8(request, "visitPurpose");
				String refDeptCode = StringUtil.reqNullCheck(request, "refDeptCode");
				String refDeptName = StringUtil.reqNullCheckHangulUTF8(request, "refDeptName");
				String giveDate = StringUtil.reqNullCheck(request, "giveDate");
				String countHour2 = StringUtil.reqNullCheck(request, "countHour2");
				String countHour3 = StringUtil.reqNullCheck(request, "countHour3");
				String countHour4 = StringUtil.reqNullCheck(request, "countHour4");
				String countHour6 = StringUtil.reqNullCheck(request, "countHour6");
				String countHour24 = StringUtil.reqNullCheck(request, "countHour24");
				String isWebSale = StringUtil.reqNullCheck(request, "isWebSale");
				String webSalePrice = StringUtil.reqNullCheck(request, "webSalePrice");

				if ("".equals(visitCompany) || "".equals(visitName) || "".equals(visitPurpose) || "".equals(refDeptCode) || "".equals(refDeptName)) {
					log.error("visitCompany : " + visitCompany + ", visitName : " + visitName + ", visitPurpose : " + visitPurpose + ", refDeptCode : " + refDeptCode + ", refDeptName : " + refDeptName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					TicketInfo info = new TicketInfo();
					info.setParkingSeq(parkingSeq);
					info.setVisitSeq(visitSeq);
					info.setVisitCompany(visitCompany);
					info.setVisitName(visitName);
					info.setVisitPurpose(visitPurpose);
					info.setRefDeptCode(refDeptCode);
					info.setRefDeptName(refDeptName);
					info.setGiveDate(giveDate);
					info.setCountHour2(countHour2);
					info.setCountHour3(countHour3);
					info.setCountHour4(countHour4);
					info.setCountHour6(countHour6);
					info.setCountHour24(countHour24);
					info.setIsWebSale(isWebSale);
					info.setWebSalePrice(webSalePrice);
					// 본인만 수정가능
					// String regSabun = SpringSecurityUtil.getUsername();
					// info.setRegSabun(regSabun);

					int resultCnt = ticketService.modifyTicket(info);

					if (resultCnt == 0) {
						log.error("resultCnt : " + resultCnt);
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
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

	// 주차권 관리 삭제 액션
	@RequestMapping(value = "ticketDeleteAjax.json")
	public ModelAndView ticketDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("ticketDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String seq = StringUtil.reqNullCheck(request, "seq");

				if ("".equals(seq)) {
					log.error("seq : " + seq);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					TicketInfo info = new TicketInfo();
					info.setParkingSeq(seq);
					// 본인만 삭제 가능
					// String regSabun = SpringSecurityUtil.getUsername();
					// info.setRegSabun(regSabun);

					int resultCnt = ticketService.deleteTicket(info);

					if (resultCnt == 0) {
						log.error("resultCnt : " + resultCnt);
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
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

	// 주차권 지급 확인 액션
	@RequestMapping(value = "ticketConfirmAjax.json")
	public ModelAndView ticketConfirmAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("ticketConfirmAjax.json");
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
					// 로그인 정보
					String deptCode = SpringSecurityUtil.getDeptCode();
					String sabun = SpringSecurityUtil.getUsername();
					String name = SpringSecurityUtil.getKname();

					// 총무팀 지급 완료/거절 가능
					if (!deptCode.equals("1040")) {
						log.error("deptCode : " + deptCode);
						mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
						mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
					} else {
						TicketInfo info = new TicketInfo();
						info.setParkingSeq(seq);
						info.setStatus(status);
						info.setConfirmSabun(sabun);
						info.setConfirmName(name);

						int resultCnt = ticketService.confirmTicket(info);

						if (resultCnt == 0) {
							log.error("resultCnt : " + resultCnt);
							mav.addObject("resultCode", CustomExceptionCodes.NOT_AUTHORIZED.getId());
							mav.addObject("resultMsg", CustomExceptionCodes.NOT_AUTHORIZED.getMsg());
						} else {
							mav.addObject("resultCode", resultCnt);
							mav.addObject("resultMsg", "success");
						}
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
