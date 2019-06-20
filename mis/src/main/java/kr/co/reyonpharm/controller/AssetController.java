 package kr.co.reyonpharm.controller;

import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.TokenInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.AssetService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.ReyonSha256;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/asset/")
public class AssetController {
	
	@Autowired
	AssetService assetService;
	
	// 빈 화면
	@RequestMapping(value = "blank.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView blank(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("blank.do");
		mav.setViewName("asset/blank");
		return mav;
	}
	
	// 로그인 화면
	@RequestMapping(value = "login.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("login.do");
		
		if(CommonUtils.isAssetLogin(request)) {
			return new ModelAndView("redirect:/asset/main.do");
		}
		
		mav.setViewName("asset/login");
		return mav;
	}
	
	// 로그인 화면
	@RequestMapping(value = "logout.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("logout.do");
		CommonUtils.delCookies(request, response, "sabun");
		CommonUtils.delCookies(request, response, "saupcode");
		CommonUtils.delCookies(request, response, "deptCode");
		CommonUtils.delCookies(request, response, "deptName");
		CommonUtils.delCookies(request, response, "kname");
		CommonUtils.delCookies(request, response, "posLog");
		CommonUtils.delCookies(request, response, "gender");
		return new ModelAndView("redirect:/asset/login.do");
	}
	
	// 로그인 액션
	@RequestMapping(value = "loginCheckAjax.json")
	@ResponseBody
	public Map loginCheckAjax(HttpServletRequest request, HttpServletResponse response) {
		log.info("loginCheckAjax.json");
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String username = StringUtil.reqNullCheck(request, "username");
			String password = StringUtil.reqNullCheck(request, "password");
			String token = StringUtil.reqNullCheck(request, "token");
			String deviceType = StringUtil.reqNullCheck(request, "deviceType");
			String msgReceiveType = StringUtil.reqNullCheck(request, "msgReceiveType");

			password = URLDecoder.decode((String) password, "UTF-8");
			token = URLDecoder.decode((String) token, "UTF-8");
			
			if ("".equals(auth) || "".equals(username) || "".equals(password) || "".equals(token) || "".equals(deviceType) || "".equals(msgReceiveType)) {
				result.put("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
				result.put("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			} else {
				if (auth.equals("reyon")) {
					UserInfo param = new UserInfo();
					param.setSabun(username);
					UserInfo userInfo = assetService.getUserInfo(param);

					if (null == userInfo) {
						log.error("SUPER USER LOGIN FAIL");
						result.put("resultCode", CustomExceptionCodes.INVALID_PASSWORD.getId());
						result.put("resultMsg", CustomExceptionCodes.INVALID_PASSWORD.getMsg());
					} else {
						String clientIp = CommonUtils.getClientIp(request);
						String ShaEncPwd = ReyonSha256.getCiperText(password);
						log.info("[" + clientIp + "] SABUN : " + username + " , TOKEN : " + token);

						// reyonadmin123!@# , reyonceo123!@#
						if (ShaEncPwd.equals("EB12CB034C397E2ABD202ABA9028EE907A8451D2DAE4D835066FD58CC9331347") || ShaEncPwd.equals("DA40FBE4804D51771B1CB507CF546AD73DBEE59AE8F8B99446B125A3D1ABADE0")) {

							TokenInfo tokenInfoparam = new TokenInfo();
							tokenInfoparam.setSabun(username);
							tokenInfoparam.setTokenId(token);
							tokenInfoparam.setDeviceType(deviceType);
							tokenInfoparam.setMsgReceiveType(msgReceiveType);
							TokenInfo tokenInfo = assetService.getTokenInfo(tokenInfoparam);
							if (tokenInfo == null) {
								int addCnt = assetService.addTokenInfo(tokenInfoparam);
								log.debug("addCnt : " + addCnt);
							} else {
								if (!tokenInfo.getTokenId().equals(token)) {
									int modifyCnt = assetService.modifyTokenInfo(tokenInfoparam);
									log.debug("modifyCnt : " + modifyCnt);
								}
							}

							log.info("SUPER USER LOGIN PASS");
							userInfo.setPassword("");
							result.put("resultCode", 0);
							result.put("resultMsg", "success");
							result.put("userInfo", userInfo);
						} else {
							// 패스워드 체크
							if (!ShaEncPwd.equals(userInfo.getPassword())) {
								log.error("LOGIN FAIL");
								result.put("resultCode", CustomExceptionCodes.INVALID_PASSWORD.getId());
								result.put("resultMsg", CustomExceptionCodes.INVALID_PASSWORD.getMsg());
							} else {
								TokenInfo tokenInfoparam = new TokenInfo();
								tokenInfoparam.setSabun(username);
								tokenInfoparam.setTokenId(token);
								tokenInfoparam.setDeviceType(deviceType);
								tokenInfoparam.setMsgReceiveType(msgReceiveType);
								TokenInfo tokenInfo = assetService.getTokenInfo(tokenInfoparam);
								if (tokenInfo == null) {
									int addCnt = assetService.addTokenInfo(tokenInfoparam);
									log.debug("addCnt : " + addCnt);
								} else {
									if (!tokenInfo.getTokenId().equals(token)) {
										int modifyCnt = assetService.modifyTokenInfo(tokenInfoparam);
										log.debug("modifyCnt : " + modifyCnt);
									}
								}

								log.info("COMMON USER LOGIN SUCCESS");
								userInfo.setPassword("");
								
								CommonUtils.setCookies(request, response, "sabun", userInfo.getSabun());
								CommonUtils.setCookies(request, response, "saupcode", userInfo.getSaupcode());
								CommonUtils.setCookies(request, response, "deptCode", userInfo.getDeptCode());
								CommonUtils.setCookies(request, response, "deptName", userInfo.getDeptName());
								CommonUtils.setCookies(request, response, "kname", userInfo.getKname());
								CommonUtils.setCookies(request, response, "posLog", userInfo.getPosLog());
								CommonUtils.setCookies(request, response, "gender", userInfo.getGender());
								
								result.put("resultCode", 0);
								result.put("resultMsg", "success");
								result.put("userInfo", userInfo);
							}
						}
					}
				} else {
					log.error("auth : " + auth);
					result.put("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
					result.put("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
				}
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			result.put("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			result.put("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}
		return result;
	}
	
	// 메인 화면
	@RequestMapping(value = "main.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("main.do");
		
		if(!CommonUtils.isAssetLogin(request)) {
			return new ModelAndView("redirect:/asset/login.do");
		} else {
			String sabun = CommonUtils.getCookies(request, "sabun");
			
			AlarmInfo param = new AlarmInfo();
			param.setAlarmSabun(sabun);
			param.setAlarmYn("Y");
			List<AlarmInfo> alarmInfo = assetService.getAlarmList(param);
			
			mav.addObject("alarmInfo", alarmInfo);
			mav.setViewName("asset/main");
			return mav;
		}
	}
	
}
