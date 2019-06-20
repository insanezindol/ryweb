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
import kr.co.reyonpharm.models.PageParam;
import kr.co.reyonpharm.models.VehicleInfo;
import kr.co.reyonpharm.service.CommonService;
import kr.co.reyonpharm.service.VehicleService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.FileIOUtils;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/vehicle/")
public class VehicleController extends CustomExceptionHandler {

	@Autowired
	VehicleService vehicleService;

	@Autowired
	CommonService commonService;

	/* 법인 차량 관리 */

	// 법인 차량 관리 리스트
	@RequestMapping(value = "vehicleList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleList(HttpServletRequest request, ModelAndView mav) {
		log.info("vehicleList.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		int pageNo = StringUtil.reqNullCheckIntVal(request, "pageNo");
		int pageSize = StringUtil.reqNullCheckIntVal(request, "pageSize");
		String s_divide = StringUtil.reqNullCheckHangulUTF8(request, "s_divide");
		String s_carnum = StringUtil.reqNullCheckHangulUTF8(request, "s_carnum");
		String s_cartype = StringUtil.reqNullCheckHangulUTF8(request, "s_cartype");
		String s_user = StringUtil.reqNullCheckHangulUTF8(request, "s_user");
		String s_paid = StringUtil.reqNullCheckHangulUTF8(request, "s_paid");
		String s_status = StringUtil.reqNullCheckHangulUTF8(request, "s_status");
		String queStr = StringUtil.nullCheck(request.getQueryString());

		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo == 0 ? 1 : pageNo);
		pageParam.setPageSize(pageSize == 0 ? 30 : pageSize);
		pageParam.setS_divide(s_divide);
		pageParam.setS_carnum(s_carnum);
		pageParam.setS_cartype(s_cartype);
		pageParam.setS_user(s_user);
		pageParam.setS_paid(s_paid);
		pageParam.setS_status(s_status == "" ? "ING" : s_status);

		try {
			pageParam.setQueStr(URLEncoder.encode(queStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int listCnt = vehicleService.getVehicleListCount(pageParam);
		pageParam.setTotalCount(listCnt);

		List<VehicleInfo> list = vehicleService.getVehicleList(pageParam);

		mav.addObject("pageParam", pageParam);
		mav.addObject("list", list);
		mav.setViewName("vehicle/vehicleList");
		return mav;
	}

	// 법인 차량 관리 상세보기
	@RequestMapping(value = "vehicleView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleView(HttpServletRequest request, ModelAndView mav) {
		log.info("vehicleView.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String vehicleSeq = StringUtil.reqNullCheck(request, "vehicleSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(vehicleSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		VehicleInfo param = new VehicleInfo();
		param.setVehicleSeq(vehicleSeq);
		VehicleInfo info = vehicleService.getVehicle(param);

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

		mav.addObject("vehicleSeq", vehicleSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.addObject("isPdf", isPdf);
		mav.setViewName("vehicle/vehicleView");
		return mav;
	}

	// 법인 차량 관리 추가
	@RequestMapping(value = "vehicleAdd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleAdd(HttpServletRequest request, ModelAndView mav) {
		log.info("vehicleAdd.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("vehicle/vehicleAdd");
		return mav;
	}

	// 법인 차량 관리 수정
	@RequestMapping(value = "vehicleModify.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleModify(HttpServletRequest request, ModelAndView mav) {
		log.info("vehicleModify.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String vehicleSeq = StringUtil.reqNullCheck(request, "vehicleSeq");
		String queStr = StringUtil.reqNullCheck(request, "queStr");

		if ("".equals(vehicleSeq)) {
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		VehicleInfo param = new VehicleInfo();
		param.setVehicleSeq(vehicleSeq);
		VehicleInfo info = vehicleService.getVehicle(param);

		if (null == info) {
			throw new CustomException(CustomExceptionCodes.INVALID_SEQUENCE);
		}

		mav.addObject("vehicleSeq", vehicleSeq);
		mav.addObject("queStr", queStr);
		mav.addObject("info", info);
		mav.setViewName("vehicle/vehicleModify");
		return mav;
	}

	// 법인 차량 관리 추가 액션
	@RequestMapping(value = "vehicleAddAjax.json")
	public ModelAndView vehicleAddAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("vehicleAddAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String vehicleNo = StringUtil.reqNullCheckHangulUTF8(request, "vehicleNo");
				String vehicleType = StringUtil.reqNullCheckHangulUTF8(request, "vehicleType");
				String division = StringUtil.reqNullCheckHangulUTF8(request, "division");
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String payment = StringUtil.reqNullCheckHangulUTF8(request, "payment");
				String rentStartDate = StringUtil.reqNullCheck(request, "rentStartDate");
				String rentEndDate = StringUtil.reqNullCheck(request, "rentEndDate");
				String rentMoney = StringUtil.reqNullCheck(request, "rentMoney");
				String insuranceStartDate = StringUtil.reqNullCheck(request, "insuranceStartDate");
				String insuranceEndDate = StringUtil.reqNullCheck(request, "insuranceEndDate");
				String insuranceMoney = StringUtil.reqNullCheck(request, "insuranceMoney");
				String status = StringUtil.reqNullCheck(request, "status");
				String remarks = StringUtil.reqNullCheckHangulUTF8(request, "remarks");

				// 로그인 정보
				String regSabun = SpringSecurityUtil.getUsername();
				String regName = SpringSecurityUtil.getKname();

				VehicleInfo param = new VehicleInfo();
				param.setVehicleNo(vehicleNo);
				param.setVehicleType(vehicleType);
				param.setDivision(division);
				param.setUsername(username);
				param.setPayment(payment);
				param.setRentStartDate(rentStartDate);
				param.setRentEndDate(rentEndDate);
				param.setRentMoney(rentMoney);
				param.setInsuranceStartDate(insuranceStartDate);
				param.setInsuranceEndDate(insuranceEndDate);
				param.setInsuranceMoney(insuranceMoney);
				param.setRemarks(remarks);
				param.setStatus(status);
				param.setRegSabun(regSabun);
				param.setRegName(regName);
				param.setUpdSabun(regSabun);
				param.setUpdName(regName);

				int resultCnt = vehicleService.addVehicle(param);

				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
				if (fileNames.hasNext()) {
					String vehicleSeq = String.valueOf(param.getCurrseq());

					MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
					String fileName = uploadfile.getOriginalFilename();

					String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_VEHICLE_DIR);
					StringBuffer fileFullPath = new StringBuffer(filePath);
					fileFullPath.append(vehicleSeq);
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

					VehicleInfo fileParam = new VehicleInfo();
					fileParam.setVehicleSeq(vehicleSeq);
					fileParam.setAttachFilepath(fileFullPath.toString());
					fileParam.setAttachFilename(fileName);
					fileParam.setAttachFilesize(String.valueOf(attachFilesize));
					int resultFileCnt = vehicleService.modifyFileVehicle(fileParam);
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

	// 법인 차량 관리 수정 액션
	@RequestMapping(value = "vehicleModifyAjax.json")
	public ModelAndView vehicleModifyAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("vehicleModifyAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String vehicleSeq = StringUtil.reqNullCheck(request, "vehicleSeq");
				String vehicleNo = StringUtil.reqNullCheckHangulUTF8(request, "vehicleNo");
				String vehicleType = StringUtil.reqNullCheckHangulUTF8(request, "vehicleType");
				String division = StringUtil.reqNullCheckHangulUTF8(request, "division");
				String username = StringUtil.reqNullCheckHangulUTF8(request, "username");
				String payment = StringUtil.reqNullCheckHangulUTF8(request, "payment");
				String rentStartDate = StringUtil.reqNullCheck(request, "rentStartDate");
				String rentEndDate = StringUtil.reqNullCheck(request, "rentEndDate");
				String rentMoney = StringUtil.reqNullCheck(request, "rentMoney");
				String insuranceStartDate = StringUtil.reqNullCheck(request, "insuranceStartDate");
				String insuranceEndDate = StringUtil.reqNullCheck(request, "insuranceEndDate");
				String insuranceMoney = StringUtil.reqNullCheck(request, "insuranceMoney");
				String status = StringUtil.reqNullCheck(request, "status");
				String remarks = StringUtil.reqNullCheckHangulUTF8(request, "remarks");
				String attFileType = StringUtil.reqNullCheck(request, "attFileType");

				// 로그인 정보
				String updSabun = SpringSecurityUtil.getUsername();
				String updName = SpringSecurityUtil.getKname();

				VehicleInfo fileData = new VehicleInfo();
				fileData.setVehicleSeq(vehicleSeq);
				VehicleInfo info = vehicleService.getVehicle(fileData);

				VehicleInfo param = new VehicleInfo();
				param.setVehicleSeq(vehicleSeq);
				param.setVehicleNo(vehicleNo);
				param.setVehicleType(vehicleType);
				param.setDivision(division);
				param.setUsername(username);
				param.setPayment(payment);
				param.setRentStartDate(rentStartDate);
				param.setRentEndDate(rentEndDate);
				param.setRentMoney(rentMoney);
				param.setInsuranceStartDate(insuranceStartDate);
				param.setInsuranceEndDate(insuranceEndDate);
				param.setInsuranceMoney(insuranceMoney);
				param.setRemarks(remarks);
				param.setStatus(status);
				param.setUpdSabun(updSabun);
				param.setUpdName(updName);

				int resultCnt = vehicleService.modifyVehicle(param);

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

						String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_VEHICLE_DIR);
						StringBuffer fileFullPath = new StringBuffer(filePath);
						fileFullPath.append(vehicleSeq);
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

						VehicleInfo fileParam = new VehicleInfo();
						fileParam.setVehicleSeq(vehicleSeq);
						fileParam.setAttachFilepath(fileFullPath.toString());
						fileParam.setAttachFilename(fileName);
						fileParam.setAttachFilesize(String.valueOf(attachFilesize));
						int resultFileCnt = vehicleService.modifyFileVehicle(fileParam);
						log.info("FILE UPDATE CNT : " + resultFileCnt);
					}
				} else if (attFileType.equals("03")) {
					// 현재 첨부파일 삭제
					if (info.getAttachFilepath() != null) {
						FileIOUtils.deleteAllFiles(info.getAttachFilepath());
					}

					VehicleInfo fileParam = new VehicleInfo();
					fileParam.setVehicleSeq(vehicleSeq);
					int resultFileCnt = vehicleService.modifyFileVehicle(fileParam);
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

	// 법인 차량 관리 삭제 액션
	@RequestMapping(value = "vehicleDeleteAjax.json")
	public ModelAndView vehicleDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("vehicleDeleteAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			if (auth.equals("reyon")) {
				String vehicleSeq = StringUtil.reqNullCheck(request, "vehicleSeq");
				String status = StringUtil.reqNullCheck(request, "status");

				// 로그인 정보
				String updSabun = SpringSecurityUtil.getUsername();
				String updName = SpringSecurityUtil.getKname();

				VehicleInfo param = new VehicleInfo();
				param.setVehicleSeq(vehicleSeq);
				param.setStatus(status);
				param.setUpdSabun(updSabun);
				param.setUpdName(updName);

				int resultCnt = vehicleService.deleteVehicle(param);

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
	@RequestMapping(value = "vehicleFileDownload.do")
	public ModelAndView vehicleFileDownload(HttpServletRequest request) {
		log.info("/vehicleFileDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		String auth = StringUtil.reqNullCheck(request, "dwAuth");
		String seq = StringUtil.reqNullCheck(request, "dwSeq");
		String filename = StringUtil.reqNullCheckHangulUTF8(request, "dwFilename");

		ModelAndView mav = new ModelAndView("downloadView");

		if (auth.equals("reyon")) {
			VehicleInfo param = new VehicleInfo();
			param.setVehicleSeq(seq);
			VehicleInfo info = vehicleService.getVehicle(param);

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

	// 법인 차량 관리 통계
	@RequestMapping(value = "vehicleStatistics.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleStatistics(HttpServletRequest request, ModelAndView mav) {
		log.info("vehicleStatistics.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("vehicle/vehicleStatistics");
		return mav;
	}

	// 법인 차량 관리 통계 조회 액션
	@RequestMapping(value = "vehicleStatisticsAjax.json")
	public ModelAndView vehicleStatisticsAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("vehicleStatisticsAjax.json");
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
						VehicleInfo param = new VehicleInfo();
						param.setStandardDate(standardDate);
						List<VehicleInfo> list = vehicleService.getVehicleStatisticsList(param);
						mav.addObject("list", list);
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

	// 법인 차량 관리 통계 엑셀 다운로드
	@RequestMapping(value = "vehicleExcelDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView vehicleExcelDownload(HttpServletRequest request) {
		log.info("vehicleExcelDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("vehicleExcelView");

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
		listColumn.add("No.");
		listColumn.add("차량종류");
		listColumn.add("차량번호");
		listColumn.add("사용자");
		listColumn.add("지급구분");
		listColumn.add("보험기간");
		listColumn.add("보험금액");
		listColumn.add("임대기간/매입일");
		listColumn.add("임차료/월\n(보험료포함)");

		if (searchType.equals("01")) {
			// 다운로드 파일 이름
			fileName = "법인차량현황";

			VehicleInfo param = new VehicleInfo();
			param.setStandardDate(standardDate);
			List<VehicleInfo> list = vehicleService.getVehicleStatisticsList(param);
			mav.addObject("standardDate", standardDate);
			mav.addObject("list", list);
		}

		mav.addObject("excelType", searchType);
		mav.addObject("fileName", fileName);
		mav.addObject("listColumn", listColumn);
		mav.addObject("sheetName", sheetName);
		return mav;
	}

}
