package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.ComingInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.TicketInfo;
import kr.co.reyonpharm.service.ComingService;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/coming/")
public class ComingController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	ComingService comingService;

	@Autowired
	CommonService commonService;

	/* 출입자 관리 */

	// 출입자 관리 목록
	@RequestMapping(value = "comingList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingList(HttpServletRequest request, ModelAndView mav) {
		log.info("comingList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "s_visitCompany");
		String s_visitName = StringUtil.reqNullCheckHangulUTF8(request, "s_visitName");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_deptCode = StringUtil.reqNullCheckHangulUTF8(request, "s_deptCode");
		String s_regName = StringUtil.reqNullCheckHangulUTF8(request, "s_regName");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_visitCompany(s_visitCompany);
		pageParam.setS_visitName(s_visitName);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_deptCode(s_deptCode);
		pageParam.setS_regName(s_regName);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = comingService.getComingListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ComingInfo> list = comingService.getComingList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("coming/comingList");
		return mav;
	}

	// 출입자 관리 상세
	@RequestMapping(value = "comingView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingView(HttpServletRequest request, ModelAndView mav) {
		log.info("comingView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "visitSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ComingInfo param = new ComingInfo();
		param.setVisitSeq(seq);
		ComingInfo info = comingService.getComing(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		boolean isMine = false;
		if (sabun.equals(info.getRegSabun())) {
			isMine = true;
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isMine", isMine);
		mav.setViewName("coming/comingView");
		return mav;
	}

	// 출입자 관리 등록
	@RequestMapping(value = "comingAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("comingAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("coming/comingAdd");
		return mav;
	}

	// 출입자 관리 수정
	@RequestMapping(value = "comingModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingModify(HttpServletRequest request, ModelAndView mav) {
		log.info("comingModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "visitSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ComingInfo param = new ComingInfo();
		param.setVisitSeq(seq);
		ComingInfo info = comingService.getComing(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		if (!sabun.equals(info.getRegSabun())) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("coming/comingModify");
		return mav;
	}

	// 출입자 관리 등록 액션
	@RequestMapping(value = "comingAddAjax.json")
	public ModelAndView comingAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("comingAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String visitStartDate = StringUtil.reqNullCheck(request, "visitStartDate");
				String meetDeptCode = StringUtil.reqNullCheck(request, "meetDeptCode");
				String meetDeptName = StringUtil.reqNullCheckHangulUTF8(request, "meetDeptName");
				String meetSabun = StringUtil.reqNullCheck(request, "meetSabun");
				String meetName = StringUtil.reqNullCheckHangulUTF8(request, "meetName");

				if ("".equals(visitCompany) || "".equals(visitName) || "".equals(visitStartDate) || "".equals(meetDeptCode) || "".equals(meetDeptName) || "".equals(meetSabun) || "".equals(meetName)) {
					log.error("visitCompany : " + visitCompany + ", visitName : " + visitName + ", visitStartDate : " + visitStartDate + ", meetDeptCode : " + meetDeptCode + ", meetDeptName : " + meetDeptName + ", meetSabun : " + meetSabun + ", meetName : " + meetName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String regDeptCode = SpringSecurityUtil.getDeptCode();
					String regDeptName = SpringSecurityUtil.getDeptName();
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					ComingInfo info = new ComingInfo();
					info.setVisitCompany(visitCompany);
					info.setVisitName(visitName);
					info.setVisitStartDate(visitStartDate);
					info.setMeetDeptCode(meetDeptCode);
					info.setMeetDeptName(meetDeptName);
					info.setMeetSabun(meetSabun);
					info.setMeetName(meetName);
					info.setRegDeptCode(regDeptCode);
					info.setRegDeptName(regDeptName);
					info.setRegSabun(regSabun);
					info.setRegName(regName);

					int resultCnt = comingService.addComing(info);

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

	// 출입자 관리 수정 액션
	@RequestMapping(value = "comingModifyAjax.json")
	public ModelAndView comingModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("comingModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String visitSeq = StringUtil.reqNullCheck(request, "visitSeq");
				String visitCompany = StringUtil.reqNullCheckHangulUTF8(request, "visitCompany");
				String visitName = StringUtil.reqNullCheckHangulUTF8(request, "visitName");
				String visitStartDate = StringUtil.reqNullCheck(request, "visitStartDate");
				String meetDeptCode = StringUtil.reqNullCheck(request, "meetDeptCode");
				String meetDeptName = StringUtil.reqNullCheckHangulUTF8(request, "meetDeptName");
				String meetSabun = StringUtil.reqNullCheck(request, "meetSabun");
				String meetName = StringUtil.reqNullCheckHangulUTF8(request, "meetName");

				if ("".equals(visitSeq) || "".equals(visitCompany) || "".equals(visitName) || "".equals(visitStartDate) || "".equals(meetDeptCode) || "".equals(meetDeptName) || "".equals(meetSabun) || "".equals(meetName)) {
					log.error("visitSeq : " + visitSeq + ", visitCompany : " + visitCompany + ", visitName : " + visitName + ", visitStartDate : " + visitStartDate + ", meetDeptCode : " + meetDeptCode + ", meetDeptName : " + meetDeptName + ", meetSabun : " + meetSabun + ", meetName : " + meetName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					// 로그인 정보
					String regSabun = SpringSecurityUtil.getUsername();

					ComingInfo info = new ComingInfo();
					info.setVisitSeq(visitSeq);
					info.setVisitCompany(visitCompany);
					info.setVisitName(visitName);
					info.setVisitStartDate(visitStartDate);
					info.setMeetDeptCode(meetDeptCode);
					info.setMeetDeptName(meetDeptName);
					info.setMeetSabun(meetSabun);
					info.setMeetName(meetName);
					info.setRegSabun(regSabun);

					int resultCnt = comingService.modifyComing(info);

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

	// 출입자 관리 삭제 액션
	@RequestMapping(value = "comingDeleteAjax.json")
	public ModelAndView comingDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("comingDeleteAjax.json");
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
					// 로그인 정보
					String regSabun = SpringSecurityUtil.getUsername();

					ComingInfo info = new ComingInfo();
					info.setVisitSeq(seq);
					info.setRegSabun(regSabun);

					int resultCnt = comingService.deleteComing(info);

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

	// 통계 관리
	@RequestMapping(value = "comingStatistics.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingStatistics(HttpServletRequest request, ModelAndView mav) {
		log.info("comingStatistics.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String searchType = StringUtil.reqNullCheck(request, "searchType");
		String startDate = StringUtil.reqNullCheck(request, "startDate");
		String endDate = StringUtil.reqNullCheck(request, "endDate");

		if (!searchType.equals("") && !startDate.equals("") && !endDate.equals("")) {
			PageParam pageParam = new PageParam();
			pageParam.setSearchType(searchType);
			pageParam.setStartDate(startDate);
			pageParam.setEndDate(endDate);

			if (searchType.equals("01")) {
				List<TicketInfo> list1 = comingService.getStatisticsByDept(pageParam);
				mav.addObject("list1", list1);
			} else if (searchType.equals("02")) {
				List<TicketInfo> list2 = comingService.getStatisticsByTotal(pageParam);
				mav.addObject("list2", list2);
			} else if (searchType.equals("03")) {
				List<ComingInfo> list3 = comingService.getStatisticsByVisitor(pageParam);
				mav.addObject("list3", list3);
			} else if (searchType.equals("04")) {
				List<TicketInfo> list4 = comingService.getStatisticsByWebSale(pageParam);
				mav.addObject("list4", list4);
			}

			mav.addObject("pageParam", pageParam);
		}

		mav.setViewName("coming/comingStatistics");
		return mav;
	}

	// 주차권 등록 GATEWAY
	@RequestMapping(value = "comingGate.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingGate(HttpServletRequest request, ModelAndView mav, RedirectAttributes redirectAttributes) {
		log.info("comingGate.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String seq = StringUtil.reqNullCheck(request, "visitSeq");

		ComingInfo param = new ComingInfo();
		param.setVisitSeq(seq);
		ComingInfo info = comingService.getComing(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visitSeq", info.getVisitSeq());
		map.put("visitCompany", info.getVisitCompany());
		map.put("visitName", info.getVisitName());
		map.put("meetDeptCode", info.getMeetDeptCode());
		map.put("meetDeptName", info.getMeetDeptName());
		map.put("visitStartDate", info.getVisitStartDate());
		redirectAttributes.addFlashAttribute("map", map);

		return (ModelAndView) new ModelAndView("redirect:/ticket/ticketAdd.do");
	}

	// 엑셀 다운로드
	@RequestMapping(value = "comingExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView comingExcelDownload(HttpServletRequest request) {
		log.info("comingExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("excelView");

		// 로그인 정보
		String deptCode = SpringSecurityUtil.getDeptCode();

		// 총무팀 & 정보관리팀만 확인 가능
		if (!deptCode.equals("1040") && !deptCode.equals("1031")) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		String auth = StringUtil.reqNullCheck(request, "auth");
		String searchType = StringUtil.reqNullCheck(request, "searchType");
		String startDate = StringUtil.reqNullCheck(request, "startDate");
		String endDate = StringUtil.reqNullCheck(request, "endDate");

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		if (searchType.equals("") || startDate.equals("") || endDate.equals("")) {
			log.error("searchType : " + searchType + " , startDate : " + endDate + ", " + endDate);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		PageParam pageParam = new PageParam();
		pageParam.setSearchType(searchType);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);

		// 파일이름, 시트이름
		String fileName = "";
		String sheetName = startDate + " ~ " + endDate;

		// 컬럼 데이터, 내용 데이터
		List<String> listColumn = new ArrayList<String>();
		if (searchType.equals("01")) {
			fileName = "부서별_주차권_지급_현황";
			listColumn.add("부서코드");
			listColumn.add("부서이름");
			listColumn.add("2시간");
			listColumn.add("3시간");
			listColumn.add("4시간");
			listColumn.add("6시간");
			listColumn.add("종일권");
			listColumn.add("계");
			List<TicketInfo> listData = comingService.getStatisticsByDept(pageParam);
			mav.addObject("listColumn", listColumn);
			mav.addObject("listData", listData);
		} else if (searchType.equals("02")) {
			fileName = "주차권_지급_상세_목록";
			listColumn.add("번호");
			listColumn.add("방문 업체명");
			listColumn.add("방문자 성함");
			listColumn.add("방문 목적");
			listColumn.add("지급일시");
			listColumn.add("담당부서");
			listColumn.add("2시간");
			listColumn.add("3시간");
			listColumn.add("4시간");
			listColumn.add("6시간");
			listColumn.add("종일권");
			List<TicketInfo> listData = comingService.getStatisticsByTotal(pageParam);
			mav.addObject("listColumn", listColumn);
			mav.addObject("listData", listData);
		} else if (searchType.equals("03")) {
			fileName = "출입자_상세_목록";
			listColumn.add("번호");
			listColumn.add("방문 업체명");
			listColumn.add("방문자 성함");
			listColumn.add("방문 일시");
			listColumn.add("접견부서");
			listColumn.add("접견인");
			listColumn.add("주차권 지급 여부");
			List<ComingInfo> listData = comingService.getStatisticsByVisitor(pageParam);
			mav.addObject("listColumn", listColumn);
			mav.addObject("listData", listData);
		} else if (searchType.equals("04")) {
			fileName = "KT&G_웹할인_상세_목록";
			listColumn.add("번호");
			listColumn.add("방문 업체명");
			listColumn.add("방문자 성함");
			listColumn.add("방문 목적");
			listColumn.add("지급일시");
			listColumn.add("담당부서");
			listColumn.add("정산금액");
			List<TicketInfo> listData = comingService.getStatisticsByWebSale(pageParam);
			mav.addObject("listColumn", listColumn);
			mav.addObject("listData", listData);
		}

		mav.addObject("excelType", searchType);
		mav.addObject("fileName", fileName);
		mav.addObject("sheetName", sheetName);
		return mav;
	}

}
