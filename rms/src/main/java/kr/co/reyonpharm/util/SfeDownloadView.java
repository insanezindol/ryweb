package kr.co.reyonpharm.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.SettlementInfo;

public class SfeDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		try {
			String gubun = (String) model.get("gubun");
			String fileName = (String) model.get("fileName");
			List<String> listColumn = (List<String>) model.get("listColumn");
			List<SettlementInfo> list = (List<SettlementInfo>) model.get("list");

			// GMT set seoul
			String timeZone = "GMT+9:00";

			fileName = createFileName(fileName, timeZone);
			setFileNameToResponse(request, response, fileName);

			// create excel xls sheet
			Sheet sheet = workbook.createSheet(fileName);

			// create font style
			Font titleFont = sheet.getWorkbook().createFont();
			titleFont.setFontName("맑은 고딕");
			titleFont.setBold(true);

			Font contentsFont = sheet.getWorkbook().createFont();
			contentsFont.setFontName("맑은 고딕");

			// create border style
			Map<String, Object> borderProperties = new HashMap<String, Object>();
			borderProperties.put(CellUtil.BORDER_TOP, BorderStyle.THIN);
			borderProperties.put(CellUtil.BORDER_RIGHT, BorderStyle.THIN);
			borderProperties.put(CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
			borderProperties.put(CellUtil.BORDER_LEFT, BorderStyle.THIN);

			// create header row
			Row header = sheet.createRow(0);
			for (int i = 0; i < listColumn.size(); i++) {
				Cell headerCell = header.createCell(i);
				headerCell.setCellValue(listColumn.get(i));
				CellUtil.setAlignment(headerCell, HorizontalAlignment.CENTER);
				CellUtil.setFont(headerCell, titleFont);
				CellUtil.setCellStyleProperties(headerCell, borderProperties);
			}

			// Create data cells
			int rowCount = 1;
			
			if(gubun.equals("master")) {
				for (int i = 0; i < list.size(); i++) {
					SettlementInfo info = list.get(i);
					Row courseRow = sheet.createRow(rowCount++);
					
					Cell cell00 = courseRow.createCell(0);
					Cell cell01 = courseRow.createCell(1);
					Cell cell02 = courseRow.createCell(2);
					Cell cell03 = courseRow.createCell(3);
					Cell cell04 = courseRow.createCell(4);
					Cell cell05 = courseRow.createCell(5);
					Cell cell06 = courseRow.createCell(6);
					Cell cell07 = courseRow.createCell(7);
					Cell cell08 = courseRow.createCell(8);
					Cell cell09 = courseRow.createCell(9);
					Cell cell10 = courseRow.createCell(10);
					Cell cell11 = courseRow.createCell(11);
					Cell cell12 = courseRow.createCell(12);
					Cell cell13 = courseRow.createCell(13);
					Cell cell14 = courseRow.createCell(14);
					Cell cell15 = courseRow.createCell(15);
					Cell cell16 = courseRow.createCell(16);
					Cell cell17 = courseRow.createCell(17);
					Cell cell18 = courseRow.createCell(18);
					Cell cell19 = courseRow.createCell(19);
					Cell cell20 = courseRow.createCell(20);
					Cell cell21 = courseRow.createCell(21);
					Cell cell22 = courseRow.createCell(22);
					Cell cell23 = courseRow.createCell(23);
					Cell cell24 = courseRow.createCell(24);
					Cell cell25 = courseRow.createCell(25);

					CellUtil.setFont(cell00, contentsFont);
					CellUtil.setFont(cell01, contentsFont);
					CellUtil.setFont(cell02, contentsFont);
					CellUtil.setFont(cell03, contentsFont);
					CellUtil.setFont(cell04, contentsFont);
					CellUtil.setFont(cell05, contentsFont);
					CellUtil.setFont(cell06, contentsFont);
					CellUtil.setFont(cell07, contentsFont);
					CellUtil.setFont(cell08, contentsFont);
					CellUtil.setFont(cell09, contentsFont);
					CellUtil.setFont(cell10, contentsFont);
					CellUtil.setFont(cell11, contentsFont);
					CellUtil.setFont(cell12, contentsFont);
					CellUtil.setFont(cell13, contentsFont);
					CellUtil.setFont(cell14, contentsFont);
					CellUtil.setFont(cell15, contentsFont);
					CellUtil.setFont(cell16, contentsFont);
					CellUtil.setFont(cell17, contentsFont);
					CellUtil.setFont(cell18, contentsFont);
					CellUtil.setFont(cell19, contentsFont);
					CellUtil.setFont(cell20, contentsFont);
					CellUtil.setFont(cell21, contentsFont);
					CellUtil.setFont(cell22, contentsFont);
					CellUtil.setFont(cell23, contentsFont);
					CellUtil.setFont(cell24, contentsFont);
					CellUtil.setFont(cell25, contentsFont);

					CellUtil.setCellStyleProperties(cell00, borderProperties);
					CellUtil.setCellStyleProperties(cell01, borderProperties);
					CellUtil.setCellStyleProperties(cell02, borderProperties);
					CellUtil.setCellStyleProperties(cell03, borderProperties);
					CellUtil.setCellStyleProperties(cell04, borderProperties);
					CellUtil.setCellStyleProperties(cell05, borderProperties);
					CellUtil.setCellStyleProperties(cell06, borderProperties);
					CellUtil.setCellStyleProperties(cell07, borderProperties);
					CellUtil.setCellStyleProperties(cell08, borderProperties);
					CellUtil.setCellStyleProperties(cell09, borderProperties);
					CellUtil.setCellStyleProperties(cell10, borderProperties);
					CellUtil.setCellStyleProperties(cell11, borderProperties);
					CellUtil.setCellStyleProperties(cell12, borderProperties);
					CellUtil.setCellStyleProperties(cell13, borderProperties);
					CellUtil.setCellStyleProperties(cell14, borderProperties);
					CellUtil.setCellStyleProperties(cell15, borderProperties);
					CellUtil.setCellStyleProperties(cell16, borderProperties);
					CellUtil.setCellStyleProperties(cell17, borderProperties);
					CellUtil.setCellStyleProperties(cell18, borderProperties);
					CellUtil.setCellStyleProperties(cell19, borderProperties);
					CellUtil.setCellStyleProperties(cell20, borderProperties);
					CellUtil.setCellStyleProperties(cell21, borderProperties);
					CellUtil.setCellStyleProperties(cell22, borderProperties);
					CellUtil.setCellStyleProperties(cell23, borderProperties);
					CellUtil.setCellStyleProperties(cell24, borderProperties);
					CellUtil.setCellStyleProperties(cell25, borderProperties);
					
					cell00.setCellValue(info.getKname());
					cell01.setCellValue(info.getJumin());
					cell02.setCellValue(info.getStartdate());
					cell03.setCellValue(info.getEnddate());
					cell04.setCellValue(info.getPayTot());
					cell05.setCellValue(info.getPayGy());
					cell06.setCellValue(info.getPaySy());
					cell07.setCellValue(info.getInjungSy());
					cell08.setCellValue(info.getGainOnStockOptions());
					cell09.setCellValue(info.getEmployeeStockOwnership());
					cell10.setCellValue(info.getOfficerRetirementIncome());
					cell11.setCellValue(info.getJobInvention());
					cell12.setCellValue(info.getIncomeTax());
					cell13.setCellValue(info.getInhabitantTax());
					cell14.setCellValue(info.getCountryTax());
					cell15.setCellValue(info.getAnnuityPay());
					cell16.setCellValue(info.getPublicEmployeePension());
					cell17.setCellValue(info.getMilitaryPension());
					cell18.setCellValue(info.getSchoolEmployeePension());
					cell19.setCellValue(info.getOfficePension());
					cell20.setCellValue(info.getHealthInsurance());
					cell21.setCellValue(info.getEmploymentPremium());
					cell22.setCellValue(info.getStatutoryDonation());
					cell23.setCellValue(info.getStatutoryDonation());
					cell24.setCellValue(info.getReligionOut());
					cell25.setCellValue(info.getNightPay());
				}
			} else if(gubun.equals("slave")) {
				for (int i = 0; i < list.size(); i++) {
					SettlementInfo info = list.get(i);
					Row courseRow = sheet.createRow(rowCount++);
					
					Cell cell00 = courseRow.createCell(0);
					Cell cell01 = courseRow.createCell(1);
					Cell cell02 = courseRow.createCell(2);
					Cell cell03 = courseRow.createCell(3);
					Cell cell04 = courseRow.createCell(4);
					Cell cell05 = courseRow.createCell(5);
					Cell cell06 = courseRow.createCell(6);
					Cell cell07 = courseRow.createCell(7);
					Cell cell08 = courseRow.createCell(8);
					Cell cell09 = courseRow.createCell(9);
					Cell cell10 = courseRow.createCell(10);
					Cell cell11 = courseRow.createCell(11);
					Cell cell12 = courseRow.createCell(12);
					Cell cell13 = courseRow.createCell(13);
					Cell cell14 = courseRow.createCell(14);
					Cell cell15 = courseRow.createCell(15);
					Cell cell16 = courseRow.createCell(16);
					Cell cell17 = courseRow.createCell(17);
					Cell cell18 = courseRow.createCell(18);
					Cell cell19 = courseRow.createCell(19);
					Cell cell20 = courseRow.createCell(20);
					Cell cell21 = courseRow.createCell(21);
					Cell cell22 = courseRow.createCell(22);
					Cell cell23 = courseRow.createCell(23);
					Cell cell24 = courseRow.createCell(24);
					Cell cell25 = courseRow.createCell(25);
					Cell cell26 = courseRow.createCell(26);
					Cell cell27 = courseRow.createCell(27);
					Cell cell28 = courseRow.createCell(28);
					Cell cell29 = courseRow.createCell(29);

					CellUtil.setFont(cell00, contentsFont);
					CellUtil.setFont(cell01, contentsFont);
					CellUtil.setFont(cell02, contentsFont);
					CellUtil.setFont(cell03, contentsFont);
					CellUtil.setFont(cell04, contentsFont);
					CellUtil.setFont(cell05, contentsFont);
					CellUtil.setFont(cell06, contentsFont);
					CellUtil.setFont(cell07, contentsFont);
					CellUtil.setFont(cell08, contentsFont);
					CellUtil.setFont(cell09, contentsFont);
					CellUtil.setFont(cell10, contentsFont);
					CellUtil.setFont(cell11, contentsFont);
					CellUtil.setFont(cell12, contentsFont);
					CellUtil.setFont(cell13, contentsFont);
					CellUtil.setFont(cell14, contentsFont);
					CellUtil.setFont(cell15, contentsFont);
					CellUtil.setFont(cell16, contentsFont);
					CellUtil.setFont(cell17, contentsFont);
					CellUtil.setFont(cell18, contentsFont);
					CellUtil.setFont(cell19, contentsFont);
					CellUtil.setFont(cell20, contentsFont);
					CellUtil.setFont(cell21, contentsFont);
					CellUtil.setFont(cell22, contentsFont);
					CellUtil.setFont(cell23, contentsFont);
					CellUtil.setFont(cell24, contentsFont);
					CellUtil.setFont(cell25, contentsFont);
					CellUtil.setFont(cell26, contentsFont);
					CellUtil.setFont(cell27, contentsFont);
					CellUtil.setFont(cell28, contentsFont);
					CellUtil.setFont(cell29, contentsFont);

					CellUtil.setCellStyleProperties(cell00, borderProperties);
					CellUtil.setCellStyleProperties(cell01, borderProperties);
					CellUtil.setCellStyleProperties(cell02, borderProperties);
					CellUtil.setCellStyleProperties(cell03, borderProperties);
					CellUtil.setCellStyleProperties(cell04, borderProperties);
					CellUtil.setCellStyleProperties(cell05, borderProperties);
					CellUtil.setCellStyleProperties(cell06, borderProperties);
					CellUtil.setCellStyleProperties(cell07, borderProperties);
					CellUtil.setCellStyleProperties(cell08, borderProperties);
					CellUtil.setCellStyleProperties(cell09, borderProperties);
					CellUtil.setCellStyleProperties(cell10, borderProperties);
					CellUtil.setCellStyleProperties(cell11, borderProperties);
					CellUtil.setCellStyleProperties(cell12, borderProperties);
					CellUtil.setCellStyleProperties(cell13, borderProperties);
					CellUtil.setCellStyleProperties(cell14, borderProperties);
					CellUtil.setCellStyleProperties(cell15, borderProperties);
					CellUtil.setCellStyleProperties(cell16, borderProperties);
					CellUtil.setCellStyleProperties(cell17, borderProperties);
					CellUtil.setCellStyleProperties(cell18, borderProperties);
					CellUtil.setCellStyleProperties(cell19, borderProperties);
					CellUtil.setCellStyleProperties(cell20, borderProperties);
					CellUtil.setCellStyleProperties(cell21, borderProperties);
					CellUtil.setCellStyleProperties(cell22, borderProperties);
					CellUtil.setCellStyleProperties(cell23, borderProperties);
					CellUtil.setCellStyleProperties(cell24, borderProperties);
					CellUtil.setCellStyleProperties(cell25, borderProperties);
					CellUtil.setCellStyleProperties(cell26, borderProperties);
					CellUtil.setCellStyleProperties(cell27, borderProperties);
					CellUtil.setCellStyleProperties(cell28, borderProperties);
					CellUtil.setCellStyleProperties(cell29, borderProperties);
					
					cell00.setCellValue(info.getCusNo());
					cell01.setCellValue(info.getKname());
					cell02.setCellValue(info.getJumin());
					cell03.setCellValue(info.getStartdate());
					cell04.setCellValue(info.getEnddate());
					cell05.setCellValue(info.getPayTot());
					cell06.setCellValue(info.getPayGy());
					cell07.setCellValue(info.getPaySy());
					cell08.setCellValue(info.getInjungSy());
					cell09.setCellValue(info.getGainOnStockOptions());
					cell10.setCellValue(info.getEmployeeStockOwnership());
					cell11.setCellValue(info.getOfficerRetirementIncome());
					cell12.setCellValue(info.getJobInvention());
					cell13.setCellValue(info.getIncomeTax());
					cell14.setCellValue(info.getInhabitantTax());
					cell15.setCellValue(info.getCountryTax());
					cell16.setCellValue(info.getAnnuityPay());
					cell17.setCellValue(info.getPublicEmployeePension());
					cell18.setCellValue(info.getMilitaryPension());
					cell19.setCellValue(info.getSchoolEmployeePension());
					cell20.setCellValue(info.getOfficePension());
					cell21.setCellValue(info.getHealthInsurance());
					cell22.setCellValue(info.getEmploymentPremium());
					cell23.setCellValue(info.getEtcPayTot());
					cell24.setCellValue(info.getTaxcutStartYmd());
					cell25.setCellValue(info.getTaxcutEndYmd());
					cell26.setCellValue(info.getTaxcutRate100());
					cell27.setCellValue(info.getTaxcutRate50());
					cell28.setCellValue(info.getTaxcutRate70());
					cell29.setCellValue(info.getTaxcutRate90());
				}
			}

			// auto size cell width
			for (int i = 0; i < listColumn.size(); i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
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

	private String createFileName(String fileName, String timeZone) {
		SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		fileFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		return new StringBuilder(fileFormat.format(new Date())).append("_").append(fileName).append(".xls").toString();
	}

}
