package kr.co.reyonpharm.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest req, Exception e) {
		log.error("handleException:" + e.getMessage());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resultCode", 9999);
		modelAndView.addObject("resultMsg", "알 수 없는 오류가 발생하였습니다.");
		log.error(e.getMessage());

		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (req.getHeader("accept").indexOf("text/html") >= 0) {
			modelAndView.setViewName("error/error");
		}

		return modelAndView;
	}

	@ExceptionHandler(CustomException.class)
	public ModelAndView handleCustomException(HttpServletRequest req, CustomException e) {
		log.error("handleCustomException:" + e.getErrorCode());
		log.error("handleCustomException:" + e.getErrorMsg());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resultCode", e.getErrorCode());
		modelAndView.addObject("resultMsg", e.getErrorMsg());

		// 요청 페이지가 Ajax인지 웹 페이지인지 구분하여 처리
		if (req.getHeader("accept").indexOf("text/html") >= 0) {
			modelAndView.setViewName("error/error");
		}

		return modelAndView;
	}
}
