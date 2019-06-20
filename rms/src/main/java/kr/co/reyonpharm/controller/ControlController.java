package kr.co.reyonpharm.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.reyonpharm.handler.CustomException;
import kr.co.reyonpharm.handler.CustomExceptionCodes;
import kr.co.reyonpharm.models.ControlInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.ControlService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.EncriptBasicDataSource;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;

@Controller
@RequestMapping("/control/")
public class ControlController {
	private static Logger log = LoggerFactory.getLogger("ControlLog");

	@Autowired
	ControlService controlService;

	@Autowired
	CommonService commonService;

	@RequestMapping(value = "controlList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlList(HttpServletRequest request, ModelAndView mav) {
		log.info("controlList.do");

		// 페이징처리
		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");

		String s_content = StringUtil.reqNullCheckHangulUTF8(request, "s_content");
		String regDate = StringUtil.reqNullCheckHangulUTF8(request, "regDate");
		String s_gubun = StringUtil.reqNullCheck(request, "s_gubun");

		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 15 : pageSize);
		pageParam.setS_content(s_content);
		pageParam.setRegDate(regDate);
		pageParam.setS_gubun(s_gubun);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = controlService.getControlListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ControlInfo> list = controlService.getControlList(pageParam);

		// 두 가지 object 넘겨야함 1. PageParam, List
		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);

		mav.setViewName("control/controlList");
		return mav;
	}

	@RequestMapping(value = "controlView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlView(HttpServletRequest request, ModelAndView mav) {
		log.info("controlView.do");

		String ryno = StringUtil.reqNullCheck(request, "num");
		String sabun = SpringSecurityUtil.getUsername();

		ControlInfo param = new ControlInfo();
		ControlInfo fileParam = null;
		ControlInfo log = null;

		param.setReqno(ryno);

		fileParam = controlService.getControlFilePath(param);
		log = controlService.getControlLog(param);
		param = controlService.getControl(param);

		// isMine처리
		if (sabun.equals(param.getInsemp())) {
			mav.addObject("isMine", "true");
		} else {
			mav.addObject("isMine", "false");
		}

		mav.addObject("info", param);
		mav.addObject("logInfo", log);
		mav.addObject("fileInfo", fileParam);
		mav.setViewName("/control/controlView");

		return mav;
	}

	@RequestMapping(value = "controlAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlAdd(HttpServletRequest request, ModelAndView mav) {

		mav.setViewName("control/controlAdd");
		return mav;
	}

	@RequestMapping(value = "controlAddAjax.json")
	public ModelAndView controlAddAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("controlAddAjax.json");

		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				// 정상접근
				String regSabun = SpringSecurityUtil.getUsername();
				String [] rynoParam = request.getParameterValues("ryno");
				String [] contents = request.getParameterValues("contents"); 
				//condesc BOM마다 각각 적용될 수 있도록 수정
				for (String tmp : rynoParam) {
					log.info("넘어온 값 : " + tmp);
				}
				for (String tmp : contents) {
					log.info("넘어온 값 : " + tmp);
				}

				String [] ryno = rynoParam[0].split(",");
				String [] condesc = contents[0].split(",");

				if ("".equals(rynoParam[0]) || "".equals(contents[0])) {
					log.error("rynoParam[0] : " + rynoParam[0] + " condesc : " + contents[0]);
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					ControlInfo param = new ControlInfo();
					param.setSabun(regSabun);
					// param.setRyno(ryno);
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
					String[] passIndex = request.getParameterValues("passIndex");
					String[] index = passIndex[0].split(",");
					if ( "".equals(index[0]) )
						index[0] = "-1";

					for (int i = 0; i < ryno.length; i++) {
						param.setRyno(ryno[i]);
						param.setCondesc(condesc[i]);

						if (controlService.setControl(param) != 1) {
							mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
							mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
							return mav;
						} else {
							// 통제 정상으로 걸었다면 파일처리
							boolean pass = false;
							for ( int j = 0 ; j<index.length ; j++ ) {
								if ( i == Integer.parseInt(index[j]) ) pass = true;
								else continue;
							}
							
							if (fileNames.hasNext() && pass == false) {
								String controlSeq = String.valueOf(param.getCurrseq()); // select key 자동으로 오나보네
								MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
								String fileName = uploadfile.getOriginalFilename();
								String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_CONTROL_DIR);
								StringBuffer fileFullPath = new StringBuffer(filePath);
								fileFullPath.append(controlSeq);
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

								ControlInfo fileParam = new ControlInfo();
								fileParam.setReqno(controlSeq);
								fileParam.setReqfilename(fileName);
								fileParam.setFilepath(fileFullPath.toString());
								int resultFileCnt = controlService.addControlFilePath(fileParam);
								log.info("FILE UPDATE CNT : " + resultFileCnt);

							}
						}
					}
					param.setReqno(Long.toString(param.getCurrseq()));
					param = controlService.getControl(param);

					mav.addObject("resultCode", "1");
					mav.addObject("resultMsg", "success");

					List<ControlInfo> mi = controlService.getEmailAddressList();
					StringBuffer sb = new StringBuffer();

					sb.append("<h3>이연제약 관리 시스템입니다.</h3>");
					sb.append("<h3>다음과 같은 이력이 발생했으니 확인해주시기 바랍니다.</h3><hr>");
					sb.append("<h4>품명 : " + param.getJpmnm() + "</h4>");
					sb.append("<h4>내용 : " + param.getCondesc() + "</h4>");
					sb.append("<h4>통제일자 : " + param.getInsdate() + "</h4><hr>");
					sb.append("<a href='http://rms.reyonpharm.co.kr'>이연제약 관리 시스템 바로가기</a>");

