package kr.co.reyonpharm.controller;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.AlarmInfo;
import kr.co.reyonpharm.models.TokenInfo;
import kr.co.reyonpharm.models.UserInfo;
import kr.co.reyonpharm.service.AppService;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.ReyonSha256;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Slf4j
@Controller
@RequestMapping("/")
public class AppController extends CommonController {

	@Autowired
	AppService appService;

	// 샘플 템플릿 액션
	@RequestMapping(value = "sampleAjax.json")
	@ResponseBody
	public Map sampleAjax(HttpServletRequest request, HttpServletResponse response) {
		log.info("sampleAjax.json");
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String barcodeNumber = StringUtil.reqNullCheck(request, "barcodeNumber");
			if (auth.equals("reyon")) {
				log.info("barcodeNumber : " + barcodeNumber);
				result.put("resultCode", 0);
				result.put("resultMsg", "success");
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

	// 로그인 액션
	@RequestMapping(value = "loginCheckAjax.json")
	@ResponseBody
	public Map loginCheckAjax(HttpServletRequest request, HttpServletResponse response) {
		log.info("loginCheckAjax.json");
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String username = StringUtil.reqNullCheck(request, "param1");
			String password = StringUtil.reqNullCheck(request, "param2");
			String token = StringUtil.reqNullCheck(request, "param3");
			String deviceType = StringUtil.reqNullCheck(request, "param4");
			String msgReceiveType = StringUtil.reqNullCheck(request, "param5");

			if ("".equals(auth) || "".equals(username) || "".equals(password) || "".equals(token) || "".equals(deviceType) || "".equals(msgReceiveType)) {
				result.put("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
				result.put("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			} else {
				if (auth.equals("reyon")) {
					UserInfo param = new UserInfo();
					param.setSabun(username);
					UserInfo userInfo = appService.getUserInfo(param);

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
							TokenInfo tokenInfo = appService.getTokenInfo(tokenInfoparam);
							if (tokenInfo == null) {
								int addCnt = appService.addTokenInfo(tokenInfoparam);
								log.debug("addCnt : " + addCnt);
							} else {
								if (!tokenInfo.getTokenId().equals(token)) {
									int modifyCnt = appService.modifyTokenInfo(tokenInfoparam);
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
								TokenInfo tokenInfo = appService.getTokenInfo(tokenInfoparam);
								if (tokenInfo == null) {
									int addCnt = appService.addTokenInfo(tokenInfoparam);
									log.debug("addCnt : " + addCnt);
								} else {
									if (!tokenInfo.getTokenId().equals(token)) {
										int modifyCnt = appService.modifyTokenInfo(tokenInfoparam);
										log.debug("modifyCnt : " + modifyCnt);
									}
								}

								log.info("COMMON USER LOGIN SUCCESS");
								userInfo.setPassword("");
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

	// 로그인 액션
	@RequestMapping(value = "getManageAjax.json")
	@ResponseBody
	public Map getManageAjax(HttpServletRequest request, HttpServletResponse response) {
		log.info("getManageAjax.json");
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String sabun = StringUtil.reqNullCheck(request, "sabun");
			String type = StringUtil.reqNullCheck(request, "type");

			if ("".equals(auth) || "".equals(sabun) || "".equals(type)) {
				result.put("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
				result.put("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			} else {
				if (auth.equals("reyon")) {
					if (type.equals("search")) {
						TokenInfo tokenInfoparam = new TokenInfo();
						tokenInfoparam.setSabun(sabun);
						TokenInfo tokenInfo = appService.getTokenInfo(tokenInfoparam);

						result.put("resultCode", 0);
						result.put("resultMsg", "success");
						result.put("tokenInfo", tokenInfo);
					} else if (type.equals("Y") || type.equals("N")) {
						TokenInfo tokenInfoparam = new TokenInfo();
						tokenInfoparam.setSabun(sabun);
						tokenInfoparam.setMsgReceiveType(type);
						int cnt = appService.modifyTokenInfoMsgType(tokenInfoparam);
						log.info("[" + sabun + "] setMsgReceiveType : " + cnt);
						result.put("resultCode", cnt);
						result.put("resultMsg", "success");
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
	
	// 알람 내역 조회 액션
	@RequestMapping(value = "getAlarmAjax.json")
	@ResponseBody
	public Map getAlarmAjax(HttpServletRequest request, HttpServletResponse response) {
		log.info("getAlarmAjax.json");
		Map result = new Hashtable();
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String sabun = StringUtil.reqNullCheck(request, "sabun");

			if ("".equals(auth) || "".equals(sabun)) {
				result.put("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
				result.put("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			} else {
				if (auth.equals("reyon")) {
					AlarmInfo param = new AlarmInfo();
					param.setAlarmSabun(sabun);
					param.setAlarmYn("Y");
					List<AlarmInfo> alarmInfo = appService.getAlarmList(param);
					
					result.put("resultCode", 0);
					result.put("resultMsg", "success");
					result.put("alarmInfo", alarmInfo);
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

}
