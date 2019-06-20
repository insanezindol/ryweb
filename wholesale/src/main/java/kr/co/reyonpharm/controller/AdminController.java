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
import kr.co.reyonpharm.models.LoginHistoryInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.AdminService;
import kr.co.reyonpharm.util.ReyonSha256;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/")
public class AdminController extends CustomExceptionHandler {

	@Autowired
	AdminService adminService;
	
	// 사용자 관리 리스트
	@RequestMapping(value = "userList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView userList(HttpServletRequest request, ModelAndView mav) {
		log.info("userList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());
		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
		String saupName = StringUtil.reqNullCheckHangulUTF8(request, "saupName");
		String userRole = StringUtil.reqNullCheckHangulUTF8(request, "userRole");
		String useYn = StringUtil.reqNullCheckHangulUTF8(request, "useYn");

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setUsername(username);
		pageParam.setSaupName(saupName);
		pageParam.setUserRole(userRole);
		pageParam.setUseYn(useYn);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = adminService.getUserInfoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<UserInfo> list = adminService.getUserInfoList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		
		mav.setViewName("admin/userList");
		return mav;
	}

	// 사용자 관리 상세
	@RequestMapping(value = "userView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView userView(HttpServletRequest request, ModelAndView mav) {
		log.info("userView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		
		String seq = StringUtil.reqNullCheck(request, "seq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		UserInfo param = new UserInfo();
		param.setUsername(seq);
		UserInfo info = adminService.getUserInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("admin/userView");
		return mav;
	}

	// 사용자 관리 추가
	@RequestMapping(value = "userAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView userAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("userAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		mav.setViewName("admin/userAdd");
		return mav;
	}
	
	// 사용자 관리 추가
	@RequestMapping(value = "userModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView userModify(HttpServletRequest request, ModelAndView mav) {
		log.info("userModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		
		String seq = StringUtil.reqNullCheck(request, "seq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		UserInfo param = new UserInfo();
		param.setUsername(seq);
		UserInfo info = adminService.getUserInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("admin/userModify");
		return mav;
	}
	
	// 사용자 관리 추가 액션
	@RequestMapping(value = "userAddAjax.json")
	public ModelAndView userAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("userAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String pwd = StringUtil.reqNullCheckHangulUTF8(request, "pwd");
				String saupName = StringUtil.reqNullCheckHangulUTF8(request, "saupName");
				String userRole = StringUtil.reqNullCheckHangulUTF8(request, "userRole");
				String useYn = StringUtil.reqNullCheckHangulUTF8(request, "useYn");
				
				// SHA256 암호화
				String password = ReyonSha256.getCiperText(pwd);
				
				UserInfo info = new UserInfo();
				info.setUsername(username);
				info.setPassword(password);
				info.setSaupName(saupName);
				info.setUserRole(userRole);
				info.setUseYn(useYn);
				
				int resultCnt = adminService.addUserInfo(info);

				mav.addObject("resultCode", resultCnt);
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
	
	// 사용자 관리 수정 액션
	@RequestMapping(value = "userModifyAjax.json")
	public ModelAndView userModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("userModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String pwd = StringUtil.reqNullCheckHangulUTF8(request, "pwd");
				String saupName = StringUtil.reqNullCheckHangulUTF8(request, "saupName");
				String userRole = StringUtil.reqNullCheckHangulUTF8(request, "userRole");
				String useYn = StringUtil.reqNullCheckHangulUTF8(request, "useYn");
				
				String password = "";
				if(!pwd.equals("")) {
					// SHA256 암호화
					password = ReyonSha256.getCiperText(pwd);
				}
				
				UserInfo info = new UserInfo();
				info.setUsername(username);
				info.setPassword(password);
				info.setSaupName(saupName);
				info.setUserRole(userRole);
				info.setUseYn(useYn);
				
				int resultCnt = adminService.modifyUserInfo(info);

				mav.addObject("resultCode", resultCnt);
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
	
	// 사용자 관리 삭제 액션
	@RequestMapping(value = "userDeleteAjax.json")
	public ModelAndView userDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("userDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String username = StringUtil.reqNullCheckHangulUTF8(request, "seq");
				UserInfo info = new UserInfo();
				info.setUsername(username);
				int resultCnt = adminService.deleteUserInfo(info);
				mav.addObject("resultCode", resultCnt);
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
	
	// 사업자 번호 존재 여부 체크 액션
	@RequestMapping(value = "usernameCheckAjax.json")
	public ModelAndView usernameCheckAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("usernameCheckAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				UserInfo info = new UserInfo();
				info.setUsername(username);
				int resultCnt = adminService.getUsernameCheckCount(info);
				mav.addObject("resultCode", resultCnt);
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

	// 비밀번호 변경 액션
	@RequestMapping(value = "pwdChangeAjax.json")
	public ModelAndView pwdChangeAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("pwdChangeAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String currentPwd = StringUtil.reqNullCheckHangulUTF8(request, "currentPwd");
				String pwd = StringUtil.reqNullCheckHangulUTF8(request, "pwd");
				String username = SpringSecurityUtil.getUsername();
				UserInfo param = new UserInfo();
				param.setUsername(username);
				UserInfo info = adminService.getUserInfo(param);
				
				// SHA256 암호화
				String shaCurrentPwd = ReyonSha256.getCiperText(currentPwd);
				
				if(shaCurrentPwd.equals(info.getPassword())) {
					// SHA256 암호화
					String password = ReyonSha256.getCiperText(pwd);
					param.setPassword(password);
					int resultCnt = adminService.modifyUserPwd(param);
					mav.addObject("resultCode", resultCnt);
					mav.addObject("resultMsg", "success");
				} else {
					mav.addObject("resultCode", 0);
					mav.addObject("resultMsg", "currentPwd is not correct");
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
	
	// 접속 로그 관리 리스트
	@RequestMapping(value = "loginLogList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loginLogList(HttpServletRequest request, ModelAndView mav) {
		log.info("loginLogList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());
		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
		String ip = StringUtil.reqNullCheckHangulUTF8(request, "ip");

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setUsername(username);
		pageParam.setIp(ip);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = adminService.getLoginLogListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<LoginHistoryInfo> list = adminService.getLoginLogList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		
		mav.setViewName("admin/loginLogList");
		return mav;
	}
	
}
