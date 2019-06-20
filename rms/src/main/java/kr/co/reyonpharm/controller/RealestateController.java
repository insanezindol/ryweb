package kr.co.reyonpharm.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
import kr.co.reyonpharm.models.ContractInfo;
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.MainService;
import kr.co.reyonpharm.service.RealestateService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/realestate")
public class RealestateController extends CustomExceptionHandler {

	@Autowired
	MainService mainService;

	@Autowired
	RealestateService realestateService;

	@Autowired
	CommonService commonService;

	/* 부동산 임대차 계약서 */

	// 부동산 임대차 계약서 리스트
	@RequestMapping(value = "realestateList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateList(HttpServletRequest request, ModelAndView mav) {
		log.info("realestateList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_divide = StringUtil.reqNullCheckHangulUTF8(request, "s_divide");
		String s_gubun = StringUtil.reqNullCheckHangulUTF8(request, "s_gubun");
		String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
		String startDate = StringUtil.reqNullCheckHangulUTF8(request, "startDate");
		String endDate = StringUtil.reqNullCheckHangulUTF8(request, "endDate");
		String s_paid = StringUtil.reqNullCheckHangulUTF8(request, "s_paid");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 30 : pageSize);
		pageParam.setS_divide(s_divide);
		pageParam.setS_gubun(s_gubun);
		pageParam.setS_user(s_user);
		pageParam.setStartDate(startDate);
		pageParam.setEndDate(endDate);
		pageParam.setS_paid(s_paid);
		pageParam.setS_status(s_status == "" ? "ING" : s_status);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = realestateService.getContractListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<ContractInfo> list = realestateService.getContractList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("realestate/realestateList");
		return mav;
	}

