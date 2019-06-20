package kr.co.reyonpharm.service;

import java.io.StringReader;
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

import kr.co.reyonpharm.mapper.ryhr.NtsMapper_Ryhr;
import kr.co.reyonpharm.models.NtsInfo;
import kr.co.reyonpharm.util.NtsCodeUtil;
import kr.co.reyonpharm.util.XMLCodeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ntsService")
public class NtsService {
	
	private static String GLOBAL_YYMM = "201812";
	private static String GLOBAL_PAYGB = "9";
	
	@Autowired
	private NtsMapper_Ryhr ntsMapper_Ryhr;

	// XML Parse
	@Transactional(value = "ryhr_transactionManager")
	public boolean parseXML(String strXml, String ntsType) {
		try {
			Document doc = getDocument(strXml);
			if(ntsType.equals("1")) {
				parseFormCd(doc);
			} else {
				parseSingoseo(doc);
			}
			return true;
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
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

	// 전체 FORM_CD 구성 분석
	private void parseFormCd(Document doc) {
		List<String> totalFormCd = evaluateXPath(doc, "/yesone/form/@form_cd"); // 전체 FORM_CD

		log.info("========== 전체 구성 분석 시작 ==========");
		for (int i = 0; i < totalFormCd.size(); i++) {
			String formcd = totalFormCd.get(i);
			if (formcd.equals(NtsCodeUtil.CD_A102Y)) {
				// 보장성 보험, 장애인전용보장성보험
				parseA102Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_B101Y)) {
				// 의료비 분석
				parseB101Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_C101Y)) {
				log.error("[expired element] [" + formcd + "] - " + NtsCodeUtil.NM_C101Y);
			} else if (formcd.equals(NtsCodeUtil.CD_C102Y)) {
				// 교육비 분석
				parseC102Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_C202Y)) {
				// 직업훈련비 분석
				parseC202Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_C301Y)) {
				// 교복구입비 분석
				parseC301Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_C401Y)) {
				// 학자금대출상환액 분석
				parseC401Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_D101Y)) {
				// 개인연금저축
				parseD101Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_E102Y)) {
				// 연금저축
				parseE102Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_F102Y)) {
				// 퇴직연금
				parseF102Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_G107Y)) {
				// 신용카드
				parseG107Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_G207M)) {
				// 현금영수증
				parseG207M(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_G307Y)) {
				// 직불카드등
				parseG307Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_J101Y)) {
				// 주택임차차입금 원리금상환액
				parseJ101Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_J203Y)) {
				// 장기주택저당차입금 이자상환액
				parseJ203Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_J301Y)) {
				// 주택마련저축
				parseJ301Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_J401Y)) {
				log.error("[expired element] [" + formcd + "] - " + NtsCodeUtil.NM_J401Y);
			} else if (formcd.equals(NtsCodeUtil.CD_K101M)) {
				// 소기업소상공인 공제부금
				parseK101M(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_L102Y)) {
				// 기부금
				parseL102Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_N101Y)) {
				// 장기집합투자증권저축
				parseN101Y(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_M101Y)) {
				log.error("[expired element] [" + formcd + "] - " + NtsCodeUtil.NM_M101Y);
			} else if (formcd.equals(NtsCodeUtil.CD_O101M)) {
				// 건강보험료 분석
				parseO101M(doc, formcd);
			} else if (formcd.equals(NtsCodeUtil.CD_P101M)) {
				log.error("[expired element] [" + formcd + "] - " + NtsCodeUtil.NM_P101M);
			} else if (formcd.equals(NtsCodeUtil.CD_P102M)) {
				// 국민연금보험료 분석
				parseP102M(doc, formcd);
			} else {
				log.error("[" + formcd + "] - unknown form_cd");
			}
		}
		log.info("========== 전체 구성 분석 종료 ==========");
	}

	// 건강보험료 분석
	private void parseO101M(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST
		
		log.info("========== 건강보험료 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@hi_yrs";   // 건강보험연말정산금액 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ltrm_yrs"; // 장기요양연말정산금액 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@hi_ntf";   // 건강보험(보수월액)고지금액합계 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ltrm_ntf"; // 장기요양(보수월액)고지금액합계 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@hi_pmt";   // 건강보험(소득월액)납부금액합계 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ltrm_pmt"; // 장기요양(소득월액)납부금액합계 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";    // 건강보험료 총 합계 LIST
			
			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "]");
			}
		}
		log.info("========== 건강보험료 종료 ==========");
	}
	
	// 국민연금보험료 분석
	private void parseP102M(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST
		
		log.info("========== 국민연금보험료 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@sp_ntf"; // 직장가입자소급고지금액합계 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@spym";   // 추납보험료납부금액 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@jlc";    // 실업크레딧납부금액 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ntf";    // 직장가입자 고지금액 합계 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@pmt";    // 지역가입자 등 납부금액 합계 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";  // 총합계 LIST
			
			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "]");
			}
		}
		log.info("========== 국민연금보험료 종료 ==========");
	}
	
	// 보장성 보험, 장애인전용보장성보험 분석
	private void parseA102Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST
		
		log.info("========== 보장성 보험, 장애인전용보장성보험 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data01 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";              // 자료코드 LIST (G0001:보장성, G0002:장애인보장성)
			String data02 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";              // 사업자번호 LIST
			String data03 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";            // 상호 LIST
			String data04 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";              // 증권번호 LIST
			String data05 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/goods_nm/text()";      // 보험종류 LIST
			String data06 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu1_resid/text()";   // 주민등록번호_주피보험자 LIST
			String data07 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu1_nm/text()";      // 성명_주피보험자 LIST
			String data08 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_resid_1/text()"; // 주민등록번호_종피보험자_1 LIST
			String data09 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_nm_1/text()";    // 성명_종피보험자_1 LIST
			String data10 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_resid_2/text()"; // 주민등록번호_종피보험자_2 LIST
			String data11 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_nm_2/text()";    // 성명_종피보험자_2 LIST
			String data12 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_resid_3/text()"; // 주민등록번호_종피보험자_3 LIST
			String data13 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/insu2_nm_3/text()";    // 성명_종피보험자_3 LIST
			String data14 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";           // 납입금액계 LIST
			
			List<String> list01 = evaluateXPath(doc, data01);
			List<String> list02 = evaluateXPath(doc, data02);
			List<String> list03 = evaluateXPath(doc, data03);
			List<String> list04 = evaluateXPath(doc, data04);
			List<String> list05 = evaluateXPath(doc, data05);
			List<String> list06 = evaluateXPath(doc, data06);
			List<String> list07 = evaluateXPath(doc, data07);
			List<String> list08 = evaluateXPath(doc, data08);
			List<String> list09 = evaluateXPath(doc, data09);
			List<String> list10 = evaluateXPath(doc, data10);
			List<String> list11 = evaluateXPath(doc, data11);
			List<String> list12 = evaluateXPath(doc, data12);
			List<String> list13 = evaluateXPath(doc, data13);
			List<String> list14 = evaluateXPath(doc, data14);
			
			for (int j = 0; j < list01.size(); j++) {
				
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list01.get(j) + "] [" + NtsCodeUtil.GETA102YNM(list01.get(j)) + "] [" + list02.get(j) + "] [" + list03.get(j) + "] [" + list04.get(j) + "] [" + list05.get(j) + "] [" + list06.get(j) + "] [" + list07.get(j) + "] [" + list08.get(j) + "] [" + list09.get(j) + "] [" + list10.get(j) + "] [" + list11.get(j) + "] [" + list12.get(j) + "] [" + list13.get(j) + "] [" + list14.get(j) + "]");
			}
		}
		log.info("========== 보장성 보험, 장애인전용보장성보험 종료 ==========");
	}

	// 의료비 분석
	private void parseB101Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 의료비 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";    // 의료비코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";    // 의료비사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";  // 의료비상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()"; // 의료비금액 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + NtsCodeUtil.GETB101YNM(list1.get(j)) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "]");
			}
		}
		log.info("========== 의료비 종료 ==========");
	}
	
	// 교육비 분석
	private void parseC102Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 교육비 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";    // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";    // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";  // 학교명 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@edu_tp";    // 교육비종류 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@edu_cl";    // 교육비구분 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()"; // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + NtsCodeUtil.GETC102YNM1(list1.get(j)) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + NtsCodeUtil.GETC102YNM2(list4.get(j)) + "] [" + list5.get(j) + "] [" + NtsCodeUtil.GETC102YNM3(list5.get(j)) + "] [" + list6.get(j) + "]");
			}
		}
		log.info("========== 교육비 종료 ==========");
	}
	
	// 직업훈련비 분석
	private void parseC202Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 직업훈련비 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";           // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";           // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";         // 교육기관명 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/course_cd/text()";  // 과정코드 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/subject_nm/text()"; // 과정명 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";        // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "]");
			}
		}
		log.info("========== 직업훈련비 종료 ==========");
	}
	
	// 교복구입비 분석
	private void parseC301Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 교복구입비 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";    // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";    // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";  // 상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()"; // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "]");
			}
		}
		log.info("========== 교복구입비 종료 ==========");
	}

	// 학자금대출상환액 분석
	private void parseC401Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 학자금대출상환액 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";    // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";    // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";  // 기관명 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()"; // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "]");
			}
		}
		log.info("========== 학자금대출상환액 종료 ==========");
	}
	
	// 신용카드 분석
	private void parseG107Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 신용카드 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";       // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";       // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";     // 상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@use_place_cd"; // 종류 LIST (1: 일반, 2: 전통시장, 3: 대중교통)
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";    // 공제대상금액합계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + NtsCodeUtil.GETG107YNM(list4.get(j)) + "] [" + list5.get(j) + "]");
			}
		}
		log.info("========== 신용카드 종료 ==========");
	}
	
	// 직불카드등 분석
	private void parseG307Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 직불카드등 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";       // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";       // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";     // 상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@use_place_cd"; // 종류 LIST (1: 일반, 2: 전통시장, 3: 대중교통)
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";    // 공제대상금액합계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + NtsCodeUtil.GETG107YNM(list4.get(j)) + "] [" + list5.get(j) + "]");
			}
		}
		log.info("========== 직불카드등 종료 ==========");
	}
	
	// 현금영수증 분석
	private void parseG207M(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 현금영수증 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";       // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@use_place_cd"; // 종류 LIST (1: 일반, 2: 전통시장, 3: 대중교통)
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";    // 공제대상금액합계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + NtsCodeUtil.GETG107YNM(list2.get(j)) + "] [" + list3.get(j) + "]");
			}
		}
		log.info("========== 현금영수증 종료 ==========");
	}
	
	// 장기집합투자증권저축 분석
	private void parseN101Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 장기집합투자증권저축 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";                // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";                // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";              // 취급기관 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@secu_no";               // 계좌번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/fund_nm/text()";         // 펀드명 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/reg_dt/text()";          // 가입일자 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/com_cd/text()";          // 금융회사등 코드 LIST
			String data8 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";             // 연간합계액 LIST
			String data9 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/ddct_bs_ass_amt/text()"; // 소득공제대상금액 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			List<String> list8 = evaluateXPath(doc, data8);
			List<String> list9 = evaluateXPath(doc, data9);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "] [" + list8.get(j) + "] [" + list9.get(j) + "]");
			}
		}
		log.info("========== 장기집합투자증권저축 종료 ==========");
	}
	
	// 장기주택저당차입금 이자상환액 분석
	private void parseJ203Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 장기주택저당차입금 이자상환액 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data01 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";                   // 자료코드 LIST
			String data02 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";                   // 사업자번호 LIST
			String data03 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";                 // 취급기관 LIST
			String data04 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";                   // 계좌번호 LIST
			String data05 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/lend_kd/text()";            // 대출종류 LIST (1:무주택자의 중도금 대출, 2:기존주택구입, 3:주택분양권 대출)
			String data06 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/house_take_dt/text()";      // 주택취득일 LIST
			String data07 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/mort_setup_dt/text()";      // 저당권설정일 LIST
			String data08 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/start_dt/text()";           // 최초차입일 LIST
			String data09 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/end_dt/text()";             // 최종상환예정일 LIST
			String data10 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/repay_years/text()";        // 상환기간연수 LIST
			String data11 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/lend_goods_nm/text()";      // 상품명 LIST
			String data12 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/debt/text()";               // 차입금 LIST
			String data13 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/fixed_rate_debt/text()";    // 고정금리차입금 LIST
			String data14 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/not_defer_debt/text()";     // 비거치식상환차입금 LIST
			String data15 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/this_year_rede_amt/text()"; // 당해년 원금상환액 LIST
			String data16 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ddct";                 // 소득공제대상액 LIST
			String data17 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";                // 연간합계액 LIST

			List<String> list01 = evaluateXPath(doc, data01);
			List<String> list02 = evaluateXPath(doc, data02);
			List<String> list03 = evaluateXPath(doc, data03);
			List<String> list04 = evaluateXPath(doc, data04);
			List<String> list05 = evaluateXPath(doc, data05);
			List<String> list06 = evaluateXPath(doc, data06);
			List<String> list07 = evaluateXPath(doc, data07);
			List<String> list08 = evaluateXPath(doc, data08);
			List<String> list09 = evaluateXPath(doc, data09);
			List<String> list10 = evaluateXPath(doc, data10);
			List<String> list11 = evaluateXPath(doc, data11);
			List<String> list12 = evaluateXPath(doc, data12);
			List<String> list13 = evaluateXPath(doc, data13);
			List<String> list14 = evaluateXPath(doc, data14);
			List<String> list15 = evaluateXPath(doc, data15);
			List<String> list16 = evaluateXPath(doc, data16);
			List<String> list17 = evaluateXPath(doc, data17);
			
			for (int j = 0; j < list01.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list01.get(j) + "] [" + list02.get(j) + "] [" + list03.get(j) + "] [" + list04.get(j) + "] [" + list05.get(j) + "] [" + NtsCodeUtil.GETJ203YNM(list05.get(j)) + "] [" + list06.get(j) + "] [" + list07.get(j) + "] [" + list08.get(j) + "] [" + list09.get(j) + "] [" + list10.get(j) + "] [" + list11.get(j) + "] [" + list12.get(j) + "] [" + list13.get(j) + "] [" + list14.get(j) + "] [" + list15.get(j) + "] [" + list16.get(j) + "] [" + list17.get(j) + "]");
			}
		}
		log.info("========== 장기주택저당차입금 이자상환액 종료 ==========");
	}
	
	// 주택임차차입금 원리금상환액
	private void parseJ101Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 주택임차차입금 원리금상환액 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";         // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";         // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";       // 취급기관 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";         // 계좌번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/goods_nm/text()"; // 상품명 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/lend_dt/text()";  // 대출일 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";      // 상환액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "]");
			}
		}
		log.info("========== 주택임차차입금 원리금상환액 종료 ==========");
	}

	// 주택마련저축
	private void parseJ301Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 주택마련저축 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";            // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";            // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";          // 취급기관 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";            // 계좌번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/goods_nm/text()";    // 저축명 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/saving_gubn/text()"; // 저축구분 LIST (1:청약저축, 2:주택청약종합저축, 4:근로자주택마련저축)
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/reg_dt/text()";      // 가입일자 LIST
			String data8 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/com_cd/text()";      // 금융기관코드 LIST
			String data9 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";         // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			List<String> list8 = evaluateXPath(doc, data8);
			List<String> list9 = evaluateXPath(doc, data9);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + NtsCodeUtil.GETJ301YNM(list6.get(j)) + "] [" + list7.get(j) + "] [" + list8.get(j) + "] [" + list9.get(j) + "]");
			}
		}
		log.info("========== 주택마련저축 종료 ==========");
	}

	// 개인연금저축
	private void parseD101Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 개인연금저축 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";         // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";         // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";       // 취급기관 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";         // 계좌/증권번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/start_dt/text()"; // 계약시작일 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/end_dt/text()";   // 계약종료일 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/com_cd/text()";   // 금융기관코드 LIST
			String data8 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";      // 납입금액계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			List<String> list8 = evaluateXPath(doc, data8);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "] [" + list8.get(j) + "]");
			}
		}
		log.info("========== 개인연금저축 종료 ==========");
	}

	// 연금저축
	private void parseE102Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 연금저축 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";                // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";                // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";              // 상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";                // 계좌번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/com_cd/text()";          // 금융회사등 코드 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/ann_tot_amt/text()";     // 당해연도납입금액 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/tax_year_amt/text()";    // 당해연도인출금액 LIST
			String data8 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/ddct_bs_ass_amt/text()"; // 순납입금액 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			List<String> list8 = evaluateXPath(doc, data8);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "] [" + list8.get(j) + "]");
			}
		}
		log.info("========== 연금저축 종료 ==========");
	}

	// 퇴직연금
	private void parseF102Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 퇴직연금 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";                // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";                // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";              // 상호 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";                // 계좌번호 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/com_cd/text()";          // 금융회사등 코드 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/pension_cd/text()";      // 계좌유형 LIST (11:근로자퇴직급여보장법, 12:과학기술인공제)
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/ann_tot_amt/text()";     // 당해연도납입금액 LIST
			String data8 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/tax_year_amt/text()";    // 당해연도인출금액 LIST
			String data9 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/ddct_bs_ass_amt/text()"; // 순납입금액 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			List<String> list8 = evaluateXPath(doc, data8);
			List<String> list9 = evaluateXPath(doc, data9);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + NtsCodeUtil.GETF102YNM(list6.get(j)) + "] [" + list7.get(j) + "] [" + list8.get(j) + "] [" + list9.get(j) + "]");
			}
		}
		log.info("========== 퇴직연금 종료 ==========");
	}

	// 소기업소상공인 공제부금
	private void parseK101M(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 소기업소상공인 공제부금 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";           // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@acc_no";           // 공제계약번호/증서번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/start_dt/text()";   // 공제가입일자 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/end_dt/text()";     // 대상기간종료일/개정규칙 적용 신청여부 LIST
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/pay_method/text()"; // 납입방법 LIST (M:월납,Q:분기납)
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";        // 납입금액계 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/@ddct";         // 소득공제대상액 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + list5.get(j) + "] [" + NtsCodeUtil.GETK101MNM(list5.get(j)) + "] [" + list6.get(j) + "] [" + list7.get(j) + "]");
			}
		}
		log.info("========== 소기업소상공인 공제부금 종료 ==========");
	}

	// 기부금
	private void parseL102Y(Document doc, String formcd) {
		List<String> usercode = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@resid"); // 주민번호 LIST
		List<String> username = evaluateXPath(doc, "/yesone/form[@form_cd='" + formcd + "']/man/@name");  // 이름 LIST

		log.info("========== 기부금 시작 ==========");
		for (int i = 0; i < usercode.size(); i++) {
			String data1 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@dat_cd";              // 자료코드 LIST
			String data2 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@busnid";              // 사업자번호 LIST
			String data3 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@trade_nm";            // 단체명 LIST
			String data4 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/@donation_cd";         // 기부유형 LIST (10:법정기부금, 20:정치자금기부금, 40:지정기부금, 41:종교단체기부금, 42:우리사주기부금)
			String data5 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sum/text()";           // 공제대상기부금액 LIST
			String data6 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/sbdy_apln_sum/text()"; // 기부장려금신청금액 LIST
			String data7 = "/yesone/form[@form_cd='" + formcd + "']/man[@resid='" + usercode.get(i) + "']/data/conb_sum/text()";      // 기부금액합계 LIST

			List<String> list1 = evaluateXPath(doc, data1);
			List<String> list2 = evaluateXPath(doc, data2);
			List<String> list3 = evaluateXPath(doc, data3);
			List<String> list4 = evaluateXPath(doc, data4);
			List<String> list5 = evaluateXPath(doc, data5);
			List<String> list6 = evaluateXPath(doc, data6);
			List<String> list7 = evaluateXPath(doc, data7);
			
			for (int j = 0; j < list1.size(); j++) {
				log.info("[" + usercode.get(i) + "] [" + username.get(i) + "] [" + list1.get(j) + "] [" + list2.get(j) + "] [" + list3.get(j) + "] [" + list4.get(j) + "] [" + NtsCodeUtil.GETL102YNM(list4.get(j)) + "] [" + list5.get(j) + "] [" + list6.get(j) + "] [" + list7.get(j) + "]");
			}
		}
		log.info("========== 기부금 종료 ==========");
	}
	
	// 전체 FORM_CD 구성 분석 (공제신고서)
	@Transactional(value = "ryhr_transactionManager")
	private void parseSingoseo(Document doc) {
		List<String> totalFormCd = evaluateXPath(doc, "/yesone/form/@form_cd"); // 전체 FORM_CD

		log.info("========== 전체 구성 분석 시작 ==========");
		for (int i = 0; i < totalFormCd.size(); i++) {
			String formcd = totalFormCd.get(i);
			if (formcd.equals(XMLCodeUtil.CD_A101Y)) {
				// 공제신고서
				singoseoA101Y(doc, formcd);
			} else if (formcd.equals(XMLCodeUtil.CD_B101Y)) {
				// 연금저축등 소득.세액 공제명세
				//singoseoB101Y(doc, formcd);
			} else if (formcd.equals(XMLCodeUtil.CD_C101Y)) {
				// 월세액.거주자간 주택임차차입금 상환액
				//singoseoC101Y(doc, formcd);
			} else if (formcd.equals(XMLCodeUtil.CD_D101Y)) {
				// 의료비 지급명세
				//singoseoD101Y(doc, formcd);
			} else if (formcd.equals(XMLCodeUtil.CD_E101Y)) {
				// 기부금 명세
				//singoseoE101Y(doc, formcd);
			} else if (formcd.equals(XMLCodeUtil.CD_F101Y)) {
				// 신용카드등 소득공제신청서
				//singoseoF101Y(doc, formcd);
			}
		}
		log.info("========== 전체 구성 분석 종료 ==========");
	}
	
	// 공제신고서
	@Transactional(value = "ryhr_transactionManager")
	private void singoseoA101Y(Document doc, String formcd) {
		log.info("========== 공제신고서 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		String sabun = "";
		int orderSeq = 1;
		
		log.info("========== GET SABUN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			String resnoEncCntn = map.get("resnoEncCntn").toString();
			if(resnoEncCntn != null) {
				//resnoEncCntn = "9004031685226";
				NtsInfo param = new NtsInfo();
				param.setJumin(resnoEncCntn);
				NtsInfo info = ntsMapper_Ryhr.selectUserInfo(param);
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
		} else {
			log.info("========== CHECK EXIST BEG ==========");
			NtsInfo existParam = new NtsInfo();
			existParam.setYymm(GLOBAL_YYMM);
			existParam.setPayGb(GLOBAL_PAYGB);
			existParam.setSabun(sabun);
			NtsInfo existInfo = ntsMapper_Ryhr.selectExistInfo(existParam);
			if(existInfo.getExistCnt() != 0) {
				NtsInfo deleteParam = new NtsInfo();
				deleteParam.setYymm(GLOBAL_YYMM);
				deleteParam.setPayGb(GLOBAL_PAYGB);
				deleteParam.setSabun(sabun);
				int deleteCnt = ntsMapper_Ryhr.deleteExistInfo(existParam);
				log.info("[SUCCESS] DELETE (" + sabun + ") - " + deleteCnt);
			}
			log.info("========== CHECK EXIST END ==========");
			
			log.info("========== " + formcd + " MAIN BEG ==========");
			for (int i = 0; i < mainList.size(); i++) {
				Map map = mainList.get(i);
				for (Object key : map.keySet()) {
					NtsInfo param = new NtsInfo();
					param.setYymm(GLOBAL_YYMM);
					param.setPayGb(GLOBAL_PAYGB);
					param.setSabun(sabun);
					param.setItemKey(key.toString());
					param.setItemData(map.get(key.toString()).toString());
					param.setOrderSeq(orderSeq++);
					int resultCnt = ntsMapper_Ryhr.insertNtsInfo(param);
				}
			}
			log.info("========== " + formcd + " MAIN END ==========");
			
			log.info("========== " + formcd + " DETAIL BEG ==========");
			for (int i = 0; i < detailList.size(); i++) {
				Map map = detailList.get(i);
				
				// XML ELEMENT 중복때문에 예외 처리
				String gbn = "";
				if(map.get("suptFmlyRltClCd") != null) {
					// 인적공제및소득세액공제명세별
					gbn = "B";
				} else if(map.get("brwOrgnBrwnHsngTennLnpbSrmNtsAmt") != null) {
					// 국세청,기타자료항목구분개별
					gbn = "G";
				} else {
					gbn = "";
				}
				
				for (Object key : map.keySet()) {
					NtsInfo param = new NtsInfo();
					param.setYymm(GLOBAL_YYMM);
					param.setPayGb(GLOBAL_PAYGB);
					param.setSabun(sabun);
					param.setItemKey(key.toString());
					param.setItemData(map.get(key.toString()).toString());
					param.setOrderSeq(orderSeq++);
					param.setGbn(gbn);
					int resultCnt = ntsMapper_Ryhr.insertNtsInfo(param);
				}
			}
			log.info("========== " + formcd + " DETAIL END ==========");
		}
		log.info("========== 공제신고서 종료 ==========");
	}
	
	// 연금저축등 소득.세액 공제명세
	private void singoseoB101Y(Document doc, String formcd) {
		log.info("========== 연금저축등 소득.세액 공제명세 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		
		log.info("========== " + formcd + " MAIN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " MAIN END ==========");
		
		log.info("========== " + formcd + " DETAIL BEG ==========");
		for (int i = 0; i < detailList.size(); i++) {
			Map map = detailList.get(i);
			if(map.get("rtpnAccRtpnCl") != null) {
				log.info("########## 퇴직연금계좌별 반복구간 ##########");
			}
			if(map.get("pnsnSvngAccPnsnSvngCl") != null) {
				log.info("########## 연금저축계좌별 반복구간 ##########");
			}
			if(map.get("hsngPrptSvngSvngCl") != null) {
				log.info("########## 주택마련저축소득공제별 반복구간 ##########");
			}
			if(map.get("ltrmCniSsFnnCmp") != null) {
				log.info("########## 장기집합투자증권저축소득공제별 반복구간 ##########");
			}
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " DETAIL END ==========");
		
		log.info("========== 연금저축등 소득.세액 공제명세 종료 ==========");
	}
	
	// 월세액.거주자간 주택임차차입금 상환액
	private void singoseoC101Y(Document doc, String formcd) {
		log.info("========== 월세액.거주자간 주택임차차입금 상환액 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		
		log.info("========== " + formcd + " MAIN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " MAIN END ==========");
		
		log.info("========== " + formcd + " DETAIL BEG ==========");
		for (int i = 0; i < detailList.size(); i++) {
			Map map = detailList.get(i);
			if(map.get("mmrLsrnCtrpAdr") != null) {
				log.info("########## 월세액세액공제명세 반복구간 ##########");
			}
			if(map.get("ctrTermDt") != null) {
				log.info("########## 금전소비대차계약내용 반복구간 ##########");
			}
			if(map.get("lsrnCtrpAdr") != null) {
				log.info("########## 임대차계약내용 반복구간 ##########");
			}
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " DETAIL END ==========");
		
		log.info("========== 월세액.거주자간 주택임차차입금 상환액 종료 ==========");
	}
	
	// 의료비 지급명세
	private void singoseoD101Y(Document doc, String formcd) {
		log.info("========== 의료비 지급명세 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		
		log.info("========== " + formcd + " MAIN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " MAIN END ==========");
		
		log.info("========== " + formcd + " DETAIL BEG ==========");
		for (int i = 0; i < detailList.size(); i++) {
			Map map = detailList.get(i);
			if(map.get("resnoEncCntn") != null) {
				log.info("########## 의료비지급명세 반복구간 ##########");
			}
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " DETAIL END ==========");
		
		log.info("========== 의료비 지급명세 종료 ==========");
	}
	
	// 기부금 명세
	private void singoseoE101Y(Document doc, String formcd) {
		log.info("========== 기부금 명세 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		
		log.info("========== " + formcd + " MAIN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " MAIN END ==========");
		
		log.info("========== " + formcd + " DETAIL BEG ==========");
		for (int i = 0; i < detailList.size(); i++) {
			Map map = detailList.get(i);
			if(map.get("conbCd") != null) {
				log.info("########## 해당연도기부명세 반복구간 ##########");
			}
			if(map.get("amtSum") != null) {
				log.info("########## 구분코드별기부금의합계 반복구간 ##########");
			}
			if(map.get("conbCddl") != null) {
				log.info("########## 기부금조정명세 반복구간 ##########");
			}
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " DETAIL END ==========");
		
		log.info("========== 기부금 명세 종료 ==========");
	}
	
	// 신용카드등 소득공제신청서
	private void singoseoF101Y(Document doc, String formcd) {
		log.info("========== 신용카드등 소득공제신청서 시작 ==========");
		List<Map> mainList = singoseoMain(doc, formcd);
		List<Map> detailList = singoseoDetail(doc, formcd);
		
		log.info("========== " + formcd + " MAIN BEG ==========");
		for (int i = 0; i < mainList.size(); i++) {
			Map map = mainList.get(i);
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " MAIN END ==========");
		
		log.info("========== " + formcd + " DETAIL BEG ==========");
		for (int i = 0; i < detailList.size(); i++) {
			Map map = detailList.get(i);
			if(map.get("nnfCl") != null) {
				log.info("########## 공제대상자및신용카드등사용금액명세 반복구간 ##########");
			}
			for (Object key : map.keySet()) {
				log.info(key.toString() + " : " + map.get(key.toString()));
			}
		}
		log.info("========== " + formcd + " DETAIL END ==========");
		
		log.info("========== 신용카드등 소득공제신청서 종료 ==========");
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

}
