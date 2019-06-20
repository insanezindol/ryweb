package kr.co.reyonpharm.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.SalaryInfo;

public class SalaryExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
		try {
			String fileName = (String) model.get("fileName");
			SalaryInfo info = (SalaryInfo) model.get("info");
			
			List<String> jgTitleList = new ArrayList<String>();
			List<String> jgValueList = new ArrayList<String>();
			List<String> gjTitleList = new ArrayList<String>();
			List<String> gjValueList = new ArrayList<String>();
			int arrMaxLength = 0;
			
			// 지급 내역 리스트
			if(!info.getAa01().equals("0")) {
				jgTitleList.add("기본급");
				jgValueList.add(info.getAa01());
			}
			if(!info.getAa02().equals("0")) {
				jgTitleList.add("직책수당");
				jgValueList.add(info.getAa02());
			}
			if(!info.getAa03().equals("0")) {
				jgTitleList.add("업무수당");
				jgValueList.add(info.getAa03());
			}
			if(!info.getAa04().equals("0")) {
				jgTitleList.add("조정수당");
				jgValueList.add(info.getAa04());
			}
			if(!info.getAa05().equals("0")) {
				jgTitleList.add("면허수당");
				jgValueList.add(info.getAa05());
			}
			if(!info.getAa06().equals("0")) {
				jgTitleList.add("자격수당");
				jgValueList.add(info.getAa06());
			}
			if(!info.getAa07().equals("0")) {
				jgTitleList.add("연장근로수당");
				jgValueList.add(info.getAa07());
			}
			if(!info.getAa08().equals("0")) {
				jgTitleList.add("휴일근로수당");
				jgValueList.add(info.getAa08());
			}
			if(!info.getAa09().equals("0")) {
				jgTitleList.add("연차수당");
				jgValueList.add(info.getAa09());
			}
			if(!info.getAa10().equals("0")) {
				jgTitleList.add("소급");
				jgValueList.add(info.getAa10());
			}
			if(!info.getAa11().equals("0")) {
				jgTitleList.add("기타지급");
				jgValueList.add(info.getAa11());
			}
			if(!info.getAa12().equals("0")) {
				jgTitleList.add("학자금");
				jgValueList.add(info.getAa12());
			}
			if(!info.getAa13().equals("0")) {
				jgTitleList.add("식대");
				jgValueList.add(info.getAa13());
			}
			
			// 공제 내역 리스트
			if (!info.getCc01().equals("0")) {
				gjTitleList.add("소득세");
				gjValueList.add(info.getCc01());
			}
			if (!info.getCc02().equals("0")) {
				gjTitleList.add("주민세");
				gjValueList.add(info.getCc02());
			}
			if (!info.getCc03().equals("0")) {
				gjTitleList.add("농특세");
				gjValueList.add(info.getCc03());
			}
			if (!info.getCc04().equals("0")) {
				gjTitleList.add("연말정산");
				gjValueList.add(info.getCc04());
			}
			if (!info.getBb01().equals("0")) {
				gjTitleList.add("국민연금");
				gjValueList.add(info.getBb01());
			}
			if (!info.getBb02().equals("0")) {
				gjTitleList.add("건강보험");
				gjValueList.add(info.getBb02());
			}
			if (!info.getBb03().equals("0")) {
				gjTitleList.add("장기요양보험");
				gjValueList.add(info.getBb03());
			}
			if (!info.getBb04().equals("0")) {
				gjTitleList.add("고용보험");
				gjValueList.add(info.getBb04());
			}
			if (!info.getBb06().equals("0")) {
				gjTitleList.add("사우회비");
				gjValueList.add(info.getBb06());
			}
			if (!info.getBb07().equals("0")) {
				gjTitleList.add("대출이자");
				gjValueList.add(info.getBb07());
			}
			if (!info.getBb08().equals("0")) {
				gjTitleList.add("지급보류");
				gjValueList.add(info.getBb08());
			}
			if (!info.getBb09().equals("0")) {
				gjTitleList.add("채권압류");
				gjValueList.add(info.getBb09());
			}
			if (!info.getBb10().equals("0")) {
				gjTitleList.add("기타공제1");
				gjValueList.add(info.getBb10());
			}
			if (!info.getBb11().equals("0")) {
				gjTitleList.add("기타공제2");
				gjValueList.add(info.getBb11());
			}
			
			if (jgTitleList.size() > gjTitleList.size()) {
				arrMaxLength = jgTitleList.size();
			} else {
				arrMaxLength = gjTitleList.size();
			}
			
			setFileNameToResponse(request, response, info.getYymm() + "_" + info.getKname() + "_" + fileName + ".xls");
			
			// create excel xls sheet
			Sheet sheet = workbook.createSheet(fileName);
			
			// create font style
			Font topFont = sheet.getWorkbook().createFont();
			topFont.setFontName("맑은 고딕");
			topFont.setBold(true);
			topFont.setFontHeightInPoints((short) 15);
			
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
			borderProperties.put(CellUtil.WRAP_TEXT, true);
			
			// create background style
			CellStyle bgGreyStyle = workbook.createCellStyle();
			bgGreyStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
			bgGreyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// create title row
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short)500);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue(info.getYyyy() + "년 " + info.getMm() + "월 "+fileName);
			CellUtil.setAlignment(titleCell, HorizontalAlignment.CENTER);
			CellUtil.setFont(titleCell, topFont);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			
			// create title row
			Row title1Row = sheet.createRow(2);
			title1Row.setHeight((short)333);
			Cell title1Cell0 = title1Row.createCell(0);
			Cell title1Cell1 = title1Row.createCell(1);
			Cell title1Cell2 = title1Row.createCell(2);
			Cell title1Cell3 = title1Row.createCell(3);
			Cell title1Cell4 = title1Row.createCell(4);
			Cell title1Cell5 = title1Row.createCell(5);
			Cell title1Cell6 = title1Row.createCell(6);
			Cell title1Cell7 = title1Row.createCell(7);
			
			title1Cell0.setCellValue("소속");
			title1Cell1.setCellValue(info.getDeptName());
			title1Cell2.setCellValue("직급");
			title1Cell3.setCellValue(info.getGrade());
			title1Cell4.setCellValue("성명");
			title1Cell5.setCellValue(info.getKname());
			title1Cell6.setCellValue("지급일");
			title1Cell7.setCellValue(info.getPayDate().substring(0, 4)+"-"+info.getPayDate().substring(4, 6)+"-"+info.getPayDate().substring(6, 8));
			
			title1Cell0.setCellStyle(bgGreyStyle);
			title1Cell2.setCellStyle(bgGreyStyle);
			title1Cell4.setCellStyle(bgGreyStyle);
			title1Cell6.setCellStyle(bgGreyStyle);
			
			CellUtil.setCellStyleProperties(title1Cell0, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell1, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell2, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell3, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell4, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell5, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell6, borderProperties);
			CellUtil.setCellStyleProperties(title1Cell7, borderProperties);
			
			CellUtil.setAlignment(title1Cell0, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell0, titleFont);
			CellUtil.setAlignment(title1Cell1, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell1, titleFont);
			CellUtil.setAlignment(title1Cell2, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell2, titleFont);
			CellUtil.setAlignment(title1Cell3, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell3, titleFont);
			CellUtil.setAlignment(title1Cell4, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell4, titleFont);
			CellUtil.setAlignment(title1Cell5, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell5, titleFont);
			CellUtil.setAlignment(title1Cell6, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell6, titleFont);
			CellUtil.setAlignment(title1Cell7, HorizontalAlignment.CENTER);
			CellUtil.setFont(title1Cell7, titleFont);
			
			// create title row
			Row title2Row = sheet.createRow(4);
			title2Row.setHeight((short)333);
			Cell title2Cell0 = title2Row.createCell(0);
			Cell title2Cell1 = title2Row.createCell(1);
			Cell title2Cell2 = title2Row.createCell(2);
			Cell title2Cell3 = title2Row.createCell(3);
			Cell title2Cell4 = title2Row.createCell(4);
			Cell title2Cell5 = title2Row.createCell(5);
			Cell title2Cell6 = title2Row.createCell(6);
			Cell title2Cell7 = title2Row.createCell(7);
			
			title2Cell0.setCellValue("지급총액");
			title2Cell2.setCellValue("공제액계");
			title2Cell4.setCellValue("차인지급액");
			title2Cell5.setCellValue("연장 근로시간");
			title2Cell6.setCellValue("휴일 근로시간");
			title2Cell7.setCellValue("급(상)여율");
			
			title2Cell0.setCellStyle(bgGreyStyle);
			title2Cell2.setCellStyle(bgGreyStyle);
			title2Cell4.setCellStyle(bgGreyStyle);
			title2Cell5.setCellStyle(bgGreyStyle);
			title2Cell6.setCellStyle(bgGreyStyle);
			title2Cell7.setCellStyle(bgGreyStyle);
			
			CellUtil.setCellStyleProperties(title2Cell0, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell1, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell2, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell3, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell4, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell5, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell6, borderProperties);
			CellUtil.setCellStyleProperties(title2Cell7, borderProperties);
			
			CellUtil.setAlignment(title2Cell0, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell0, titleFont);
			CellUtil.setAlignment(title2Cell2, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell2, titleFont);
			CellUtil.setAlignment(title2Cell4, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell4, titleFont);
			CellUtil.setAlignment(title2Cell5, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell5, titleFont);
			CellUtil.setAlignment(title2Cell6, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell6, titleFont);
			CellUtil.setAlignment(title2Cell7, HorizontalAlignment.CENTER);
			CellUtil.setFont(title2Cell7, titleFont);
			
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));
			
			// create title row
			Row title3Row = sheet.createRow(5);
			title3Row.setHeight((short)333);
			Cell title3Cell0 = title3Row.createCell(0);
			Cell title3Cell1 = title3Row.createCell(1);
			Cell title3Cell2 = title3Row.createCell(2);
			Cell title3Cell3 = title3Row.createCell(3);
			Cell title3Cell4 = title3Row.createCell(4);
			Cell title3Cell5 = title3Row.createCell(5);
			Cell title3Cell6 = title3Row.createCell(6);
			Cell title3Cell7 = title3Row.createCell(7);
			
			title3Cell0.setCellValue(info.getPayTotal());
			title3Cell2.setCellValue(info.getSubTotal());
			title3Cell4.setCellValue(info.getPayRemain());
			title3Cell5.setCellValue(info.getOtNight());
			title3Cell6.setCellValue(info.getOtSunday());
			title3Cell7.setCellValue(info.getBonusRate());
			
			CellUtil.setCellStyleProperties(title3Cell0, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell1, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell2, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell3, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell4, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell5, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell6, borderProperties);
			CellUtil.setCellStyleProperties(title3Cell7, borderProperties);
			
			CellUtil.setAlignment(title3Cell0, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell0, titleFont);
			CellUtil.setAlignment(title3Cell2, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell2, titleFont);
			CellUtil.setAlignment(title3Cell4, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell4, titleFont);
			CellUtil.setAlignment(title3Cell5, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell5, titleFont);
			CellUtil.setAlignment(title3Cell6, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell6, titleFont);
			CellUtil.setAlignment(title3Cell7, HorizontalAlignment.CENTER);
			CellUtil.setFont(title3Cell7, titleFont);
			
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));
			
			// data title row
			Row dataTitleRow = sheet.createRow(7);
			dataTitleRow.setHeight((short)333);
			Cell data1Cell0 = dataTitleRow.createCell(0);
			Cell data1Cell1 = dataTitleRow.createCell(1);
			Cell data1Cell2 = dataTitleRow.createCell(2);
			Cell data1Cell3 = dataTitleRow.createCell(3);
			Cell data1Cell4 = dataTitleRow.createCell(4);
			Cell data1Cell5 = dataTitleRow.createCell(5);
			Cell data1Cell6 = dataTitleRow.createCell(6);
			Cell data1Cell7 = dataTitleRow.createCell(7);
			
			data1Cell0.setCellValue("지급내역");
			data1Cell2.setCellValue("금액");
			data1Cell4.setCellValue("공제내역");
			data1Cell6.setCellValue("금액");
			
			data1Cell0.setCellStyle(bgGreyStyle);
			data1Cell2.setCellStyle(bgGreyStyle);
			data1Cell4.setCellStyle(bgGreyStyle);
			data1Cell6.setCellStyle(bgGreyStyle);
			
			CellUtil.setCellStyleProperties(data1Cell0, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell1, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell2, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell3, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell4, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell5, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell6, borderProperties);
			CellUtil.setCellStyleProperties(data1Cell7, borderProperties);
			
			CellUtil.setAlignment(data1Cell0, HorizontalAlignment.CENTER);
			CellUtil.setFont(data1Cell0, titleFont);
			CellUtil.setAlignment(data1Cell2, HorizontalAlignment.CENTER);
			CellUtil.setFont(data1Cell2, titleFont);
			CellUtil.setAlignment(data1Cell4, HorizontalAlignment.CENTER);
			CellUtil.setFont(data1Cell4, titleFont);
			CellUtil.setAlignment(data1Cell6, HorizontalAlignment.CENTER);
			CellUtil.setFont(data1Cell6, titleFont);
			
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 2, 3));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 4, 5));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 6, 7));
			
			// data row
			for (int i = 0; i < arrMaxLength; i++) {
				Row dataRow = sheet.createRow(8+i);
				dataRow.setHeight((short)333);
				Cell dataCell0 = dataRow.createCell(0);
				Cell dataCell1 = dataRow.createCell(1);
				Cell dataCell2 = dataRow.createCell(2);
				Cell dataCell3 = dataRow.createCell(3);
				Cell dataCell4 = dataRow.createCell(4);
				Cell dataCell5 = dataRow.createCell(5);
				Cell dataCell6 = dataRow.createCell(6);
				Cell dataCell7 = dataRow.createCell(7);
				
				dataCell0.setCellStyle(bgGreyStyle);				
				dataCell4.setCellStyle(bgGreyStyle);
				
				if(jgTitleList.size() > i) {
					dataCell0.setCellValue(jgTitleList.get(i));
					dataCell2.setCellValue(jgValueList.get(i));
				} else {
					dataCell0.setCellValue("");
					dataCell2.setCellValue("");
				}
				
				if(gjTitleList.size() > i) {
					dataCell4.setCellValue(gjTitleList.get(i));
					dataCell6.setCellValue(gjValueList.get(i));
				} else {
					dataCell4.setCellValue("");
					dataCell6.setCellValue("");
				}
				
				CellUtil.setCellStyleProperties(dataCell0, borderProperties);
				CellUtil.setCellStyleProperties(dataCell1, borderProperties);
				CellUtil.setCellStyleProperties(dataCell2, borderProperties);
				CellUtil.setCellStyleProperties(dataCell3, borderProperties);
				CellUtil.setCellStyleProperties(dataCell4, borderProperties);
				CellUtil.setCellStyleProperties(dataCell5, borderProperties);
				CellUtil.setCellStyleProperties(dataCell6, borderProperties);
				CellUtil.setCellStyleProperties(dataCell7, borderProperties);
				
				CellUtil.setAlignment(dataCell0, HorizontalAlignment.CENTER);
				CellUtil.setFont(dataCell0, titleFont);
				CellUtil.setAlignment(dataCell2, HorizontalAlignment.RIGHT);
				CellUtil.setFont(dataCell2, titleFont);
				CellUtil.setAlignment(dataCell4, HorizontalAlignment.CENTER);
				CellUtil.setFont(dataCell4, titleFont);
				CellUtil.setAlignment(dataCell6, HorizontalAlignment.RIGHT);
				CellUtil.setFont(dataCell6, titleFont);
				
				sheet.addMergedRegion(new CellRangeAddress(8+i, 8+i, 0, 1));
				sheet.addMergedRegion(new CellRangeAddress(8+i, 8+i, 2, 3));
				sheet.addMergedRegion(new CellRangeAddress(8+i, 8+i, 4, 5));
				sheet.addMergedRegion(new CellRangeAddress(8+i, 8+i, 6, 7));
			}
			
			// data sum row
			Row dataSumRow = sheet.createRow(8+arrMaxLength);
			dataSumRow.setHeight((short)333);
			Cell dataSumCell0 = dataSumRow.createCell(0);
			Cell dataSumCell1 = dataSumRow.createCell(1);
			Cell dataSumCell2 = dataSumRow.createCell(2);
			Cell dataSumCell3 = dataSumRow.createCell(3);
			Cell dataSumCell4 = dataSumRow.createCell(4);
			Cell dataSumCell5 = dataSumRow.createCell(5);
			Cell dataSumCell6 = dataSumRow.createCell(6);
			Cell dataSumCell7 = dataSumRow.createCell(7);
			
			dataSumCell0.setCellStyle(bgGreyStyle);				
			dataSumCell4.setCellStyle(bgGreyStyle);
			
			dataSumCell0.setCellValue("지급총액");
			dataSumCell2.setCellValue(info.getPayTotal());
			dataSumCell4.setCellValue("공제총액");
			dataSumCell6.setCellValue(info.getSubTotal());
			
			CellUtil.setCellStyleProperties(dataSumCell0, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell1, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell2, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell3, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell4, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell5, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell6, borderProperties);
			CellUtil.setCellStyleProperties(dataSumCell7, borderProperties);
			
			CellUtil.setAlignment(dataSumCell0, HorizontalAlignment.CENTER);
			CellUtil.setFont(dataSumCell0, titleFont);
			CellUtil.setAlignment(dataSumCell2, HorizontalAlignment.RIGHT);
			CellUtil.setFont(dataSumCell2, titleFont);
			CellUtil.setAlignment(dataSumCell4, HorizontalAlignment.CENTER);
			CellUtil.setFont(dataSumCell4, titleFont);
			CellUtil.setAlignment(dataSumCell6, HorizontalAlignment.RIGHT);
			CellUtil.setFont(dataSumCell6, titleFont);
			
			sheet.addMergedRegion(new CellRangeAddress(8+arrMaxLength, 8+arrMaxLength, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(8+arrMaxLength, 8+arrMaxLength, 2, 3));
			sheet.addMergedRegion(new CellRangeAddress(8+arrMaxLength, 8+arrMaxLength, 4, 5));
			sheet.addMergedRegion(new CellRangeAddress(8+arrMaxLength, 8+arrMaxLength, 6, 7));
			
			// footer row
			Row footerRow = sheet.createRow(10+arrMaxLength);
			footerRow.setHeight((short)333);
			Cell footerCell0 = footerRow.createCell(0);
			footerCell0.setCellValue("귀하의 노고에 감사드립니다. 이연제약(주)");
			CellUtil.setAlignment(footerCell0, HorizontalAlignment.RIGHT);
			CellUtil.setFont(footerCell0, titleFont);
			sheet.addMergedRegion(new CellRangeAddress(10+arrMaxLength, 10+arrMaxLength, 0, 7));
			
			for (int i = 0; i < 8; i++) {
				sheet.setColumnWidth(i, 15*256);
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

}
