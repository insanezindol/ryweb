package kr.co.reyonpharm.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.util.CommonUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		/*
		 * 로그인은 했으나 권한이 부족한 경우
		 */
		log.debug("handle start");
		if (SpringSecurityUtil.isAjaxRequest(request)) {
			Map<String, Object> jsonData = new HashMap<String, Object>();

			jsonData.put("resultCode", CustomExceptionCodes.ERROR_401.getId());
			jsonData.put("resultMsg", CustomExceptionCodes.ERROR_401.getMsg());
			CommonUtils.jsonResponse(response, jsonData);
		} else {
			request.setAttribute("resultCode", CustomExceptionCodes.ERROR_401.getId());
			request.setAttribute("resultMsg", CustomExceptionCodes.ERROR_401.getMsg());

			request.getRequestDispatcher("/error403.do").forward(request, response);
		}
		log.debug("handle end");

	}
}
