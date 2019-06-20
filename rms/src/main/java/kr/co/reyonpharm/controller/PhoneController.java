package kr.co.reyonpharm.controller;

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
import kr.co.reyonpharm.models.PhoneInfo;
import kr.co.reyonpharm.service.PhoneService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/phone/")
public class PhoneController extends CustomExceptionHandler {

	@Autowired
	PhoneService phoneService;

	/* 내선번호 관리 */
	
	// 내선번호 목록 리스트
	@RequestMapping(value = "phonenumberList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView phonenumberList(HttpServletRequest request, ModelAndView mav) {
		log.info("phonenumberList.do");
		
		PhoneInfo param = new PhoneInfo();
		param.setPhoneType("10");
		List<PhoneInfo> listBonsa = phoneService.getPhoneInfoBookList(param);
		param.setPhoneType("20");
		List<PhoneInfo> listJincheon = phoneService.getPhoneInfoBookList(param);
		param.setPhoneType("30");
		List<PhoneInfo> listChungju = phoneService.getPhoneInfoBookList(param);
		PhoneInfo info = phoneService.getPhoneInfoLastUpdateTime();
		
		mav.addObject("listBonsa", listBonsa);
		mav.addObject("listJincheon", listJincheon);
		mav.addObject("listChungju", listChungju);
		mav.addObject("info", info);
		mav.setViewName("phone/phonenumberList");
		return mav;
	}
	
