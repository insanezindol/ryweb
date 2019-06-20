package kr.co.reyonpharm.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sso/")
public class SSOController extends CustomExceptionHandler {

	/* SSO 컨트롤러 */

	// SSO 인증 처리 후 메인페이지 이동
	// http://192.168.1.84/sso/auth.do?uid=MTgwMjEyMDE=
	// http://192.168.1.84/sso/auth.do?uid=MTgwMjEyMDE%3D
	// http://rms.reyonpharm.co.kr/sso/auth.do?uid=MTgwMjEyMDE%3D
	@RequestMapping(value = "auth.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView auth(HttpServletRequest request, ModelAndView mav) {
		log.info("auth.do");
		String uid = StringUtil.reqNullCheck(request, "uid");
		log.info("[ENC] uid : " + uid);
		if (uid.equals("")) {
			return new ModelAndView("redirect:/main.do");
		}
		byte[] decoded = Base64.decodeBase64(uid.getBytes());
		uid = new String(decoded);
		log.info("[DEC] uid : " + uid);
		mav.addObject("uid", uid);
		mav.setViewName("sso/auth");
		return mav;
	}
	
	// 18021201 / 20190226
	// http://192.168.1.84/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAyMjY=
	// http://rms.reyonpharm.co.kr/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAyMjY=
	// 18021201 / 20190227
	// http://192.168.1.84/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAyMjc=
	// http://rms.reyonpharm.co.kr/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAyMjc=
	// 18021201 / 20190305
	// http://192.168.1.84/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAzMDU=
	// http://rms.reyonpharm.co.kr/sso/access.do?uid=MTgwMjEyMDE%3D&tz=MjAxOTAzMDU=
	@RequestMapping(value = "access.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView access(HttpServletRequest request, ModelAndView mav) {
		log.info("access.do");
		String uid = StringUtil.reqNullCheck(request, "uid");
		String tz = StringUtil.reqNullCheck(request, "tz");
		log.info("[ENC] uid : " + uid);
		log.info("[ENC] tz : " + tz);
		if (uid.equals("") || tz.equals("")) {
			return new ModelAndView("redirect:/main.do");
		}
		byte[] decodedUid = Base64.decodeBase64(uid.getBytes());
		uid = new String(decodedUid);
		byte[] decodedTz = Base64.decodeBase64(tz.getBytes());
		tz = new String(decodedTz);
		log.info("[DEC] uid : " + uid);
		log.info("[DEC] tz : " + tz);
		mav.addObject("uid", uid);
		mav.addObject("tz", tz);
		mav.setViewName("sso/access");
		return mav;
	}

}
