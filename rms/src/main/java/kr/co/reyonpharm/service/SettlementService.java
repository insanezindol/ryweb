package kr.co.reyonpharm.service;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.epapyrus.api.ExportCustomFile;

import kr.co.reyonpharm.mapper.ryhr.SettlementMapper_Ryhr;
import kr.co.reyonpharm.models.NtsInfo;
import kr.co.reyonpharm.models.SettlementInfo;
import kr.co.reyonpharm.util.Constants;
import kr.co.reyonpharm.util.XMLCodeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("settlementService")
public class SettlementService {

	@Autowired
	private SettlementMapper_Ryhr settlementMapper_Ryhr;

	@Transactional(value = "ryhr_transactionManager")
	public List<SettlementInfo> getDeclarationDBList(SettlementInfo param) {
		return settlementMapper_Ryhr.selectDeclarationDBList(param);
	}
	
	public List<SettlementInfo> getExcelMasterData(SettlementInfo param) {
		return settlementMapper_Ryhr.selectExcelMasterData(param);
	}
	
	public List<SettlementInfo> getExcelMasterExData(SettlementInfo param) {
		return settlementMapper_Ryhr.selectExcelMasterExData(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public Map<String, String> analyzeDeclaration(SettlementInfo settlementInfo) {
		String resultCode = "-1";
		String resultMsg = "준비중 오류";
		
		String yymm = settlementInfo.getYymm();
		String fileName = settlementInfo.getFileName();
		
		String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + "ING" + File.separator + fileName;
		File file = new File(filePath);
		
		if (!file.exists()) {
			log.error("파일이 존재하지 않습니다.");
			resultCode = "-1";
			resultMsg = "파일이 존재하지 않습니다.";
		} else {
			if (!fileName.toUpperCase().endsWith(".PDF")) {
				log.error("PDF 파일이 아닙니다.");
				resultCode = "-1";
				resultMsg = "PDF 파일이 아닙니다.";
			} else {
				try {
					byte[] pdfBytes = Files.readAllBytes(file.toPath());
					
					ExportCustomFile pdf = new ExportCustomFile();
					
					// 데이터 추출
					byte[] buf = pdf.NTS_GetFileBufEx(pdfBytes, "", "XML", false);
					int v_ret = pdf.NTS_GetLastError();

					if (v_ret == 1) {
						String strXml = new String(buf, "UTF-8");
						
						Document doc = getDocument(strXml);
						List<String> totalFormCd = evaluateXPath(doc, "/yesone/form/@form_cd"); // 전체 FORM_CD

						log.info("========== 전체 구성 분석 시작 ==========");
						for (int i = 0; i < totalFormCd.size(); i++) {
							String formcd = totalFormCd.get(i);
							if (formcd.equals(XMLCodeUtil.CD_A101Y)) {
								// 공제신고서
								log.info("========== 공제신고서 시작 ==========");
								List<Map> mainList = singoseoMain(doc, formcd);
								List<Map> detailList = singoseoDetail(doc, formcd);
								String sabun = "";
								int orderSeq = 1;
								
								log.info("========== GET SABUN BEG ==========");
								for (int j = 0; j < mainList.size(); j++) {
									Map map = mainList.get(j);
									String resnoEncCntn = map.get("resnoEncCntn").toString();
									if(resnoEncCntn != null) {
										NtsInfo param = new NtsInfo();
										param.setJumin(resnoEncCntn);
										NtsInfo info = settlementMapper_Ryhr.selectUserInfo(param);
										if(info != null) {
											if(info.getSabun() != null) {
												sabun = info.getSabun();
											}
										}
									}
								}
								log.info("========== GET SABUN END ==========");
								
								if(sabun == null || sabun.equals("")) {
									log.error("[EXCEPTION] SABUN NOT FOUND");
									resultCode = "-1";
									resultMsg = "사번을 찾을 수 없습니다.";
								} else {
									try {
										log.info("========== CHECK EXIST BEG ==========");
										NtsInfo existParam = new NtsInfo();
										existParam.setYymm(yymm);
										existParam.setPayGb(XMLCodeUtil.PAYGB);
										existParam.setSabun(sabun);
										NtsInfo existInfo = settlementMapper_Ryhr.selectExistInfo(existParam);
										if(existInfo.getExistCnt() != 0) {
											int deleteCnt = settlementMapper_Ryhr.deleteExistInfo(existParam);
											log.info("[SUCCESS] DELETE (" + sabun + ") - " + deleteCnt);
										}
										log.info("========== CHECK EXIST END ==========");
										
										log.info("========== " + formcd + " MAIN BEG ==========");
										for (int j = 0; j < mainList.size(); j++) {
											Map map = mainList.get(j);
											for (Object key : map.keySet()) {
												NtsInfo param = new NtsInfo();
												param.setYymm(yymm);
												param.setPayGb(XMLCodeUtil.PAYGB);
												param.setSabun(sabun);
												param.setItemKey(key.toString());
												param.setItemData(map.get(key.toString()).toString());
												param.setOrderSeq(orderSeq++);
												int resultCnt = settlementMapper_Ryhr.insertNtsInfo(param);
											}
										}
										log.info("========== " + formcd + " MAIN END ==========");
										
										log.info("========== " + formcd + " DETAIL BEG ==========");
										for (int j = 0; j < detailList.size(); j++) {
											Map map = detailList.get(j);
											// XML ELEMENT 중복때문에 예외 처리
											String gbn = "";
											if(map.get("suptFmlyRltClCd") != null) {
												// 인적공제및소득세액공제명세별
												gbn = "B";
											} else if(map.get("tchIntdCtrYn") != null) {
												// 세액감면및공제별
												gbn = "F";
											} else if(map.get("brwOrgnBrwnHsngTennLnpbSrmNtsAmt") != null) {
												// 국세청,기타자료항목구분개별
												gbn = "G";
											} else {
												gbn = "";
											}
											
											for (Object key : map.keySet()) {
												NtsInfo param = new NtsInfo();
												param.setYymm(yymm);
												param.setPayGb(XMLCodeUtil.PAYGB);
												param.setSabun(sabun);
												param.setItemKey(key.toString());
												param.setItemData(map.get(key.toString()).toString());
												param.setOrderSeq(orderSeq++);
												param.setGbn(gbn);
												int resultCnt = settlementMapper_Ryhr.insertNtsInfo(param);
											}
										}
										log.info("========== " + formcd + " DETAIL END ==========");
										resultCode = "0";
										resultMsg = "성공";
									} catch (Exception e) {
										log.error("공제신고서 분석 실패 (" + e.toString() + ")");
										resultCode = "-1";
										resultMsg = "실패 (" + e.toString() + ")";
										NtsInfo deleteParam = new NtsInfo();
										deleteParam.setYymm(yymm);
										deleteParam.setPayGb(XMLCodeUtil.PAYGB);
										deleteParam.setSabun(sabun);
										int deleteCnt = settlementMapper_Ryhr.deleteExistInfo(deleteParam);
										log.info("[ROLLBACK SUCCESS] DELETE (" + sabun + ") - " + deleteCnt);
									}
								}
								log.info("========== 공제신고서 종료 ==========");
							} else if (formcd.equals(XMLCodeUtil.CD_B101Y)) {
								// 연금저축등 소득.세액 공제명세
							} else if (formcd.equals(XMLCodeUtil.CD_C101Y)) {
								// 월세액.거주자간 주택임차차입금 상환액
							} else if (formcd.equals(XMLCodeUtil.CD_D101Y)) {
								// 의료비 지급명세
							} else if (formcd.equals(XMLCodeUtil.CD_E101Y)) {
								// 기부금 명세
							} else if (formcd.equals(XMLCodeUtil.CD_F101Y)) {
								// 신용카드등 소득공제신청서
							}
						}
						log.info("========== 전체 구성 분석 종료 ==========");
					} else if (v_ret == 0) {
						log.error("연말정산간소화 표준 전자문서가 아닙니다.");
						resultCode = "-1";
						resultMsg = "연말정산간소화 표준 전자문서가 아닙니다.";
					} else if (v_ret == -1) {
						log.error("비밀번호가 틀립니다.");
						resultCode = "-1";
						resultMsg = "비밀번호가 틀립니다.";
					} else if (v_ret == -2) {
						log.error("PDF문서가 아니거나 손상된 문서입니다.");
						resultCode = "-1";
						resultMsg = "PDF문서가 아니거나 손상된 문서입니다.";
					} else {
						log.error("데이터 추출에 실패하였습니다.");
						resultCode = "-1";
						resultMsg = "데이터 추출에 실패하였습니다.";
					}
				} catch (Exception e) {
					log.error("데이터 추출 실패 (" + e.toString() + ")");
					resultCode = "-1";
					resultMsg = "실패 (" + e.toString() + ")";
				}
				
			}
		}
		
		// DB INSERT 성공한 경우 해당 파일을 완료 완료 폴더로 이동
		if (resultCode.equals("0")) {
			String filePathMove = Constants.configProp.getProperty(Constants.FILE_UPLOAD_DECLARATION_DIR) + yymm + File.separator + "END";
			File fileToMove = new File(filePathMove);
			if(!fileToMove.exists()) {
				fileToMove.mkdirs();
			}
			
			File oldFile = new File(filePathMove + File.separator + fileName);
			if(oldFile.exists()) {
				oldFile.delete();
			}
			
			try {
				FileUtils.moveFileToDirectory(file, fileToMove,  true);
				log.info("[SUCCESS] MOVE FILE TO END FOLDER");
			} catch (Exception e) {
				log.error("[FAIL] MOVE FILE TO END FOLDER - " + e.toString());
			}
		}
		
		Map<String, String> output = new Hashtable<String, String>();
		output.put("resultCode", resultCode);
		output.put("resultMsg", resultMsg);
		return output;
	}
	
	// Document build and parse
	private Document getDocument(String strXml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(strXml));
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}
	
	// Custom Xpath Parser
	private List<String> evaluateXPath(Document document, String xpathExpression) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		List<String> values = new ArrayList<>();
		try {
			if(xpathExpression.contains("/text()")) {
				xpathExpression = xpathExpression.replace("/text()", "");
				XPathExpression expr = xpath.compile(xpathExpression);
				NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					if (nodes.item(i).getFirstChild() == null) {
						values.add(null);
					} else {
						values.add(nodes.item(i).getFirstChild().getNodeValue());
					}
				}
			} else {
				XPathExpression expr = xpath.compile(xpathExpression);
				NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					values.add(nodes.item(i).getNodeValue());
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return values;
	}
	
	// 신고서 main parser
	private List<Map> singoseoMain(Document doc, String formcd) {
		List<Map> list = new ArrayList<Map>();
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/yesone/form[@form_cd='" + formcd + "']/man");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element element = (Element) node;
				NamedNodeMap attributes = element.getAttributes();
				int numAttrs = attributes.getLength();
				Map map = new Hashtable();
				for (int j = 0; j < numAttrs; j++) {
					Attr attr = (Attr) attributes.item(j);
					String attrName = attr.getNodeName();
					String attrValue = attr.getNodeValue();
					map.put(attrName, attrValue);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 신고서 detail parser
	private List<Map> singoseoDetail(Document doc, String formcd) {
		List<Map> list = new ArrayList<Map>();
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/yesone/form[@form_cd='" + formcd + "']/man/data");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element element = (Element) node;
				NamedNodeMap attributes = element.getAttributes();
				int numAttrs = attributes.getLength();
				Map map = new Hashtable();
				for (int j = 0; j < numAttrs; j++) {
					Attr attr = (Attr) attributes.item(j);
					String attrName = attr.getNodeName();
					String attrValue = attr.getNodeValue();
					map.put(attrName, attrValue);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Transactional(value = "ryhr_transactionManager")
	public List<SettlementInfo> getSpecificationDBList(SettlementInfo param) {
		return settlementMapper_Ryhr.selectSpecificationDBList(param);
	}

	@Transactional(value = "ryhr_transactionManager")
	public Map<String, String> analyzeSpecification(SettlementInfo settlementInfo) {
		String resultCode = "-1";
		String resultMsg = "준비중 오류";
		
		String yymm = settlementInfo.getYymm();
		String fileName = settlementInfo.getFileName();
		
		String filePath = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + "ING" + File.separator + fileName;
		File file = new File(filePath);
		
		if (!file.exists()) {
			log.error("파일이 존재하지 않습니다.");
			resultCode = "-1";
			resultMsg = "파일이 존재하지 않습니다.";
		} else {
			try {
				// 원천징수영수증, 지급명세서 분석 로직
				boolean parseResult1 = false;
				boolean parseResult2 = false;
				boolean parseResult3 = false;
				
				PDDocument doc = PDDocument.load(file);
				PDFTextStripper textStripper = new PDFTextStripper();
				StringWriter textWriter = new StringWriter();
				textStripper.writeText(doc, textWriter);
				doc.close();
				
				String result = textWriter.toString();
				String[] resultArray = result.split("\\n");
				
				NtsInfo param = new NtsInfo();
				param.setYymm(yymm);
				for (int i = 0; i < resultArray.length; i++) {
					String pdfTmp = resultArray[i];
					if(pdfTmp.contains("주민등록번호(외국인등록번호)")) {
						String[] infoArray = pdfTmp.split(" ");
						String kname = infoArray[13].replace("\n", "").replace("\r", "");
						String userNum = infoArray[17].replace("\n", "").replace("\r", "");
						param.setKname(kname);
						param.setUserNum(userNum);
						parseResult1 = true;
					} else if(pdfTmp.contains("결    정    세    액72")) {
						String[] gjsaArray = pdfTmp.split(" ");
						String deductedTaxIncome = gjsaArray[16].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						String determinedTaxLocalIncome = gjsaArray[17].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						String determinedTaxSpecialRural = gjsaArray[18].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						param.setDeterminedTaxIncome(deductedTaxIncome);
						param.setDeterminedTaxLocalIncome(determinedTaxLocalIncome);
						param.setDeterminedTaxSpecialRural(determinedTaxSpecialRural);
						parseResult2 = true;
					} else if(pdfTmp.contains("차 감 징 수 세 액( - - - )76 72 73 74 75")) {
						String[] cgjssaArray = pdfTmp.split(" ");
						String deductedTaxIncome = cgjssaArray[17].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						String deductedTaxLocalIncome = cgjssaArray[18].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						String deductedTax_SpecialRural = cgjssaArray[19].replace("\n", "").replace("\r", "").replaceAll("\\,","");
						param.setDeductedTaxIncome(deductedTaxIncome);
						param.setDeductedTaxLocalIncome(deductedTaxLocalIncome);
						param.setDeductedTax_SpecialRural(deductedTax_SpecialRural);
						parseResult3 = true;
					}
				}
				
				log.info("========== GET SABUN BEG ==========");
				String sabun = "";
				NtsInfo juminParam = new NtsInfo();
				juminParam.setJumin(param.getUserNum());
				NtsInfo info = settlementMapper_Ryhr.selectUserInfo(juminParam);
				if(info != null) {
					if(info.getSabun() != null) {
						sabun = info.getSabun();
					}
				}
				log.info("========== GET SABUN END ==========");
				
				if(sabun != "") {
					param.setSabun(sabun);
					if (parseResult1 && parseResult2 && parseResult3) {
						int resultCnt = settlementMapper_Ryhr.insertNtsResultInfo(param);
						if (resultCnt == 1) {
							resultCode = "0";
							resultMsg = "성공";
						} else {
							log.error("데이터 INSERT 실패 : " + resultCnt);
							resultCode = "-1";
							resultMsg = "데이터 INSERT 실패";
						}

					} else {
						log.error("데이터 추출 실패 : " + parseResult1 + " , " + parseResult2 + " , " + parseResult3);
						resultCode = "-1";
						resultMsg = "데이터 추출 실패";
					}
				} else {
					log.error("[EXCEPTION] SABUN NOT FOUND");
					resultCode = "-1";
					resultMsg = "사번을 찾을 수 없습니다.";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
				resultCode = "-1";
				resultMsg = e.toString();
			}
		}
		
		// DB INSERT 성공한 경우 해당 파일을 완료 완료 폴더로 이동
		if (resultCode.equals("0")) {
			String filePathMove = Constants.configProp.getProperty(Constants.FILE_UPLOAD_SPECIFICATION_DIR) + yymm + File.separator + "END";
			File fileToMove = new File(filePathMove);
			if(!fileToMove.exists()) {
				fileToMove.mkdirs();
			}
			
			File oldFile = new File(filePathMove + File.separator + fileName);
			if(oldFile.exists()) {
				oldFile.delete();
			}
			
			try {
				FileUtils.moveFileToDirectory(file, fileToMove,  true);
				log.info("[SUCCESS] MOVE FILE TO END FOLDER");
			} catch (Exception e) {
				log.error("[FAIL] MOVE FILE TO END FOLDER - " + e.toString());
			}
		}
		
		Map<String, String> output = new Hashtable<String, String>();
		output.put("resultCode", resultCode);
		output.put("resultMsg", resultMsg);
		return output;
	}

}
