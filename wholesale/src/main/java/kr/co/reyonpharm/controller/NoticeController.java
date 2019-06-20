package kr.co.reyonpharm.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.handler.CustomExceptionHandler;
import kr.co.reyonpharm.models.NoticeInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.service.NoticeService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/notice/")
public class NoticeController extends CustomExceptionHandler {

	@Autowired
	NoticeService noticeService;
	
	// 공지사항 리스트
	@RequestMapping(value = "noticeList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView noticeList(HttpServletRequest request, ModelAndView mav) {
		log.info("noticeList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		
		String queStr = StringUtil.nullCheck(request.getQueryString());
		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String title = StringUtil.reqNullCheckHangulUTF8(request, "title");
		String contents = StringUtil.reqNullCheckHangulUTF8(request, "contents");

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setTitle(title);
		pageParam.setContents(contents);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = noticeService.getNoticeInfoListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<NoticeInfo> list = noticeService.getNoticeInfoList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		
		mav.setViewName("notice/noticeList");
		return mav;
	}

	// 공지사항 상세
	@RequestMapping(value = "noticeView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView noticeView(HttpServletRequest request, ModelAndView mav) {
		log.info("noticeView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		String seq = StringUtil.reqNullCheck(request, "seq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		NoticeInfo param = new NoticeInfo();
		param.setNoticeSeq(seq);
		NoticeInfo info = noticeService.getNoticeInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("notice/noticeView");
		return mav;
	}

	// 공지사항 추가
	@RequestMapping(value = "noticeAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView noticeAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("noticeAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		mav.setViewName("notice/noticeAdd");
		return mav;
	}
	
	// 공지사항 수정
	@RequestMapping(value = "noticeModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView noticeModify(HttpServletRequest request, ModelAndView mav) {
		log.info("noticeModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		String seq = StringUtil.reqNullCheck(request, "seq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(seq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		NoticeInfo param = new NoticeInfo();
		param.setNoticeSeq(seq);
		NoticeInfo info = noticeService.getNoticeInfo(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("seq", seq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("notice/noticeModify");
		return mav;
	}
	
	// 공지사항 추가 액션
	@RequestMapping(value = "noticeAddAjax.json")
	public ModelAndView noticeAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("noticeAddAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String title = StringUtil.reqNullCheckHangulUTF8(request, "title");
				String contents = StringUtil.reqNullCheckHangulUTF8(request, "contents");
				String regUser = SpringSecurityUtil.getUsername();
				String regName = SpringSecurityUtil.getSaupName();
				
				NoticeInfo info = new NoticeInfo();
				info.setTitle(title);
				info.setContents(contents);
				info.setRegUser(regUser);
				info.setRegName(regName);
				info.setUpdUser(regUser);
				info.setUpdName(regName);
				int resultCnt = noticeService.addNoticeInfo(info);
				
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
				if (fileNames.hasNext()) {
					String noticeSeq = String.valueOf(info.getCurrseq());
					
					MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
					String fileName = uploadfile.getOriginalFilename();

					String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
					StringBuffer fileFullPath = new StringBuffer(filePath);
					fileFullPath.append("notice/");
					fileFullPath.append(noticeSeq);
					fileFullPath.append("/");

					File targetDir = new File(fileFullPath.toString());
					if (!targetDir.exists()) {
						targetDir.mkdirs();
					}

					log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
					File convFile = new File(fileFullPath.toString() + fileName);
					uploadfile.transferTo(convFile);

					long attachFilesize = 0;
					if (convFile.exists()) {
						attachFilesize = convFile.length();
					}
					
					info.setNoticeSeq(noticeSeq);
					info.setAttachFilepath(fileFullPath.toString());
					info.setAttachFilename(fileName);
					info.setAttachFilesize(String.valueOf(attachFilesize));
					
					int resultFileCnt = noticeService.modifyNoticeInfoWithFile(info);
					log.info("resultFileCnt : " + resultFileCnt);
				}

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
	
	// 공지사항 수정 액션
	@RequestMapping(value = "noticeModifyAjax.json")
	public ModelAndView noticeModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("noticeModifyAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String noticeSeq = StringUtil.reqNullCheckHangulUTF8(request, "noticeSeq");
				String title = StringUtil.reqNullCheckHangulUTF8(request, "title");
				String contents = StringUtil.reqNullCheckHangulUTF8(request, "contents");
				String attFileType = StringUtil.reqNullCheckHangulUTF8(request, "attFileType");
				String updUser = SpringSecurityUtil.getUsername();
				String updName = SpringSecurityUtil.getSaupName();
				
				NoticeInfo info = new NoticeInfo();
				info.setNoticeSeq(noticeSeq);
				info.setTitle(title);
				info.setContents(contents);
				info.setUpdUser(updUser);
				info.setUpdName(updName);
				
				int resultCnt = noticeService.modifyNoticeInfo(info);
				
				if (attFileType.equals("A")) {
					// 변경 없음
				} else if (attFileType.equals("B")) {
					// 새로운 첨부파일 사용
					NoticeInfo result = noticeService.getNoticeInfo(info);
					String attachFilepath = result.getAttachFilepath();
					if (attachFilepath != null) {
						FileIOUtils.deleteAllFiles(attachFilepath);
					}
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
					if (fileNames.hasNext()) {
						MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
						String fileName = uploadfile.getOriginalFilename();

						String filePath = Constants.configProp.getProperty(Constants.SYSTEM_FILE_DIR);
						StringBuffer fileFullPath = new StringBuffer(filePath);
						fileFullPath.append("notice/");
						fileFullPath.append(noticeSeq);
						fileFullPath.append("/");

						File targetDir = new File(fileFullPath.toString());
						if (!targetDir.exists()) {
							targetDir.mkdirs();
						}

						log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
						File convFile = new File(fileFullPath.toString() + fileName);
						uploadfile.transferTo(convFile);

						long attachFilesize = 0;
						if (convFile.exists()) {
							attachFilesize = convFile.length();
						}
						
						info.setNoticeSeq(noticeSeq);
						info.setAttachFilepath(fileFullPath.toString());
						info.setAttachFilename(fileName);
						info.setAttachFilesize(String.valueOf(attachFilesize));
						
						int resultFileCnt = noticeService.modifyNoticeInfoWithFile(info);
						log.info("resultFileCnt : " + resultFileCnt);
					}
				} else if (attFileType.equals("C")) {
					// 첨부파일 삭제
					NoticeInfo result = noticeService.getNoticeInfo(info);
					String attachFilepath = result.getAttachFilepath();
					if (attachFilepath != null) {
						FileIOUtils.deleteAllFiles(attachFilepath);
					}
					int resultFileCnt = noticeService.modifyNoticeInfoWithFile(info);
					log.info("[" + attFileType + "] resultFileCnt : " + resultFileCnt);
				}

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
	
	// 공지사항 삭제 액션
	@RequestMapping(value = "noticeDeleteAjax.json")
	public ModelAndView noticeDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("noticeDeleteAjax.json");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String noticeSeq = StringUtil.reqNullCheckHangulUTF8(request, "seq");
				NoticeInfo info = new NoticeInfo();
				info.setNoticeSeq(noticeSeq);
				
				// 첨부파일 삭제
				NoticeInfo result = noticeService.getNoticeInfo(info);
				String attachFilepath = result.getAttachFilepath();
				if (attachFilepath != null) {
					FileIOUtils.deleteAllFiles(attachFilepath);
				}
				
				int resultCnt = noticeService.deleteNoticeInfo(info);
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
	
	// 파일 다운로드
	@RequestMapping(value = "noticeFileDownload.do")
	public ModelAndView noticeFileDownload(HttpServletRequest request) {
		log.info("/noticeFileDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getSaupName() + "]");
		ModelAndView mav = new ModelAndView("downloadView");
		
		String seq = StringUtil.reqNullCheckHangulUTF8(request, "seq");

		NoticeInfo param = new NoticeInfo();
		param.setNoticeSeq(seq);
		NoticeInfo info = noticeService.getNoticeInfo(param);
		
		String filePath = info.getAttachFilepath() + info.getAttachFilename();

		if (filePath == null || filePath.equals("")) {
			log.error(CustomExceptionCodes.FILE_NOT_EXIST.getMsg());
			throw new CustomException(CustomExceptionCodes.FILE_NOT_EXIST);
		}

		mav.addObject("filePath", filePath);
		return mav;
	}
		
}
