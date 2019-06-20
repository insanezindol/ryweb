package kr.co.reyonpharm.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import kr.co.reyonpharm.models.SettlementInfo;
import kr.co.reyonpharm.service.SettlementService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.SpringSecurityUtil;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/settlement/")
public class SettlementController extends CustomExceptionHandler {

	@Autowired
	SettlementService settlementService;

	/* 연말정산 관리 */

	// 기초데이터 다운로드
	@RequestMapping(value = "downloadMasterData.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadMasterData(HttpServletRequest request, ModelAndView mav) {
		log.info("downloadMasterData.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("settlement/downloadMasterData");
		return mav;
	}
	
	// 기초데이터 엑셀 다운로드
	@RequestMapping(value = "downloadExcelMasterData.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadExcelMasterData(HttpServletRequest request) {
		log.info("downloadExcelMasterData.do");

		ModelAndView mav = new ModelAndView("masterDataExcelView");
		String auth = StringUtil.reqNullCheck(request, "auth");
		String yymm = StringUtil.reqNullCheck(request, "yymm");
		String saup = StringUtil.reqNullCheck(request, "saup");
		String gubun = StringUtil.reqNullCheck(request, "gubun");

		if (!auth.equals("reyon") || yymm.equals("") || saup.equals("") || gubun.equals("")) {
			log.error("auth : " + auth + ", yymm : " + yymm + ", saup : " + saup + ", gubun : " + gubun);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}

		List<SettlementInfo> list = new ArrayList<SettlementInfo>();
		List<String> listColumn = new ArrayList<String>();
		
		SettlementInfo param = new SettlementInfo();
		param.setYymm(yymm);
		param.setYymmNextYear(String.valueOf(Integer.parseInt(yymm)+1));
		param.setSaup(saup);
		param.setGubun(gubun);
		
		StringBuffer fileName = new StringBuffer(yymm); 
		if(saup.equals("10")) {
			fileName.append("_본사");
		} else if(saup.equals("20")) {
			fileName.append("_진천");
		}
		if(gubun.equals("master")) {
			fileName.append("_주현근무지");
			list = settlementService.getExcelMasterData(param);
			
			listColumn.add("성명");
			listColumn.add("주민등록번호");
			listColumn.add("시작일자");
			listColumn.add("종료일자");
			listColumn.add("총급여");
			listColumn.add("급여");
			listColumn.add("상여");
			listColumn.add("인정상여");
			listColumn.add("주식매수선택권행사이익");
			listColumn.add("우리사주조합인출금");
			listColumn.add("임원퇴직소득금액한도초과액");
			listColumn.add("직무발명보상금");
			listColumn.add("소득세");
			listColumn.add("지방소득세");
			listColumn.add("농어촌특별세");
			listColumn.add("국민연금보험료");
			listColumn.add("공무원연금");
			listColumn.add("군인연금");
			listColumn.add("사립학교직원연금");
			listColumn.add("별정우체국연금");
			listColumn.add("건강보험료(장기요양포함)");
			listColumn.add("고용보험료");
			listColumn.add("법정기부금");
			listColumn.add("종교단체지정기부금");
			listColumn.add("종교단체외지정기부금");
			listColumn.add("비과세(야간근로수당)");
		} else if(gubun.equals("slave")) {
			fileName.append("_종전근무지");
			list = settlementService.getExcelMasterExData(param);
			
			listColumn.add("사업자등록번호");
			listColumn.add("성명");
			listColumn.add("주민등록번호");
			listColumn.add("근무시작일자");
			listColumn.add("근무종료일자");
			listColumn.add("총급여");
			listColumn.add("급여");
			listColumn.add("상여");
			listColumn.add("인정상여");
			listColumn.add("주식매수선택권행사이익");
			listColumn.add("우리사주조합인출금");
			listColumn.add("임원퇴직소득금액");
			listColumn.add("직무발명보상금");
			listColumn.add("소득세");
			listColumn.add("지방소득세");
			listColumn.add("농어촌특별세");
			listColumn.add("국민연금보험료");
			listColumn.add("공무원연금");
			listColumn.add("군인연금");
			listColumn.add("사립학교직원연금");
			listColumn.add("별정우체국연금");
			listColumn.add("건강보험료");
			listColumn.add("고용보험료");
			listColumn.add("비과세항목(항목선택필요)");
			listColumn.add("감면시작일자");
			listColumn.add("감면종료일자");
			listColumn.add("중소기업감면(100%)");
			listColumn.add("중소기업감면(50%)");
			listColumn.add("중소기업감면(70%)");
			listColumn.add("중소기업감면(90%)");
		}
		
		mav.addObject("gubun", gubun);
		mav.addObject("fileName", fileName.toString());
		mav.addObject("listColumn", listColumn);
		mav.addObject("list", list);
		return mav;
	}
	
	// 공제신고서 업로드
	@RequestMapping(value = "uploadDeclaration.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView uploadDeclaration(HttpServletRequest request, ModelAndView mav) {
		log.info("uploadDeclaration.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("settlement/uploadDeclaration");
		return mav;
	}
	
	// 공제신고서 파일 리스트
	@RequestMapping(value = "getDeclarationListAjax.json")
	public ModelAndView getDeclarationListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getDeclarationListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			if (auth.equals("reyon")) {
				String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + status;
				File folder = new File(filePath);
				if(!folder.exists()) {
					folder.mkdirs();
					folder = new File(filePath);
				}
				File[] listOfFiles = folder.listFiles();
				List<SettlementInfo> list = new ArrayList<SettlementInfo>();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						String fileName = file.getName();
						String fileByte = String.valueOf(Math.ceil(file.length()/1024));
						long modified = file.lastModified();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date date = new Date(modified);
						String fileTime = format.format(date);
						
						SettlementInfo info = new SettlementInfo();
						info.setFileName(fileName);
						info.setFileByte(fileByte);
						info.setFileTime(fileTime);
						list.add(info);
					}
				}
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 공제신고서 파일 다운로드
	@RequestMapping(value = "declarationDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView declarationDownload(HttpServletRequest request) {
		log.info("declarationDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("downloadView");

		String auth = StringUtil.reqNullCheck(request, "auth");
		String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
		String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
		String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		if (yymm.equals("") || fileName.equals("")) {
			log.error("yymm : " + yymm + " , fileName : " +fileName);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}
		
		String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + status + File.separator + fileName;

		mav.addObject("filePath", filePath);
		return mav;
	}
	
	// 공제신고서 파일 삭제
	@RequestMapping(value = "declarationDeleteAjax.json")
	public ModelAndView declarationDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("declarationDeleteAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");
			
			if (auth.equals("reyon")) {
				String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + status + File.separator + fileName;
				File file = new File(filePath);
				if(file.exists()) {
					if(file.delete()){
		                mav.addObject("resultCode", 0);
						mav.addObject("resultMsg", "success");
		            }else{
		            	mav.addObject("resultCode", 1);
		            	mav.addObject("resultMsg", "파일삭제 실패 (ERR01 - FAIL DELETE FILE)");
		            }
				} else {
					mav.addObject("resultCode", 1);
					mav.addObject("resultMsg", "파일삭제 실패 (ERR02 - NOT EXIST FILE)");
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
	
	// 공제신고서 파일 업로드
	@RequestMapping(value = "declarationUploadAjax.json")
	public ModelAndView declarationUploadAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("declarationUploadAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			if (auth.equals("reyon")) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
				if (fileNames.hasNext()) {
					MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
					String fileName = uploadfile.getOriginalFilename();

					String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + status + File.separator;

					File targetDir = new File(filePath);
					if (!targetDir.exists()) {
						targetDir.mkdirs();
					}

					log.info("PATH : " + filePath + fileName);
					File convFile = new File(filePath + fileName);
					uploadfile.transferTo(convFile);

					if (convFile.exists()) {
						log.info("SUCCESS - " + fileName);
						mav.addObject("resultCode", 0);
						mav.addObject("resultMsg", "업로드 성공");
					} else {
						log.error("ERROR - " + fileName);
						mav.addObject("resultCode", -1);
						mav.addObject("resultMsg", "업로드 실패");
					}
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

	// 공제신고서 분석
	@RequestMapping(value = "analyzeDeclaration.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView analyzeDeclaration(HttpServletRequest request, ModelAndView mav) {
		log.info("analyzeDeclaration.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("settlement/analyzeDeclaration");
		return mav;
	}
	
	// 공제신고서 DB 리스트
	@RequestMapping(value = "getDeclarationDBListAjax.json")
	public ModelAndView getDeclarationDBListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getDeclarationDBListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			if (auth.equals("reyon")) {
				SettlementInfo param = new SettlementInfo();
				param.setYymm(yymm);
				List<SettlementInfo> list = settlementService.getDeclarationDBList(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 공제신고서 분석 액션
	@RequestMapping(value = "analyzeDeclarationAjax.json")
	public ModelAndView analyzeDeclarationAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("analyzeDeclarationAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");
			if (auth.equals("reyon")) {
				SettlementInfo param = new SettlementInfo();
				param.setYymm(yymm);
				param.setFileName(fileName);
				Map<String, String> output = settlementService.analyzeDeclaration(param);
				mav.addObject("resultCode", output.get("resultCode"));
				mav.addObject("resultMsg", output.get("resultMsg"));
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
	
	
	// 지급명세서 업로드
	@RequestMapping(value = "uploadSpecification.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView uploadSpecification(HttpServletRequest request, ModelAndView mav) {
		log.info("uploadSpecification.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		mav.setViewName("settlement/uploadSpecification");
		return mav;
	}
	
	// 지급명세서 파일 리스트
	@RequestMapping(value = "getSpecificationListAjax.json")
	public ModelAndView getSpecificationListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getSpecificationListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			if (auth.equals("reyon")) {
				String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + status;
				File folder = new File(filePath);
				if(!folder.exists()) {
					folder.mkdirs();
					folder = new File(filePath);
				}
				File[] listOfFiles = folder.listFiles();
				List<SettlementInfo> list = new ArrayList<SettlementInfo>();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						String fileName = file.getName();
						String fileByte = String.valueOf(Math.ceil(file.length()/1024));
						long modified = file.lastModified();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date date = new Date(modified);
						String fileTime = format.format(date);
						
						SettlementInfo info = new SettlementInfo();
						info.setFileName(fileName);
						info.setFileByte(fileByte);
						info.setFileTime(fileTime);
						list.add(info);
					}
				}
				
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 지급명세서 파일 다운로드
	@RequestMapping(value = "specificationDownload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView specificationDownload(HttpServletRequest request) {
		log.info("specificationDownload.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");

		ModelAndView mav = new ModelAndView("downloadView");

		String auth = StringUtil.reqNullCheck(request, "auth");
		String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
		String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
		String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");

		if (!auth.equals("reyon")) {
			log.error("auth : " + auth + " , " + CustomExceptionCodes.INVALID_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.INVALID_PARAMETER);
		}

		if (yymm.equals("") || fileName.equals("")) {
			log.error("yymm : " + yymm + " , fileName : " +fileName);
			log.error(CustomExceptionCodes.MISSING_PARAMETER.getMsg());
			throw new CustomException(CustomExceptionCodes.MISSING_PARAMETER);
		}
		
		String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + status + File.separator + fileName;

		mav.addObject("filePath", filePath);
		return mav;
	}
	
	// 지급명세서 파일 삭제
	@RequestMapping(value = "specificationDeleteAjax.json")
	public ModelAndView specificationDeleteAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("specificationDeleteAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");
			
			if (auth.equals("reyon")) {
				String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + status + File.separator + fileName;
				File file = new File(filePath);
				if(file.exists()) {
					if(file.delete()){
		                mav.addObject("resultCode", 0);
						mav.addObject("resultMsg", "success");
		            }else{
		            	mav.addObject("resultCode", 1);
		            	mav.addObject("resultMsg", "파일삭제 실패 (ERR01 - FAIL DELETE FILE)");
		            }
				} else {
					mav.addObject("resultCode", 1);
					mav.addObject("resultMsg", "파일삭제 실패 (ERR02 - NOT EXIST FILE)");
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
	
	// 지급명세서 파일 업로드
	@RequestMapping(value = "specificationUploadAjax.json")
	public ModelAndView specificationUploadAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("specificationUploadAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String status = StringUtil.reqNullCheckHangulUTF8(request, "status");
			if (auth.equals("reyon")) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
				if (fileNames.hasNext()) {
					MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());
					String fileName = uploadfile.getOriginalFilename();

					String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + status + File.separator;

					File targetDir = new File(filePath);
					if (!targetDir.exists()) {
						targetDir.mkdirs();
					}

					log.info("PATH : " + filePath + fileName);
					File convFile = new File(filePath + fileName);
					uploadfile.transferTo(convFile);

					if (convFile.exists()) {
						log.info("SUCCESS - " + fileName);
						mav.addObject("resultCode", 0);
						mav.addObject("resultMsg", "업로드 성공");
					} else {
						log.error("ERROR - " + fileName);
						mav.addObject("resultCode", -1);
						mav.addObject("resultMsg", "업로드 실패");
					}
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

	// 지급명세서 분석
	@RequestMapping(value = "analyzeSpecification.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView analyzeSpecification(HttpServletRequest request, ModelAndView mav) {
		log.info("analyzeSpecification.do");
		log.info("[accesslog] [" + request.getRequestURI() + "] [" + request.getQueryString() + "] [" + SpringSecurityUtil.getUsername() + "] [" + SpringSecurityUtil.getKname() + "]");
		mav.setViewName("settlement/analyzeSpecification");
		return mav;
	}
	
	// 지급명세서 DB 리스트
	@RequestMapping(value = "getSpecificationDBListAjax.json")
	public ModelAndView getSpecificationDBListAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("getSpecificationDBListAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			if (auth.equals("reyon")) {
				SettlementInfo param = new SettlementInfo();
				param.setYymm(yymm);
				List<SettlementInfo> list = settlementService.getSpecificationDBList(param);
				mav.addObject("resultCode", 0);
				mav.addObject("resultMsg", "success");
				mav.addObject("list", list);
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
	
	// 지급명세서 분석 액션
	@RequestMapping(value = "analyzeSpecificationAjax.json")
	public ModelAndView analyzeSpecificationAjax(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		log.info("analyzeSpecificationAjax.json");
		try {
			String auth = StringUtil.reqNullCheck(request, "auth");
			String yymm = StringUtil.reqNullCheckHangulUTF8(request, "yymm");
			String fileName = StringUtil.reqNullCheckHangulUTF8(request, "fileName");
			if (auth.equals("reyon")) {
				SettlementInfo param = new SettlementInfo();
				param.setYymm(yymm);
				param.setFileName(fileName);
				Map<String, String> output = settlementService.analyzeSpecification(param);
				mav.addObject("resultCode", output.get("resultCode"));
				mav.addObject("resultMsg", output.get("resultMsg"));
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
	
}
