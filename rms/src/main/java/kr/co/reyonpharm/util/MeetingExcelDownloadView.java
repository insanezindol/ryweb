package kr.co.reyonpharm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.MeetingInfo;
import net.sf.jxls.transformer.XLSTransformer;

public class MeetingExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		InputStream is = null;
		try {
			String fileName = (String) model.get("fileName");
			String templateFileName = (String) model.get("templateFileName");
			MeetingInfo info = (MeetingInfo) model.get("info");

			setFileNameToResponse(request, response, info.getMeetingStartDate().substring(0, 10) + "_" + fileName + ".xlsx");

			is = new ClassPathResource(templateFileName).getInputStream();
			os = response.getOutputStream();

			XLSTransformer transformer = new XLSTransformer();
			Workbook excel = transformer.transformXLS(is, model);

			// 셀 스타일 (개행, 세로 가운데 정렬, 하단 선)
			CellStyle cs = excel.createCellStyle();
			cs.setWrapText(true);
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			cs.setBorderBottom(BorderStyle.THIN);

			Sheet sheet = excel.getSheet("회의록");
			
			// html string to plain string
			String meetingContents = info.getMeetingContents();
			String decisionContents = info.getDecisionContents();
			String planContents = info.getPlanContents();
			String issueContents = info.getIssueContents();
			if (meetingContents != null) {
				meetingContents = meetingContents.replaceAll("<br>", "\n");
				meetingContents = meetingContents.replaceAll("</p><p>", "\n");
				meetingContents = meetingContents.replaceAll("&nbsp;", " ");
				meetingContents = meetingContents.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
			}
			if (decisionContents != null) {
				decisionContents = decisionContents.replaceAll("<br>", "\n");
				decisionContents = decisionContents.replaceAll("</p><p>", "\n");
				decisionContents = decisionContents.replaceAll("&nbsp;", " ");
				decisionContents = decisionContents.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
			}
			if (planContents != null) {
				planContents = planContents.replaceAll("<br>", "\n");
				planContents = planContents.replaceAll("</p><p>", "\n");
				planContents = planContents.replaceAll("&nbsp;", " ");
				planContents = planContents.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
			}
			if (issueContents != null) {
				issueContents = issueContents.replaceAll("<br>", "\n");
				issueContents = issueContents.replaceAll("</p><p>", "\n");
				issueContents = issueContents.replaceAll("&nbsp;", " ");
				issueContents = issueContents.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
			}
			
			// 회의내용
			Row row8 = sheet.getRow(8);
			Cell cell86 = row8.getCell(6);
			cell86.setCellStyle(cs);
			cell86.setCellValue(meetingContents);

			// 결정사항
			Row row9 = sheet.getRow(9);
			Cell cell96 = row9.getCell(6);
			cell96.setCellStyle(cs);
			cell96.setCellValue(decisionContents);

			// 향후일정
			Row row10 = sheet.getRow(10);
			Cell cell106 = row10.getCell(6);
			cell106.setCellStyle(cs);
			cell106.setCellValue(planContents);

			// 특이사항
			Row row11 = sheet.getRow(11);
			Cell cell116 = row11.getCell(6);
			cell116.setCellStyle(cs);
			cell116.setCellValue(issueContents);

			// 첨부파일
			Row row12 = sheet.getRow(12);
			Cell cell126 = row12.getCell(6);
			cell126.setCellStyle(cs);

			excel.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

	}

	private void setFileNameToResponse(HttpServletRequest request, HttpServletResponse response, String fileName) {
		String fileNameEnc = fileName;
		try {
			fileNameEnc = new String(fileName.getBytes("euc-kr"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("MSIE 5.5") >= 0) {
			response.setContentType("doesn/matter");
			response.setHeader("Content-Disposition", "filename=\"" + fileNameEnc + "\"");
		} else {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\"");
		}
	}

}
