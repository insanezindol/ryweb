package kr.co.reyonpharm.controller;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.dreamsecurity.exception.DVException;
import com.dreamsecurity.verify.DSTSPDFSig;
import com.epapyrus.api.ExportCustomFile;

import kr.co.reyonpharm.service.NtsService;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class NtsController {
	
	@Autowired
	NtsService ntsService;
	
	// home 접속
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView home(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("home");
		return mav;
	}
	
	// 업로드 페이지
	@RequestMapping(value = "upload.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView upload(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("upload");
		return mav;
	}
	
	// 업로드 페이지
	@RequestMapping(value = "getData.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getData(HttpServletRequest request, ModelAndView mav) {
		String resultCode = "ERR01";
		String resultMsg = "알수없는 에러";
		
		String p_pwd = StringUtil.reqNullCheckHangulUTF8(request, "p_pwd");  // 비밀번호
		String ntsType = StringUtil.reqNullCheckHangulUTF8(request, "ntsType");  // 업로드 타입
		String key = "XML";  //key
		
		String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DIR);
		
		File targetDir = new File(filePath);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			java.util.Iterator<String> fileNames = multipartRequest.getFileNames();

			if (fileNames.hasNext()) {
				MultipartFile uploadfile = multipartRequest.getFile(fileNames.next());

				/* 문서 비밀번호 가져오기(비밀번호 없는 문서일 경우 생략 가능. 이 때 p_pwd="") */
				// 방식1.사용자 입력 방식인 경우(폼필드 입력값 추출)
				/*if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equals("p_pwd")) {
						p_pwd = fileItem.getString();
						continue;
					}
				}*/
		
				// 방식2.파일명등의 정보를 기준으로 인사DB 등에서 가져오는 경우
				// 비밀번호를 가져오는 로직 구현
				/*
		            p_pwd = "1234567";  // 인사정보 등에서 가져오기
				 */

				// path 제외한 파일명만 취득
				String fileName = uploadfile.getOriginalFilename();

				// PDF파일이 아닌 경우 skip
				if (!fileName.toUpperCase().endsWith(".PDF")) {
					log.error("PDF 파일이 아닙니다.");
					resultCode = "ERR02";
					resultMsg = "PDF 파일이 아닙니다.";
				} else {
					File convFile = new File(targetDir + "\\" + fileName);
					uploadfile.transferTo(convFile);

					if (convFile.exists()) {
						log.info("업로드 완료 : " + targetDir + "\\" + fileName);
					}

					// 파일내용을 읽음
					byte[] pdfBytes = Files.readAllBytes(convFile.toPath());
					boolean isSuccess = false;

					/* [Step1] 전자문서 위변조 검증 */
					if (ntsType.equals("1")) {
						try {
							DSTSPDFSig dstsPdfsig = new DSTSPDFSig();
							dstsPdfsig.init(pdfBytes);
							dstsPdfsig.tokenParse();

							isSuccess = dstsPdfsig.tokenVerify();

							if (isSuccess) {
								log.info("검증 완료(진본)");
							} else {
								String msg = dstsPdfsig.getTstVerifyFailInfo();
								log.info(msg);
								resultCode = "ERR03";
								resultMsg = "검증 실패(진본 아님)";
							}
						} catch (DVException e) {
							log.error("에러 코드 : " + e.getLastError());
							log.error("에러 메시지 : " + e.getMessage());
							resultCode = "ERR04";
							resultMsg = "[" + e.getLastError() + "]" + e.getMessage();
						}
					} else {
						isSuccess = true;
					}

					if (ntsType.equals("3")) {
						// 원천징수영수증, 지급명세서 분석 로직
						boolean parseResult1 = false;
						boolean parseResult2 = false;
						boolean parseResult3 = false;
						PDDocument doc = PDDocument.load(convFile);
						PDFTextStripper textStripper = new PDFTextStripper();
						StringWriter textWriter = new StringWriter();
						textStripper.writeText(doc, textWriter);
						doc.close();
						
						String result = textWriter.toString();
						String[] resultArray = result.split("\\n");
						
						StringBuffer sb = new StringBuffer();
						
						for (int i = 0; i < resultArray.length; i++) {
							String pdfTmp = resultArray[i];
							if(pdfTmp.contains("주민등록번호(외국인등록번호)")) {
								String[] infoArray = pdfTmp.split(" ");
								String info1 = infoArray[13].replace("\n", "").replace("\r", "");
								String info2 = infoArray[17].replace("\n", "").replace("\r", "");
								sb.append("이름 : " + info1);
								sb.append("<br>주민등록번호 : " + info2);
								parseResult1 = true;
							} else if(pdfTmp.contains("결    정    세    액72")) {
								String[] gjsaArray = pdfTmp.split(" ");
								String gjsa1 = gjsaArray[16].replace("\n", "").replace("\r", "");
								String gjsa2 = gjsaArray[17].replace("\n", "").replace("\r", "");
								String gjsa3 = gjsaArray[18].replace("\n", "").replace("\r", "");
								sb.append("<br><br>결정세액(소득세) : " + gjsa1);
								sb.append("<br>결정세액(지방소득세) : " + gjsa2);
								sb.append("<br>결정세액(농어촌특별세) : " + gjsa3);
								parseResult2 = true;
							} else if(pdfTmp.contains("차 감 징 수 세 액( - - - )76 72 73 74 75")) {
								String[] cgjssaArray = pdfTmp.split(" ");
								String cgjssa1 = cgjssaArray[17].replace("\n", "").replace("\r", "");
								String cgjssa2 = cgjssaArray[18].replace("\n", "").replace("\r", "");
								String cgjssa3 = cgjssaArray[19].replace("\n", "").replace("\r", "");
								sb.append("<br><br>차감징수세액(소득세) : " + cgjssa1);
								sb.append("<br>차감징수세액(지방소득세) : " + cgjssa2);
								sb.append("<br>차감징수세액(농어촌특별세) : " + cgjssa3);
								parseResult3 = true;
							}
						}
						
						if(parseResult1 && parseResult2 && parseResult3) {
							resultCode = "SUC00";
							resultMsg = sb.toString();
						} else {
							log.error("데이터 추출에 실패하였습니다.");
							resultCode = "ERR11";
							resultMsg = "데이터 추출에 실패하였습니다.";
						}
						
					} else {
						/* [Step2] XML(or SAM) 데이터 추출 */
						try {
							if (isSuccess) {
								ExportCustomFile pdf = new ExportCustomFile();

								// 데이터 추출
								byte[] buf = pdf.NTS_GetFileBufEx(pdfBytes, p_pwd, key, false);
								int v_ret = pdf.NTS_GetLastError();

								if (v_ret == 1) {
									String strXml = new String(buf, "UTF-8");
									boolean isParse = ntsService.parseXML(strXml, ntsType);
									if (isParse) {
										resultCode = "SUC00";
										resultMsg = "성공";
									} else {
										resultCode = "ERR99";
										resultMsg = "정보관리팀으로 문의하세요.";
									}
								} else if (v_ret == 0) {
									log.error("연말정산간소화 표준 전자문서가 아닙니다.");
									resultCode = "ERR05";
									resultMsg = "연말정산간소화 표준 전자문서가 아닙니다.";
								} else if (v_ret == -1) {
									log.error("비밀번호가 틀립니다.");
									resultCode = "ERR06";
									resultMsg = "비밀번호가 틀립니다.";
								} else if (v_ret == -2) {
									log.error("PDF문서가 아니거나 손상된 문서입니다.");
									resultCode = "ERR07";
									resultMsg = "PDF문서가 아니거나 손상된 문서입니다.";
								} else {
									log.error("데이터 추출에 실패하였습니다.");
									resultCode = "ERR08";
									resultMsg = "데이터 추출에 실패하였습니다.";
								}
							}
						} catch (Exception e) {
							log.error("[Step2] 데이터 추출 실패(" + e.toString() + ")");
							resultCode = "ERR09";
							resultMsg = e.toString();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception" + e.getMessage());
			resultCode = "ERR10";
			resultMsg = e.getMessage();
		}
		
		mav.addObject("resultCode", resultCode);
		mav.addObject("resultMsg", resultMsg);
		mav.setViewName("getData");
		return mav;
	}
	
	// 파일 다운로드
	@RequestMapping(value = "sampleDownload.do")
	public ModelAndView sampleDownload(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("samplePdfDownload");
		return mav;
	}
	
}