//					for ( int i = 0 ; i<mi.size() ; i++ )
//						commonService.sendNotifyEmailByGw(mi.get(i).getMailId(), "재고자산 통제 이력이 발생했습니다.", sb);
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

	@RequestMapping(value = "controlDeleteAjax.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlDeleteAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("controlDeleteAjax.json");

		String auth = StringUtil.reqNullCheck(request, "auth");
		if (auth.equals("reyon")) {
			// 정상접근
			String reqno = StringUtil.reqNullCheck(request, "reqno");

			ControlInfo param = new ControlInfo();
			ControlInfo mi = null;

			param.setReqno(reqno);

			mi = controlService.getControlFilePath(param);
			if (mi != null) {
				FileIOUtils.deleteAllFiles(mi.getFilepath());
				controlService.delControlFilePath(param);
			}

			int resultCode = controlService.delControl(param);

			mav.addObject("resultCode", resultCode);
			mav.addObject("resultMsg", "success");
		} else {
			log.error("auth : " + auth);
			mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
		}

		return mav;
	}

	@RequestMapping(value = "controlSelect.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlSelect(HttpServletRequest request, ModelAndView mav) {
		log.info("controlSelect.json");

		try {
			int resultCnt = 0;
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				// 정상접근
				String jpmnm = StringUtil.reqNullCheck(request, "jpmnm");
				if ("".equals(jpmnm)) {
					log.error("seq : " + jpmnm);
					mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
				}
				List<ControlInfo> param = null;
				ControlInfo mi = new ControlInfo();

				mi.setJpmnm(StringUtil.reqNullCheckHangulUTF8(request, "jpmnm"));
				param = controlService.getControlMaster(mi);

				mav.addObject("controlList", param);

				mav.addObject("resultCode", resultCnt);
				mav.addObject("resultMsg", "success");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}

		return mav;
	}

	@RequestMapping(value = "BOMSelect.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView bomSelect(HttpServletRequest request, ModelAndView mav) {
		log.info("bomSelect.json");

		try {
			int resultCnt = 0;
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				// 정상접근
				String jpmcd = StringUtil.reqNullCheck(request, "jpmcd");
				if ("".equals(jpmcd)) {
					log.error("seq : " + jpmcd);
					mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
				}
				List<ControlInfo> param = null;
				ControlInfo mi = new ControlInfo();

				mi.setRyno(jpmcd);
				param = controlService.getBom(mi);

				mav.addObject("controlList", param);

				mav.addObject("resultCode", resultCnt);
				mav.addObject("resultMsg", "success");
			}
		} catch (Exception e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			mav.addObject("resultCode", CustomExceptionCodes.SYSTEM_ERROR.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.SYSTEM_ERROR.getMsg());
		}

		return mav;
	}

	@RequestMapping(value = "controlFileDownload.do")
	public ModelAndView controlFileDownload(HttpServletRequest request) {
		log.info("controlFileDownload.do");

		ModelAndView mav = new ModelAndView("downloadView");

		String auth = StringUtil.reqNullCheck(request, "dwAuth");
		String reqno = StringUtil.reqNullCheck(request, "dwSeq");
		String filename = StringUtil.reqNullCheckHangulUTF8(request, "dwFilename");

		if (auth.equals("reyon")) {
			// 정상접근
			ControlInfo param = new ControlInfo();
			param.setReqno(reqno);

			ControlInfo mi = controlService.getControlFilePath(param);

			String filePath = mi.getFilepath() + filename;
			log.info(filePath);

			if (filePath == null || filePath.equals("")) {
				log.error(CustomExceptionCodes.FILE_NOT_EXIST.getMsg());
				throw new CustomException(CustomExceptionCodes.FILE_NOT_EXIST);
			}

			mav.addObject("filePath", filePath);
		} else {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		return mav;
	}

	@RequestMapping(value = "controlConfirmAjax.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlConfirmAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("controlConfirmAjax.json");

		String auth = StringUtil.reqNullCheck(request, "auth");
		if (auth.equals("reyon")) {
			// 정상
			String sabun = SpringSecurityUtil.getUsername();
			String reqno = StringUtil.reqNullCheck(request, "reqno");

			if ("".equals(reqno) || reqno == null) {
				log.error("reqno : " + reqno + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
				throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
			}

			ControlInfo param = new ControlInfo();
			ControlInfo info = null;
			
			param.setReqno(reqno);
			param.setCfemp(sabun);
			param.setRelemp(sabun);

			info = controlService.getControl(param);
			int resultCode = controlService.modifyControlLog(param);
			
			if (info.getRyno().length() == 7) { 
				resultCode = controlService.modifyControl(param);
			}
			
			mav.addObject("resultCode", resultCode);
			mav.addObject("resultMsg", "success");
		} else {
			log.error("auth : " + auth);
			mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
		}

		return mav;
	}

	@RequestMapping(value = "confirmAddAjax.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmAddAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("confirmAddAjax.json");

		String sabun = SpringSecurityUtil.getUsername();
		String auth = StringUtil.reqNullCheckHangulUTF8(request, "auth");

		try {
			if (auth.equals("reyon")) {
				// 정상
				String reqno = StringUtil.reqNullCheck(request, "reqno");
				String soldesc = StringUtil.reqNullCheckHangulUTF8(request, "soldesc");
				String isMaterial = StringUtil.reqNullCheck(request, "isMaterial");

				if ("".equals(reqno) || "".equals(soldesc)) {
					log.error("reqno : " + reqno + " soldesc : " + soldesc);
					mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
				}

				ControlInfo param = new ControlInfo();

				param.setReqno(reqno);
				param.setSoldesc(soldesc);
				param.setLogemp(sabun);
				param.setCfemp(sabun);

				int resultCode = controlService.addControlLog(param);

				// 완제품일 때 그냥 검증+확인
				if (isMaterial.equals("false")) {
					log.info(isMaterial);
					int result0 = controlService.modifyControl(param);
					int result1 = controlService.modifyControlLog(param);

					if (result0 == result1)
						resultCode = 1;
				}

				mav.addObject("resultCode", resultCode);
				mav.addObject("resultMsg", "success");

				ControlInfo mailAddr = null;
				ControlInfo mi = null;
				ControlInfo log = null;

				mi = controlService.getControl(param);
				mailAddr = controlService.getEmailAddress(mi);
				log = controlService.getControlLog(param);

				StringBuffer sb = new StringBuffer();

				sb.append("<h3>이연제약 관리 시스템입니다.</h3>");
				sb.append("<h3>다음과 같은 이력이 검증 됐으니 확인해주시기 바랍니다.</h3><hr>");
				sb.append("<h4>품명 : " + mi.getJpmnm() + "</h4>");
				sb.append("<h4>처리내용 : " + log.getSoldesc() + "</h4>");
				sb.append("<h4>처리일자 : " + log.getLogdate() + "</h4><hr>");
				sb.append("<a href='http://rms.reyonpharm.co.kr'>이연제약 관리 시스템 바로가기</a>");

				commonService.sendNotifyEmailByGw(mailAddr.getMailId(), "이력이 검증 되었습니다.", sb);
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

	@RequestMapping(value = "control.json", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView control(HttpServletRequest request, ModelAndView mav) {
		log.info("control.json");
		String auth = StringUtil.reqNullCheck(request, "auth");

		if (!"".equals(auth) || auth != null) {
			// 정상접근
			String reqno = StringUtil.reqNullCheckHangulUTF8(request, "reqno");
			String cfemp = SpringSecurityUtil.getUsername();

			ControlInfo param = new ControlInfo();

			param.setReqno(reqno);
			param.setCfemp(cfemp);

			int resultCode = 0;
			int result0 = controlService.modifyControl(param);
			int result1 = controlService.modifyControlLog(param);

			if (result0 == result1)
				resultCode = 1;

			mav.addObject("resultCode", resultCode);
			mav.addObject("resultMsg", "success");
		}
		return mav;
	}
	
	@RequestMapping(value = "controlCheck.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView controlCheck(HttpServletRequest request, ModelAndView mav) {
		log.info("controlCheck.do");
		String auth = StringUtil.reqNullCheck(request, "auth");

		if (!"".equals(auth) || auth != null) {
			// 정상접근
			String reqno = StringUtil.reqNullCheckHangulUTF8(request, "reqno");
			String cfemp = SpringSecurityUtil.getUsername();

			ControlInfo param = new ControlInfo();

			param.setReqno(reqno);
			param.setCfemp(cfemp);

			ControlInfo file = controlService.getControlFilePath(param);
			ControlInfo info = controlService.getControl(param);
			
			mav.addObject("info", info);
			mav.addObject("file", file);
			mav.setViewName("/control/controlCheck");
		}
		return mav;
	}
	
	@RequestMapping(value = "CheckAddAjax.json", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView CheckAddAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("CheckAddAjax.json");
		
		String auth = StringUtil.reqNullCheck(request, "auth");
		
		if (!"".equals(auth) || auth != null) {
			// 정상접근
			String reqno = StringUtil.reqNullCheckHangulUTF8(request, "reqno");
			String soldesc = StringUtil.reqNullCheckHangulUTF8(request, "soldesc");
			String logemp = SpringSecurityUtil.getUsername();

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
			int sw = 0;
			ControlInfo fileParam = new ControlInfo();
			while ( fileNames.hasNext() ) {
				String controlSeq = reqno; // select key 자동으로 오나보네
				MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
				String fileName = uploadfile.getOriginalFilename();
				String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_CONTROL_DIR);
				StringBuffer fileFullPath = new StringBuffer(filePath);
				fileFullPath.append(controlSeq);
				fileFullPath.append("/");
				
				File targetDir = new File(fileFullPath.toString());
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}

				log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
				File convFile = new File(fileFullPath.toString() + fileName);
				try {
					uploadfile.transferTo(convFile);
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				long attachFilesize = 0;
				if (convFile.exists()) {
					attachFilesize = convFile.length();
				}

				fileParam.setReqno(controlSeq);
				if ( sw == 0 ) {
					fileParam.setBffilename(fileName);
					sw = 1;
				} else {
					fileParam.setAffilename(fileName);
				}
			}
			int resultFileCnt = controlService.addControlFilePath(fileParam);
			log.info("FILE UPDATE CNT : " + resultFileCnt);
			
			ControlInfo param = new ControlInfo();

			param.setReqno(reqno);
			param.setCfemp(logemp);

			ControlInfo file = controlService.getControlFilePath(param);
			ControlInfo info = controlService.getControl(param);
			
			mav.addObject("info", info);
			mav.addObject("file", file);
			mav.setViewName("/control/controlCheck");
		}
		return mav;
	}
	
	@RequestMapping(value = "controlCheckModifyAjax.json", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView controlCheckModifyAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("controlCheckModifyAjax.json");
		
		String auth = StringUtil.reqNullCheck(request, "auth");
		
		if (!"".equals(auth) || auth != null) {
			// 정상접근
			String sabun = SpringSecurityUtil.getUsername();
			String reqno = StringUtil.reqNullCheck(request, "reqno");
			String soldesc = StringUtil.reqNullCheckHangulUTF8(request, "soldesc");
			
			ControlInfo param = new ControlInfo();
			param.setLogemp(sabun);
			param.setSoldesc(soldesc);
			param.setReqno(reqno);
			
			if ( controlService.addControlLog(param) != 1 ) {
				mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
				mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				return mav;
			}
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = multipartRequest.getFileNames();
			List<String> fName = new ArrayList<String>();
			String filePath = "";
			
			while ( fileNames.hasNext() ) {
				String controlSeq = reqno; // select key 자동으로 오나보네
				MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
				String fileName = uploadfile.getOriginalFilename();
				fName.add(uploadfile.getOriginalFilename());
				filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_CONTROL_DIR);
				StringBuffer fileFullPath = new StringBuffer(filePath);
				fileFullPath.append(controlSeq);
				fileFullPath.append("/");
				
				File targetDir = new File(fileFullPath.toString());
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}

				log.info("UPLOAD FILE : " + fileFullPath.toString() + fileName);
				File convFile = new File(fileFullPath.toString() + fileName);
				try {
					uploadfile.transferTo(convFile);
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long attachFilesize = 0;
				if (convFile.exists()) {
					attachFilesize = convFile.length();
				}
			}
			
			if ( fName.size() == 1 ) {
				param.setAffilename(fName.get(0));
			} else if ( fName.size() == 2 ){
				param.setBffilename(fName.get(0));
				param.setAffilename(fName.get(1));
			} else {
				//완제 변경도안 없을 때
				mav.addObject("resultCode", 1);
				return mav;
			}
			
			int resultCode = 0;
			if ( controlService.modifyControlFilePath(param) == 0 ) {
				param.setFilepath(filePath);
				controlService.insertControlFilePath(param);
			}
			resultCode = controlService.modifyControlFilePath(param);
			
			mav.addObject("resultCode", resultCode);
			mav.addObject("resultMsg", "success");
			
		} else {
			log.error("auth : " + auth);
			mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
		}
		
		return mav;
	}
	
	@RequestMapping(value = "controlReleaseAjax.json", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView controlReleaseAjax(HttpServletRequest request, ModelAndView mav) {
		log.info("controlReleaseAjax.json");
		
		String auth = StringUtil.reqNullCheck(request, "auth");
		
		if (!"".equals(auth) || auth != null) {
			// 정상접근
			String sabun = SpringSecurityUtil.getUsername();
			String reqno = StringUtil.reqNullCheck(request, "reqno");
			
			ControlInfo param = new ControlInfo();
			param.setRelemp(sabun);
			param.setReqno(reqno);
			
			int resultCode = controlService.modifyControl(param);
			
			mav.addObject("resultCode", resultCode);
			mav.addObject("resultMsg", "success");
		} else {
			log.error("auth : " + auth);
			mav.addObject("resultCode", CustomExceptionCodes.INVALID_PARAMETER.getId());
			mav.addObject("resultMsg", CustomExceptionCodes.INVALID_PARAMETER.getMsg());
		}
		
		return mav;
	}
}
