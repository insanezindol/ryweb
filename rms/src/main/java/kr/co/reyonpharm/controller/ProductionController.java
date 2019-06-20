package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.ProductionInfo;
import kr.co.reyonpharm.service.ProductionService;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/production/")
public class ProductionController extends CustomExceptionHandler {
	
	@Autowired
	ProductionService productionService;
	
	// 진천공장 알람 페이지
	@RequestMapping(value = "notice.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sampleList(HttpServletRequest request, ModelAndView mav) {
		log.info("notice.do");
		mav.setViewName("production/sampleList");
		return mav;
	}
	
	/*
	 * Develop
	 */
	@RequestMapping(value = "dev.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sampleList1(HttpServletRequest request, ModelAndView mav) {
		log.info("dev.do");
		
		//권한 등급
		/*
		 * String deptCode = SpringSecurityUtil.getDeptCode(); log.info(deptCode);
		 */
		
		
		ProductionInfo param = new ProductionInfo();
		/* 
		 * if ( request.getParameter("del") != null ) {
		 * param.setNum(request.getParameter("del")); log.info(param.getNum());
		 * productionService.delContent(param); }
		 */
		
		//ProductionInfo param = new ProductionInfo();
		//List<ProductionInfo> list = productionService.getSampleList(param);
		
		//mav.addObject("list", list);
		
		List<ProductionInfo> list = productionService.getContent(param);
		int delayCount = productionService.getDelayCount(param); //성적승인 지연 DB로부터 get
		int stockCount = productionService.getStockCount(param);
		List<ProductionInfo> stockList = productionService.getStock(param);
		
		//mav.addObject("deptCode", deptCode);
		mav.addObject("list", list);
		mav.addObject("delayCount", delayCount);
		mav.addObject("stockCount", stockCount);
		mav.addObject("stockList", stockList);
		mav.setViewName("production/sampleList0");
		return mav;
	}