	// 부동산 임대차 계약서 상세보기
	@RequestMapping(value = "realestateView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateView(HttpServletRequest request, ModelAndView mav) {
		log.info("realestateView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String contractSeq = StringUtil.reqNullCheck(request, "contractSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(contractSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		ContractInfo param = new ContractInfo();
		param.setContractSeq(contractSeq);
		ContractInfo info = realestateService.getContract(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}
		
		// 첨부파일의 확장자가 PDF 이면 temp 폴더로 복사해서 미리보기를 지원
		boolean isPdf = false;
		if ((info.getAttachFilename().substring(info.getAttachFilename().lastIndexOf(".") + 1, info.getAttachFilename().length())).toUpperCase().equals("PDF")) {
			isPdf = true;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = sdf.format(new Date()) + ".pdf";
			mav.addObject("sampleFileName", newFileName);
			try {
				FileUtils.copyFile(new File(info.getAttachFilepath() + info.getAttachFilename()), new File(Constants.configProp.getProperty(Constants.FILE_UPLOAD_TEMP_DIR) + newFileName));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
				isPdf = false;
			}
		}

		mav.addObject("contractSeq", contractSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isPdf", isPdf);
		mav.setViewName("realestate/realestateView");
		return mav;
	}

	// 부동산 임대차 계약서 추가
	@RequestMapping(value = "realestateAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("realestateAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("realestate/realestateAdd");
		return mav;
	}

	// 부동산 임대차 계약서 수정
	@RequestMapping(value = "realestateModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateModify(HttpServletRequest request, ModelAndView mav) {
		log.info("realestateModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String contractSeq = StringUtil.reqNullCheck(request, "contractSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(contractSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		ContractInfo param = new ContractInfo();
		param.setContractSeq(contractSeq);
		ContractInfo info = realestateService.getContract(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("contractSeq", contractSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("realestate/realestateModify");
		return mav;
	}

	// 부동산 임대차 계약서 추가 액션
	@RequestMapping(value = "realestateAddAjax.json")
	public ModelAndView realestateAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("realestateAddAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String saupGubun = StringUtil.reqNullCheckHangulUTF8(request, "saupGubun");
				String division = StringUtil.reqNullCheckHangulUTF8(request, "division");
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String startDate = StringUtil.reqNullCheck(request, "startDate");
				String endDate = StringUtil.reqNullCheck(request, "endDate");
				String roadAddr = StringUtil.reqNullCheckHangulUTF8(request, "roadAddr");
				String jibunAddr = StringUtil.reqNullCheckHangulUTF8(request, "jibunAddr");
				String detailAddr = StringUtil.reqNullCheckHangulUTF8(request, "detailAddr");
				String sinm = StringUtil.reqNullCheckHangulUTF8(request, "sinm");
				String zipno = StringUtil.reqNullCheck(request, "zipno");
				String positionX = StringUtil.reqNullCheck(request, "positionX");
				String positionY = StringUtil.reqNullCheck(request, "positionY");
				String payment = StringUtil.reqNullCheck(request, "payment");
				String deposit = StringUtil.reqNullCheck(request, "deposit");
				String rent = StringUtil.reqNullCheck(request, "rent");
				String administrativeExpenses = StringUtil.reqNullCheck(request, "administrativeExpenses");
				String remarks = StringUtil.reqNullCheckHangulUTF8(request, "remarks");
				String status = StringUtil.reqNullCheck(request, "status");

				// 로그인 정보
				String regSabun = SpringSecurityUtil.getUsername();
				String regName = SpringSecurityUtil.getKname();

				ContractInfo param = new ContractInfo();
				param.setSaupGubun(saupGubun);
				param.setDivision(division);
				param.setUsername(username);
				param.setStartDate(startDate);
				param.setEndDate(endDate);
				param.setRoadAddr(roadAddr);
				param.setJibunAddr(jibunAddr);
				param.setDetailAddr(detailAddr);
				param.setSinm(sinm);
				param.setZipno(zipno);
				param.setPayment(payment);
				param.setDeposit(deposit);
				param.setRent(rent);
				param.setAdministrativeExpenses(administrativeExpenses);
				param.setRemarks(remarks);
				param.setStatus(status);
				param.setRegSabun(regSabun);
				param.setRegName(regName);
				param.setUpdSabun(regSabun);
				param.setUpdName(regName);
				param.setPositionX(positionX);
				param.setPositionY(positionY);

				int resultCnt = realestateService.addContract(param);

				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
				if (fileNames.hasNext()) {
					String contractSeq = String.valueOf(param.getCurrseq());

					MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
					String fileName = uploadfile.getOriginalFilename();

					String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_CONTRACT_DIR);
					StringBuffer fileFullPath = new StringBuffer(filePath);
					fileFullPath.append(contractSeq);
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

					ContractInfo fileParam = new ContractInfo();
					fileParam.setContractSeq(contractSeq);
					fileParam.setAttachFilepath(fileFullPath.toString());
					fileParam.setAttachFilename(fileName);
					fileParam.setAttachFilesize(String.valueOf(attachFilesize));
					int resultFileCnt = realestateService.modifyFileContract(fileParam);
					log.info("FILE UPDATE CNT : " + resultFileCnt);
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

	// 부동산 임대차 계약서 수정 액션
	@RequestMapping(value = "realestateModifyAjax.json")
	public ModelAndView realestateModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("realestateModifyAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String contractSeq = StringUtil.reqNullCheck(request, "contractSeq");
				String saupGubun = StringUtil.reqNullCheckHangulUTF8(request, "saupGubun");
				String division = StringUtil.reqNullCheckHangulUTF8(request, "division");
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String startDate = StringUtil.reqNullCheck(request, "startDate");
				String endDate = StringUtil.reqNullCheck(request, "endDate");
				String roadAddr = StringUtil.reqNullCheckHangulUTF8(request, "roadAddr");
				String jibunAddr = StringUtil.reqNullCheckHangulUTF8(request, "jibunAddr");
				String detailAddr = StringUtil.reqNullCheckHangulUTF8(request, "detailAddr");
				String sinm = StringUtil.reqNullCheckHangulUTF8(request, "sinm");
				String zipno = StringUtil.reqNullCheck(request, "zipno");
				String positionX = StringUtil.reqNullCheck(request, "positionX");
				String positionY = StringUtil.reqNullCheck(request, "positionY");
				String payment = StringUtil.reqNullCheck(request, "payment");
				String deposit = StringUtil.reqNullCheck(request, "deposit");
				String rent = StringUtil.reqNullCheck(request, "rent");
				String administrativeExpenses = StringUtil.reqNullCheck(request, "administrativeExpenses");
				String remarks = StringUtil.reqNullCheckHangulUTF8(request, "remarks");
				String status = StringUtil.reqNullCheck(request, "status");
				String attFileType = StringUtil.reqNullCheck(request, "attFileType");

				// 로그인 정보
				String updSabun = SpringSecurityUtil.getUsername();
				String updName = SpringSecurityUtil.getKname();

				ContractInfo fileData = new ContractInfo();
				fileData.setContractSeq(contractSeq);
				ContractInfo info = realestateService.getContract(fileData);

				ContractInfo param = new ContractInfo();
				param.setContractSeq(contractSeq);
				param.setSaupGubun(saupGubun);
				param.setDivision(division);
				param.setUsername(username);
				param.setStartDate(startDate);
				param.setEndDate(endDate);
				param.setRoadAddr(roadAddr);
				param.setJibunAddr(jibunAddr);
				param.setDetailAddr(detailAddr);
				param.setSinm(sinm);
				param.setZipno(zipno);
				param.setPayment(payment);
				param.setDeposit(deposit);
				param.setRent(rent);
				param.setAdministrativeExpenses(administrativeExpenses);
				param.setRemarks(remarks);
				param.setStatus(status);
				param.setUpdSabun(updSabun);
				param.setUpdName(updName);
				param.setPositionX(positionX);
				param.setPositionY(positionY);

				int resultCnt = realestateService.modifyContract(param);

				if (attFileType.equals("01")) {
					// 첨부파일 변경없음
				} else if (attFileType.equals("02")) {
					// 새로운 첨부파일 사용
					if (info.getAttachFilepath() != null) {
						FileIOUtils.deleteAllFiles(info.getAttachFilepath());
					}

					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
					if (fileNames.hasNext()) {
						MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
						String fileName = uploadfile.getOriginalFilename();

						String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_CONTRACT_DIR);
						StringBuffer fileFullPath = new StringBuffer(filePath);
						fileFullPath.append(contractSeq);
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

						ContractInfo fileParam = new ContractInfo();
						fileParam.setContractSeq(contractSeq);
						fileParam.setAttachFilepath(fileFullPath.toString());
						fileParam.setAttachFilename(fileName);
						fileParam.setAttachFilesize(String.valueOf(attachFilesize));
						int resultFileCnt = realestateService.modifyFileContract(fileParam);
						log.info("FILE UPDATE CNT : " + resultFileCnt);
					}
				} else if (attFileType.equals("03")) {
					// 현재 첨부파일 삭제
					if (info.getAttachFilepath() != null) {
						FileIOUtils.deleteAllFiles(info.getAttachFilepath());
					}

					ContractInfo fileParam = new ContractInfo();
					fileParam.setContractSeq(contractSeq);
					int resultFileCnt = realestateService.modifyFileContract(fileParam);
					log.info("FILE UPDATE CNT : " + resultFileCnt);
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

	// 부동산 임대차 계약서 삭제 액션
	@RequestMapping(value = "realestateDeleteAjax.json")
	public ModelAndView realestateDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("realestateDeleteAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String contractSeq = StringUtil.reqNullCheck(request, "contractSeq");
				String status = StringUtil.reqNullCheck(request, "status");

				// 로그인 정보
				String updSabun = SpringSecurityUtil.getUsername();
				String updName = SpringSecurityUtil.getKname();

				ContractInfo param = new ContractInfo();
				param.setContractSeq(contractSeq);
				param.setStatus(status);
				param.setUpdSabun(updSabun);
				param.setUpdName(updName);

				int resultCnt = realestateService.deleteContract(param);

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

	// 계약서 파일 다운로드
	@RequestMapping(value = "realestateFileDownload.do")
	public ModelAndView realestateFileDownload(HttpServletRequest request) {
		log.info("/realestateFileDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String auth = StringUtil.reqNullCheck(request, "dwAuth");
		String seq = StringUtil.reqNullCheck(request, "dwSeq");
		String filename = StringUtil.reqNullCheckHangulUTF8(request, "dwFilename");

		ModelAndView mav = new ModelAndView("downloadView");

		if (auth.equals("reyon")) {
			ContractInfo param = new ContractInfo();
			param.setContractSeq(seq);
			ContractInfo info = realestateService.getContract(param);

			if (!filename.equals(info.getAttachFilename())) {
				log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
				throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
			}

			String filePath = info.getAttachFilepath() + info.getAttachFilename();

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

	// 부동산 임대차 계약서 통계
	@RequestMapping(value = "realestateStatistics.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateStatistics(HttpServletRequest request, ModelAndView mav) {
		log.info("realestateStatistics.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("realestate/realestateStatistics");
		return mav;
	}

	// 부동산 임대차 계약서 통계 조회 액션
	@RequestMapping(value = "realestateStatisticsAjax.json")
	public ModelAndView realestateStatisticsAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("realestateStatisticsAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String searchType = StringUtil.reqNullCheck(request, "searchType");
				String standardDate = StringUtil.reqNullCheck(request, "standardDate");

				if ("".equals(searchType) || "".equals(standardDate)) {
					mav.addObject("resultCode", CustomExceptionCodes.MISSING_PARAMETER.getId());
					mav.addObject("resultMsg", CustomExceptionCodes.MISSING_PARAMETER.getMsg());
				} else {
					if (searchType.equals("01")) {
						ContractInfo param = new ContractInfo();
						param.setStandardDate(standardDate);
						param.setDivision("사무실용");
						List<ContractInfo> officeList = realestateService.getContractStatisticsList(param);
						param.setDivision("개인용");
						List<ContractInfo> personList = realestateService.getContractStatisticsList(param);
						List<ContractInfo> totalList = realestateService.getTotalContractStatisticsList(param);

						mav.addObject("officeList", officeList);
						mav.addObject("personList", personList);
						mav.addObject("totalList", totalList);
					} else if (searchType.equals("02")) {
						ContractInfo param = new ContractInfo();
						param.setStandardDate(standardDate);
						param.setPayment("월세");
						List<ContractInfo> monthlyList = realestateService.getContractStatisticsList(param);
						param.setPayment("연납");
						List<ContractInfo> yearlyList = realestateService.getContractStatisticsList(param);
						param.setPayment("전세");
						List<ContractInfo> rentList = realestateService.getContractStatisticsList(param);
						param.setPayment("보유");
						List<ContractInfo> possessionList = realestateService.getContractStatisticsList(param);
						List<ContractInfo> totalList = realestateService.getTotalContractStatisticsList(param);

						mav.addObject("monthlyList", monthlyList);
						mav.addObject("yearlyList", yearlyList);
						mav.addObject("rentList", rentList);
						mav.addObject("possessionList", possessionList);
						mav.addObject("totalList", totalList);
					}

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

	// 부동산 계약관리 통계 엑셀 다운로드
	@RequestMapping(value = "realestateExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView realestateExcelDownload(HttpServletRequest request) {
		log.info("realestateExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("contractExcelView");

		String auth = StringUtil.reqNullCheck(request, "auth");
		String searchType = StringUtil.reqNullCheck(request, "searchType");
		String standardDate = StringUtil.reqNullCheck(request, "standardDate");

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		if ("".equals(searchType) || "".equals(standardDate)) {
			log.error("searchType : " + searchType + " , standardDate : " + standardDate);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		// 파일이름, 시트이름
		String fileName = "";
		String sheetName = standardDate;
		// 컬럼 데이터
		List<String> listColumn = new ArrayList<String>();
		listColumn.add("구분");
		listColumn.add("사용자");
		listColumn.add("소재지");
		listColumn.add("지급구분");
		listColumn.add("보증금");
		listColumn.add("임대료");
		listColumn.add("관리비");
		listColumn.add("계");

		if (searchType.equals("01")) {
			// 다운로드 파일 이름
			fileName = "용도별_부동산_계약_통계";

			// 내용 조회
			ContractInfo param = new ContractInfo();
			param.setStandardDate(standardDate);
			param.setDivision("사무실용");
			List<ContractInfo> officeList = realestateService.getContractStatisticsList(param);
			param.setDivision("개인용");
			List<ContractInfo> personList = realestateService.getContractStatisticsList(param);
			List<ContractInfo> totalList = realestateService.getTotalContractStatisticsList(param);

			mav.addObject("officeList", officeList);
			mav.addObject("personList", personList);
			mav.addObject("totalList", totalList);
		} else if (searchType.equals("02")) {
			// 다운로드 파일 이름
			fileName = "지급구분별_부동산_계약_통계";

			// 내용 조회
			ContractInfo param = new ContractInfo();
			param.setStandardDate(standardDate);
			param.setPayment("월세");
			List<ContractInfo> monthlyList = realestateService.getContractStatisticsList(param);
			param.setPayment("연납");
			List<ContractInfo> yearlyList = realestateService.getContractStatisticsList(param);
			param.setPayment("전세");
			List<ContractInfo> rentList = realestateService.getContractStatisticsList(param);
			param.setPayment("보유");
			List<ContractInfo> possessionList = realestateService.getContractStatisticsList(param);
			List<ContractInfo> totalList = realestateService.getTotalContractStatisticsList(param);

			mav.addObject("monthlyList", monthlyList);
			mav.addObject("yearlyList", yearlyList);
			mav.addObject("rentList", rentList);
			mav.addObject("possessionList", possessionList);
			mav.addObject("totalList", totalList);
		}

		mav.addObject("excelType", searchType);
		mav.addObject("fileName", fileName);
		mav.addObject("listColumn", listColumn);
		mav.addObject("sheetName", sheetName);
		return mav;
	}

}
