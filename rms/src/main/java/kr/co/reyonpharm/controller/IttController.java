package kr.co.reyonpharm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import kr.co.reyonpharm.models.BtsInfo;
import kr.co.reyonpharm.models.CommonCodeInfo;
import kr.co.reyonpharm.models.GroupwareExtInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.service.IttService;
import kr.co.reyonpharm.util.ReyonSha256;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/itt/")
public class IttController extends CustomExceptionHandler {

	@Autowired
	IttService ittService;

	/* 정보관리팀 전용 컨트롤러 */

	// 제품 일련번호 수동 이관
	@RequestMapping(value = "manualMigrationProduct.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView manualMigrationProduct(HttpServletRequest request, ModelAndView mav) {
		log.info("manualMigrationProduct.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("itt/manualMigrationProduct");
		return mav;
	}

	// 제품 일련번호 조회 액션
	@RequestMapping(value = "getMigrationProductAjax.json")
	public ModelAndView getMigrationProductAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getMigrationProductAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String outdate = StringUtil.reqNullCheck(request, "outdate"); // yyyyMM
			if (auth.equals("reyon")) {
				if ("".equals(outdate)) {
					log.error("outdate : " + outdate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					BtsInfo param = new BtsInfo();
					param.setOutdate(outdate);
					List<BtsInfo> btsList = ittService.getBtsInfoListCnt(param);
					List<BtsInfo> migList = ittService.getMigrationInfoCnt(param);
					mav.addObject("btsList", btsList);
					mav.addObject("migList", migList);
					mav.addObject("resultCode", 0);
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

	// 제품 일련번호 마이그레이션 액션
	@RequestMapping(value = "manualMigrationProductAjax.json")
	public ModelAndView manualMigrationProductAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("manualMigrationProductAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String outdate = StringUtil.reqNullCheck(request, "outdate"); // yyyy-MM-dd
			if (auth.equals("reyon")) {
				if ("".equals(outdate)) {
					log.error("outdate : " + outdate);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					log.info("[BEG] BTS MIGRATION");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String startDate = sdf.format(new Date());
					Map<String, Integer> output = ittService.migrationProduct(outdate);
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("deleteSize", output.get("deleteSize"));
					mav.addObject("originSize", output.get("originSize"));
					mav.addObject("copySize", output.get("copySize"));
					String endDate = sdf.format(new Date());
					mav.addObject("startDate", startDate);
					mav.addObject("endDate", endDate);
					log.info("[END] BTS MIGRATION");
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

	// 공통코드 목록
	@RequestMapping(value = "commonCodeList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView commonCodeList(HttpServletRequest request, ModelAndView mav) {
		log.info("commonCodeList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_grpCode = StringUtil.reqNullCheckHangulUTF8(request, "s_grpCode");
		String s_grpName = StringUtil.reqNullCheckHangulUTF8(request, "s_grpName");
		String s_codeName = StringUtil.reqNullCheckHangulUTF8(request, "s_codeName");
		String s_useGbn = StringUtil.reqNullCheckHangulUTF8(request, "s_useGbn");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_grpCode(s_grpCode);
		pageParam.setS_grpName(s_grpName);
		pageParam.setS_codeName(s_codeName);
		pageParam.setS_useGbn(s_useGbn);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = ittService.getCommonCodeListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<CommonCodeInfo> list = ittService.getCommonCodeList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("itt/commonCodeList");
		return mav;
	}

	// 공통코드 상세
	@RequestMapping(value = "commonCodeView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView commonCodeView(HttpServletRequest request, ModelAndView mav) {
		log.info("commonCodeView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String grpCode = StringUtil.reqNullCheck(request, "grpCode");
		String code = StringUtil.reqNullCheck(request, "code");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(grpCode) || "".equals(code)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		CommonCodeInfo param = new CommonCodeInfo();
		param.setGrpCode(grpCode);
		param.setCode(code);
		CommonCodeInfo info = ittService.getCommonCodeInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("itt/commonCodeView");
		return mav;
	}

	// 공통코드 등록
	@RequestMapping(value = "commonCodeAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView commonCodeAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("commonCodeAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("itt/commonCodeAdd");
		return mav;
	}

	// 공통코드 등록 액션
	@RequestMapping(value = "commonCodeAddAjax.json")
	public ModelAndView commonCodeAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("commonCodeAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String grpCode = StringUtil.reqNullCheck(request, "grpCode");
			String code = StringUtil.reqNullCheck(request, "code");
			String grpName = StringUtil.reqNullCheckHangulUTF8(request, "grpName");
			String codeName = StringUtil.reqNullCheckHangulUTF8(request, "codeName");
			String codeDesc = StringUtil.reqNullCheckHangulUTF8(request, "codeDesc");
			String useGbn = StringUtil.reqNullCheck(request, "useGbn");
			String item1 = StringUtil.reqNullCheckHangulUTF8(request, "item1");
			String item2 = StringUtil.reqNullCheckHangulUTF8(request, "item2");
			String item3 = StringUtil.reqNullCheckHangulUTF8(request, "item3");
			String item4 = StringUtil.reqNullCheckHangulUTF8(request, "item4");
			String sortGbn = StringUtil.reqNullCheck(request, "sortGbn");
			String hiddenGbn = StringUtil.reqNullCheck(request, "hiddenGbn");
			if (auth.equals("reyon")) {
				if ("".equals(grpCode) || "".equals(code) || "".equals(grpName)) {
					log.error("grpCode : " + grpCode + ", code : " + code + ", grpName : " + grpName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					CommonCodeInfo param = new CommonCodeInfo();
					param.setGrpCode(grpCode);
					param.setCode(code);
					param.setGrpName(grpName);
					param.setCodeName(codeName);
					param.setCodeDesc(codeDesc);
					param.setUseGbn(useGbn);
					param.setItem1(item1);
					param.setItem2(item2);
					param.setItem3(item3);
					param.setItem4(item4);
					param.setSortGbn(sortGbn);
					param.setHiddenGbn(hiddenGbn);
					int resultCnt = ittService.addCommonCodeInfo(param);
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

	// 공통코드 수정
	@RequestMapping(value = "commonCodeModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView commonCodeModify(HttpServletRequest request, ModelAndView mav) {
		log.info("commonCodeModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String grpCode = StringUtil.reqNullCheck(request, "grpCode");
		String code = StringUtil.reqNullCheck(request, "code");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(grpCode) || "".equals(code)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		CommonCodeInfo param = new CommonCodeInfo();
		param.setGrpCode(grpCode);
		param.setCode(code);
		CommonCodeInfo info = ittService.getCommonCodeInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("itt/commonCodeModify");
		return mav;
	}

	// 공통코드 수정 액션
	@RequestMapping(value = "commonCodeModifyAjax.json")
	public ModelAndView commonCodeModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("commonCodeModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String grpCode = StringUtil.reqNullCheck(request, "grpCode");
			String code = StringUtil.reqNullCheck(request, "code");
			String grpName = StringUtil.reqNullCheckHangulUTF8(request, "grpName");
			String codeName = StringUtil.reqNullCheckHangulUTF8(request, "codeName");
			String codeDesc = StringUtil.reqNullCheckHangulUTF8(request, "codeDesc");
			String useGbn = StringUtil.reqNullCheck(request, "useGbn");
			String item1 = StringUtil.reqNullCheckHangulUTF8(request, "item1");
			String item2 = StringUtil.reqNullCheckHangulUTF8(request, "item2");
			String item3 = StringUtil.reqNullCheckHangulUTF8(request, "item3");
			String item4 = StringUtil.reqNullCheckHangulUTF8(request, "item4");
			String sortGbn = StringUtil.reqNullCheck(request, "sortGbn");
			String hiddenGbn = StringUtil.reqNullCheck(request, "hiddenGbn");
			if (auth.equals("reyon")) {
				if ("".equals(grpCode) || "".equals(code) || "".equals(grpName)) {
					log.error("grpCode : " + grpCode + ", code : " + code + ", grpName : " + grpName);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					CommonCodeInfo param = new CommonCodeInfo();
					param.setGrpCode(grpCode);
					param.setCode(code);
					param.setGrpName(grpName);
					param.setCodeName(codeName);
					param.setCodeDesc(codeDesc);
					param.setUseGbn(useGbn);
					param.setItem1(item1);
					param.setItem2(item2);
					param.setItem3(item3);
					param.setItem4(item4);
					param.setSortGbn(sortGbn);
					param.setHiddenGbn(hiddenGbn);
					int resultCnt = ittService.modifyCommonCodeInfo(param);
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

	// 공통코드 삭제 액션
	@RequestMapping(value = "commonCodeDeleteAjax.json")
	public ModelAndView commonCodeDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("commonCodeDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String grpCode = StringUtil.reqNullCheck(request, "grpCode");
			String code = StringUtil.reqNullCheck(request, "code");
			if (auth.equals("reyon")) {
				if ("".equals(grpCode) || "".equals(code)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					CommonCodeInfo param = new CommonCodeInfo();
					param.setGrpCode(grpCode);
					param.setCode(code);
					int resultCnt = ittService.deleteCommonCodeInfo(param);
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

	// 암호화 관리
	@RequestMapping(value = "encryptView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView encryptView(HttpServletRequest request, ModelAndView mav) {
		log.info("encryptView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("itt/encryptView");
		return mav;
	}

	// 암호화 액션
	@RequestMapping(value = "getEncryptAjax.json")
	public ModelAndView getEncryptAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getEncryptAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String plainText = StringUtil.reqNullCheck(request, "plainText");
			if (auth.equals("reyon")) {
				if ("".equals(plainText)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					plainText = URLDecoder.decode(plainText, "UTF-8");
					String cipherText = ReyonSha256.getCiperText(plainText);
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", cipherText);
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
	
	// 그룹웨어 외부접속 목록
	@RequestMapping(value = "groupwareExtList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView groupwareExtList(HttpServletRequest request, ModelAndView mav) {
		log.info("groupwareExtList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_reqComment = StringUtil.reqNullCheckHangulUTF8(request, "s_reqComment");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_user(s_user);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_reqComment(s_reqComment);
		pageParam.setS_status(s_status);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = ittService.getGroupwareExtListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<GroupwareExtInfo> list = ittService.getGroupwareExtList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("itt/groupwareExtList");
		return mav;
	}
	
	// 그룹웨어 외부접속 추가
	@RequestMapping(value = "groupwareExtAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView groupwareExtAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("groupwareExtAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("itt/groupwareExtAdd");
		return mav;
	}
	
	// 그룹웨어 외부접속 추가
	@RequestMapping(value = "groupwareExtAddAjax.json")
	public ModelAndView groupwareExtAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("groupwareExtAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String sabun = StringUtil.reqNullCheckHangulUTF8(request, "sabun");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String accessIngType = StringUtil.reqNullCheckHangulUTF8(request, "accessIngType");
			String accessEndType = StringUtil.reqNullCheckHangulUTF8(request, "accessEndType");
			String reqComment = StringUtil.reqNullCheckHangulUTF8(request, "reqComment");
			
			if (auth.equals("reyon")) {
				if ("".equals(sabun) || "".equals(startDate) || "".equals(endDate) || "".equals(accessIngType) || "".equals(accessEndType) || "".equals(reqComment)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					GroupwareExtInfo param = new GroupwareExtInfo();
					param.setSabun(sabun);
					param.setStartDate(startDate);
					param.setEndDate(endDate);
					param.setAccessIngType(accessIngType);
					param.setAccessEndType(accessEndType);
					param.setReqComment(reqComment);
					param.setStatus("1");
					int resultCnt = ittService.addGroupwareExt(param);
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
	
	// 그룹웨어 외부접속 수정
	@RequestMapping(value = "groupwareExtModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView groupwareExtModify(HttpServletRequest request, ModelAndView mav) {
		log.info("groupwareExtModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(reqSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		GroupwareExtInfo param = new GroupwareExtInfo();
		param.setReqSeq(reqSeq);
		GroupwareExtInfo info = ittService.getGroupwareExt(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("reqSeq", reqSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("itt/groupwareExtModify");
		return mav;
	}
	
	// 그룹웨어 외부접속 수정
	@RequestMapping(value = "groupwareExtModifyAjax.json")
	public ModelAndView groupwareExtModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("groupwareExtModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String reqSeq = StringUtil.reqNullCheckHangulUTF8(request, "reqSeq");
			String sabun = StringUtil.reqNullCheckHangulUTF8(request, "sabun");
			String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
			String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
			String accessIngType = StringUtil.reqNullCheckHangulUTF8(request, "accessIngType");
			String accessEndType = StringUtil.reqNullCheckHangulUTF8(request, "accessEndType");
			String reqComment = StringUtil.reqNullCheckHangulUTF8(request, "reqComment");
			
			if (auth.equals("reyon")) {
				if ("".equals(sabun) || "".equals(startDate) || "".equals(endDate) || "".equals(accessIngType) || "".equals(accessEndType) || "".equals(reqComment)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					GroupwareExtInfo param = new GroupwareExtInfo();
					param.setReqSeq(reqSeq);
					param.setSabun(sabun);
					param.setStartDate(startDate);
					param.setEndDate(endDate);
					param.setAccessIngType(accessIngType);
					param.setAccessEndType(accessEndType);
					param.setReqComment(reqComment);
					param.setStatus("1");
					int resultCnt = ittService.modifyGroupwareExt(param);
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
	
	// 그룹웨어 외부접속 조회
	@RequestMapping(value = "groupwareExtView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView groupwareExtView(HttpServletRequest request, ModelAndView mav) {
		log.info("groupwareExtView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		String reqSeq = StringUtil.reqNullCheck(request, "reqSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(reqSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		GroupwareExtInfo param = new GroupwareExtInfo();
		param.setReqSeq(reqSeq);
		GroupwareExtInfo info = ittService.getGroupwareExt(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("reqSeq", reqSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("itt/groupwareExtView");
		return mav;
	}
	
	// 그룹웨어 외부접속 삭제
	@RequestMapping(value = "groupwareExtDeleteAjax.json")
	public ModelAndView groupwareExtDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("groupwareExtDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String reqSeq = StringUtil.reqNullCheckHangulUTF8(request, "reqSeq");
			if (auth.equals("reyon")) {
				if ("".equals(reqSeq)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					GroupwareExtInfo param = new GroupwareExtInfo();
					param.setReqSeq(reqSeq);
					int resultCnt = ittService.deleteGroupwareExt(param);
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
	
	// 전표 전자결재 초기화
	@RequestMapping(value = "groupwareChit.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView groupwareChit(HttpServletRequest request, ModelAndView mav) {
		log.info("groupwareChit.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("itt/groupwareChit");
		return mav;
	}
	
	// 전표 전자결재 초기화 조회
	@RequestMapping(value = "getGroupwareChitAjax.json")
	public ModelAndView getGroupwareChitAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getGroupwareChitAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yyyy = StringUtil.reqNullCheck(request, "yyyy");
				String chitNum = StringUtil.reqNullCheck(request, "chitNum");
				
				if ("".equals(yyyy) || "".equals(chitNum)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					GroupwareExtInfo param = new GroupwareExtInfo();
					param.setTsYy(yyyy);
					param.setTsNo(chitNum);
					GroupwareExtInfo info1 = ittService.getGroupwareChitByRyacc(param);
					GroupwareExtInfo info2 = ittService.getGroupwareChitByGwif(param);
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "success");
					mav.addObject("info1", info1);
					mav.addObject("info2", info2);
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
	
	// 전표 전자결재 초기화 및 삭제
	@RequestMapping(value = "setGroupwareChitAjax.json")
	public ModelAndView setGroupwareChitAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("setGroupwareChitAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String yyyy = StringUtil.reqNullCheck(request, "yyyy");
				String chitNum = StringUtil.reqNullCheck(request, "chitNum");
				String actionCode = StringUtil.reqNullCheck(request, "actionCode");
				
				if ("".equals(yyyy) || "".equals(chitNum) || "".equals(actionCode)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					GroupwareExtInfo param = new GroupwareExtInfo();
					param.setTsYy(yyyy);
					param.setTsNo(chitNum);
					int resultCnt = 0;
					if (actionCode.equals("upd")) {
						resultCnt = ittService.setGroupwareChitByRyacc(param);
					} else if (actionCode.equals("del")) {
						resultCnt = ittService.setGroupwareChitByGwif(param);
					}
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