	// 진천공장 알람 페이지 - 공지사항
	@RequestMapping(value = "noticeGetAjax.json")
	public ModelAndView noticeGetAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("noticeGetAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				ProductionInfo param = new ProductionInfo();
				List<ProductionInfo> list = productionService.getContent(param);
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
	
	// 진천공장 알람 페이지 - 알람
	@RequestMapping(value = "alarmGetAjax.json")
	public ModelAndView alarmGetAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("alarmGetAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				ProductionInfo param = new ProductionInfo();
				int delayCount = productionService.getDelayCount(param); //성적승인 지연 DB로부터 get
				int stockCount = productionService.getStockCount(param);
				List<ProductionInfo> stockList = productionService.getStock(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("delayCount", delayCount);
				mav.addObject("stockCount", stockCount);
				mav.addObject("list", stockList);
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
	
	@RequestMapping(value = "insertNotice.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView insertNotice(HttpServletRequest request, ModelAndView mav) {
		log.info("insertNotice.do");
		
		ProductionInfo param = new ProductionInfo();
		param.setSabun(request.getParameter("sabun"));
		param.setAuth(request.getParameter("auth"));
		param.setContent(request.getParameter("content"));
		param.setEmrg(request.getParameter("emrg"));
		
		productionService.addContent(param);
		
		log.info("----------------------------1");
		
		mav.setViewName("production/sampleList");
		return mav;
	}
	
	/*
	 * @RequestMapping(value = "ajax.do", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public ModelAndView ajax(HttpServletRequest request,
	 * ModelAndView mav) { log.info("ajax.do");
	 * 
	 * ProductionInfo param = new ProductionInfo();
	 * param.setSabun(request.getParameter("sabun"));
	 * 
	 * List<ProductionInfo> list = productionService.getName(param);
	 * 
	 * mav.addObject("list", list); mav.setViewName("production/ajax"); return mav;
	 * }
	 */
	
	@RequestMapping(value = "getName.json")
	public ModelAndView getName(HttpServletRequest request, ModelAndView mav) {
		log.info("getName.json");
		
		ProductionInfo param = new ProductionInfo();
		param.setSabun(request.getParameter("sabun"));
		
		List<ProductionInfo> list = productionService.getName(param);
		
		mav.addObject("auth", list.get(0).getAuth());		
		return mav;
	}
	
	
	/* 진천공장 알림 관리 메뉴 */

	// 알림 관리 목록
	@RequestMapping(value = "productionList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView productionList(HttpServletRequest request, ModelAndView mav) {
		log.info("productionList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		
		String s_auth = StringUtil.reqNullCheckHangulUTF8(request, "s_auth");
		String s_content = StringUtil.reqNullCheckHangulUTF8(request, "s_content");
		String s_emrg = StringUtil.reqNullCheckHangulUTF8(request, "s_emrg");
		String s_wDate = StringUtil.reqNullCheckHangulUTF8(request, "s_wDate");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_auth(s_auth);
		pageParam.setS_content(s_content);
		pageParam.setS_emrg(s_emrg);
		pageParam.setS_wDate(s_wDate);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = productionService.getProductionListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ProductionInfo> list = productionService.getProductionList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("production/productionList");
		return mav;
	}

	// 알림 관리 상세
	@RequestMapping(value = "productionView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView productionView(HttpServletRequest request, ModelAndView mav) {
		log.info("productionView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String num = StringUtil.reqNullCheck(request, "num");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(num)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ProductionInfo param = new ProductionInfo();
		param.setNum(num);
		ProductionInfo info = productionService.getProduction(param); //어차피 내용하나

		if (info == null) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getSabun())) {
			isMine = true;
		}

		mav.addObject("num", num);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isMine", isMine);
		mav.setViewName("production/productionView");
		return mav;
	}

	// 알림 관리 등록
	@RequestMapping(value = "productionAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView productionAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("productionAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("production/productionAdd");
		return mav;
	}

	// 알림 관리 수정
	@RequestMapping(value = "productionModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView productionModify(HttpServletRequest request, ModelAndView mav) {
		log.info("productionModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String num = StringUtil.reqNullCheck(request, "num");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(num)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 로그인 정보
		String sabun = SpringSecurityUtil.getUsername();

		ProductionInfo param = new ProductionInfo();
		param.setNum(num);
		ProductionInfo info = productionService.getProduction(param);

		if (info == null) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		// 내가 등록한 회의 인지 검사
		boolean isMine = false;
		if (sabun.equals(info.getSabun())) {
			isMine = true;
		}

		if (!isMine) {
			throw new CustomException(CustomExceptionCodes.NOT_AUTHORIZED);
		}

		mav.addObject("num", num);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("production/productionModify");
		return mav;
	}

	// 알림 관리 등록 액션
	@RequestMapping(value = "productionAddAjax.json")
	public ModelAndView productionAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("productionAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String content = StringUtil.reqNullCheckHangulUTF8(request, "content");
				String emrg = StringUtil.reqNullCheckHangulUTF8(request, "emrg");
				String sdate = StringUtil.reqNullCheck(request, "sdate");
				String edate = StringUtil.reqNullCheck(request, "edate");

				if ("".equals(content) || "".equals(emrg)) {
					log.error("content : " + content + ", emrg : " + emrg);
					log.error("sdate : " + sdate + ", edate : " + edate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					ProductionInfo info = new ProductionInfo();
					info.setSabun(regSabun);
					info.setAuth(regName);
					info.setContent(content);
					info.setEmrg(emrg);
					info.setSdate(sdate);
					info.setEdate(edate);

					int resultCnt = productionService.addProduction(info);
					log.debug("resultCnt : " + resultCnt);
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

	// 알림 관리 수정 액션
	@RequestMapping(value = "productionModifyAjax.json")
	public ModelAndView productionModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("productionModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String num = StringUtil.reqNullCheckHangulUTF8(request, "num");
				String content = StringUtil.reqNullCheckHangulUTF8(request, "content");
				String emrg = StringUtil.reqNullCheckHangulUTF8(request, "emrg");
				String sdate = StringUtil.reqNullCheck(request, "sdate");
				String edate = StringUtil.reqNullCheck(request, "edate");

				if ("".equals(num) || "".equals(content) || "".equals(emrg)) {
					log.error("num : " + num + ", content : " + content + ", emrg : " + emrg);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					String regSabun = SpringSecurityUtil.getUsername();
					String regName = SpringSecurityUtil.getKname();

					ProductionInfo info = new ProductionInfo();
					info.setNum(num);
					info.setSabun(regSabun);
					info.setAuth(regName);
					info.setContent(content);
					info.setEmrg(emrg);
					info.setSdate(sdate);
					info.setEdate(edate);

					int resultCnt = productionService.modifyProduction(info);
					log.debug("resultCnt : " + resultCnt);
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

	// 알림 관리 삭제 액션
	@RequestMapping(value = "productionDeleteAjax.json")
	public ModelAndView productionDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("productionDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String num = StringUtil.reqNullCheck(request, "num");

				if ("".equals(num)) {
					log.error("num : " + num);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					ProductionInfo info = new ProductionInfo();
					info.setNum(num);
					int resultCnt = productionService.deleteProduction(info);
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

	//두번째 페이지 검색관련 ajax
	@RequestMapping(value = "countAjax.json")
	public ModelAndView countAjax(HttpServletRequest request, HttpServletResponse respone, ModelAndView mov) {
		log.info("countAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		
		return mov;
	}
}