	// 내선번호 편집
	@RequestMapping(value = "phonenumberBook.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView phonenumberBook(HttpServletRequest request, ModelAndView mav) {
		log.info("phonenumberBook.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("phone/phonenumberBook");
		return mav;
	}
	
	// 내선번호 순서편집
	@RequestMapping(value = "phonenumberOrder.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView phonenumberOrder(HttpServletRequest request, ModelAndView mav) {
		log.info("phonenumberOrder.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		PhoneInfo param = new PhoneInfo();
		param.setPhoneType("10");
		List<PhoneInfo> listBonsa = phoneService.getPhoneInfoBookList(param);
		param.setPhoneType("20");
		List<PhoneInfo> listJincheon = phoneService.getPhoneInfoBookList(param);
		param.setPhoneType("30");
		List<PhoneInfo> listChungju = phoneService.getPhoneInfoBookList(param);
		
		mav.addObject("listBonsa", listBonsa);
		mav.addObject("listJincheon", listJincheon);
		mav.addObject("listChungju", listChungju);
		mav.setViewName("phone/phonenumberOrder");
		return mav;
	}
	
	// 내선번호 조회 액션
	@RequestMapping(value = "phonenumberGetAjax.json")
	public ModelAndView phonenumberGetAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("phonenumberGetAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String searchText = StringUtil.reqNullCheckHangulUTF8(request, "searchText");
			
			if (auth.equals("reyon")) {
				PhoneInfo param = new PhoneInfo();
				param.setSearchText(searchText);
				List<PhoneInfo> list = phoneService.getPhoneInfoList(param);
				
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
	
	// 내선번호 등록 액션
	@RequestMapping(value = "phonenumberAddAjax.json")
	public ModelAndView phonenumberAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("phonenumberAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String regSabun = SpringSecurityUtil.getUsername();
				
				String sabun = StringUtil.reqNullCheckHangulUTF8(request, "sabun");
				String viewDept = StringUtil.reqNullCheckHangulUTF8(request, "viewDept");
				String viewPosi = StringUtil.reqNullCheckHangulUTF8(request, "viewPosi");
				String viewName = StringUtil.reqNullCheckHangulUTF8(request, "viewName");
				String phoneType1 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType1");
				String phonenum1 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum1");
				String faxnum1 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum1");
				String phoneType2 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType2");
				String phonenum2 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum2");
				String faxnum2 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum2");
				String phoneType3 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType3");
				String phonenum3 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum3");
				String faxnum3 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum3");
				
				PhoneInfo info = new PhoneInfo();
				info.setSabun(sabun);
				info.setViewDept(viewDept);
				info.setViewPosi(viewPosi);
				info.setViewName(viewName);
				info.setPhoneType1(phoneType1);
				info.setPhoneType2(phoneType2);
				info.setPhoneType3(phoneType3);
				info.setPhonenum1(phonenum1);
				info.setPhonenum2(phonenum2);
				info.setPhonenum3(phonenum3);
				info.setFaxnum1(faxnum1);
				info.setFaxnum2(faxnum2);
				info.setFaxnum3(faxnum3);
				info.setRegSabun(regSabun);
				
				int resultCnt = phoneService.addPhoneInfo(info);

				if (resultCnt == 0) {
					mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
				} else {
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

	// 내선번호 수정 액션
	@RequestMapping(value = "phonenumberModifyAjax.json")
	public ModelAndView phonenumberModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("phonenumberModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String regSabun = SpringSecurityUtil.getUsername();
				
				String phoneSeq = StringUtil.reqNullCheckHangulUTF8(request, "phoneSeq");
				String sabun = StringUtil.reqNullCheckHangulUTF8(request, "sabun");
				String viewDept = StringUtil.reqNullCheckHangulUTF8(request, "viewDept");
				String viewPosi = StringUtil.reqNullCheckHangulUTF8(request, "viewPosi");
				String viewName = StringUtil.reqNullCheckHangulUTF8(request, "viewName");
				String phoneType1 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType1");
				String phonenum1 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum1");
				String faxnum1 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum1");
				String phoneType2 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType2");
				String phonenum2 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum2");
				String faxnum2 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum2");
				String phoneType3 = StringUtil.reqNullCheckHangulUTF8(request, "phoneType3");
				String phonenum3 = StringUtil.reqNullCheckHangulUTF8(request, "phonenum3");
				String faxnum3 = StringUtil.reqNullCheckHangulUTF8(request, "faxnum3");
				
				PhoneInfo info = new PhoneInfo();
				info.setPhoneSeq(phoneSeq);
				info.setSabun(sabun);
				info.setViewDept(viewDept);
				info.setViewPosi(viewPosi);
				info.setViewName(viewName);
				info.setPhoneType1(phoneType1);
				info.setPhoneType2(phoneType2);
				info.setPhoneType3(phoneType3);
				info.setPhonenum1(phonenum1);
				info.setPhonenum2(phonenum2);
				info.setPhonenum3(phonenum3);
				info.setFaxnum1(faxnum1);
				info.setFaxnum2(faxnum2);
				info.setFaxnum3(faxnum3);
				info.setRegSabun(regSabun);
				
				int resultCnt = phoneService.modifyPhoneInfo(info);

				if (resultCnt == 0) {
					mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
				} else {
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
	
	// 내선번호 삭제 액션
	@RequestMapping(value = "phonenumberDeleteAjax.json")
	public ModelAndView phonenumberDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("phonenumberDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String phoneSeq = StringUtil.reqNullCheckHangulUTF8(request, "phoneSeq");
				
				PhoneInfo info = new PhoneInfo();
				info.setPhoneSeq(phoneSeq);
				
				int resultCnt = phoneService.deletePhoneInfo(info);

				if (resultCnt == 0) {
					mav.addObject("resultCode", CustomExceptionCodes.NOT_AFFECTED.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.NOT_AFFECTED.getMsg());
				} else {
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
	
	// 내선번호 순서 수정 액션
	@RequestMapping(value = "phonenumberOrderModifyAjax.json")
	public ModelAndView phonenumberOrderModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("phonenumberOrderModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String regSabun = SpringSecurityUtil.getUsername();
				String arrBonsa = StringUtil.reqNullCheckHangulUTF8(request, "arrBonsa");
				String arrJincheon = StringUtil.reqNullCheckHangulUTF8(request, "arrJincheon");
				String arrChungju = StringUtil.reqNullCheckHangulUTF8(request, "arrChungju");
				
				String[] arrayBonsa = arrBonsa.split(",");
				String[] arrayJincheon = arrJincheon.split(",");
				String[] arrayChungju = arrChungju.split(",");
				
				for (int i = 0; i < arrayBonsa.length; i++) {
					PhoneInfo info = new PhoneInfo();
					info.setPhoneSeq(arrayBonsa[i]);
					info.setOrderSeq1(String.valueOf(i+1));
					info.setRegSabun(regSabun);
					phoneService.modifyPhoneOrderInfo(info);
				}

				for (int i = 0; i < arrayJincheon.length; i++) {
					PhoneInfo info = new PhoneInfo();
					info.setPhoneSeq(arrayJincheon[i]);
					info.setOrderSeq2(String.valueOf(i+1));
					info.setRegSabun(regSabun);
					phoneService.modifyPhoneOrderInfo(info);
				}

				for (int i = 0; i < arrayChungju.length; i++) {
					PhoneInfo info = new PhoneInfo();
					info.setPhoneSeq(arrayChungju[i]);
					info.setOrderSeq3(String.valueOf(i+1));
					info.setRegSabun(regSabun);
					phoneService.modifyPhoneOrderInfo(info);
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
	
	// 내선번호 엑셀 다운로드
	@RequestMapping(value = "phoneExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView phoneExcelDownload(HttpServletRequest request) {
		log.info("phoneExcelDownload.do");

		ModelAndView mav = new ModelAndView("phoneExcelView");
		String auth = StringUtil.reqNullCheck(request, "auth");
		String saup = StringUtil.reqNullCheck(request, "saup");

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		PhoneInfo param = new PhoneInfo();
		param.setPhoneType(saup);
		List<PhoneInfo> list = phoneService.getPhoneInfoBookList(param);
		PhoneInfo info = phoneService.getPhoneInfoLastUpdateTime();

		int halfCnt = list.size()/2;
		List<PhoneInfo> list1 = new ArrayList<PhoneInfo>();
		List<PhoneInfo> list2 = new ArrayList<PhoneInfo>();
		for (int i = 0; i < halfCnt; i++) {
			list1.add(list.get(i));
		}
		for (int i = halfCnt; i < list.size(); i++) {
			list2.add(list.get(i));
		}
		
		int totalCnt = 0;
		if(list1.size() > list2.size()) {
			totalCnt = list1.size()+3;
		} else {
			totalCnt = list2.size()+3;
		}
		
		String fileName = "";
		String subInfo1 = "";
		String subInfo2 = "";
		String templateFileName = "/phone"+saup+".xlsx";
		
		if (saup.equals("10")) {
			fileName = "본사내선번호";
			subInfo1 = "서울특별시 강남구 영동대로 416 (대치동 코스모타워 8층)";
			subInfo2 = "개인별 직통번호 : 02-3407-5(내선번호)";
		} else if (saup.equals("20")) {
			fileName = "진천내선번호";
			subInfo1 = "충청북도 진천군 덕산면 한삼로 69-10 (덕산농공단지)";
			subInfo2 = "개인별 직통번호 : 043-531-3(내선번호)";
		} else if (saup.equals("30")) {
			fileName = "충주내선번호";
			subInfo1 = "충청북도 충주시 중앙탑면 원앙길 8-13 스타프라자 4층";
			subInfo2 = "개인별 직통번호 : 043-840-6(내선번호)";
		}

		mav.addObject("fileName", fileName);
		mav.addObject("subInfo1", subInfo1);
		mav.addObject("subInfo2", subInfo2);
		mav.addObject("templateFileName", templateFileName);
		mav.addObject("totalCnt", totalCnt);
		mav.addObject("list1", list1);
		mav.addObject("list2", list2);
		mav.addObject("info", info);
		return mav;
	}
	
}
