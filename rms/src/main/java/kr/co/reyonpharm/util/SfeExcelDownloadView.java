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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class SfeExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		try {
			String excelType = (String) model.get("excelType");
			String fileName = (String) model.get("fileName");
			List<String> listColumn = (List<String>) model.get("listColumn");
			List<HashMap<String,String>> list = (List<HashMap<String,String>>) model.get("list");

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
			
			CellStyle bgGreyStyle = workbook.createCellStyle();
			bgGreyStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			bgGreyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			// create header row
			Row header = sheet.createRow(0);
			for (int i = 0; i < listColumn.size(); i++) {
				Cell headerCell = header.createCell(i);
				headerCell.setCellValue(listColumn.get(i));
				headerCell.setCellStyle(bgGreyStyle);
				CellUtil.setAlignment(headerCell, HorizontalAlignment.CENTER);
				CellUtil.setFont(headerCell, titleFont);
				CellUtil.setCellStyleProperties(headerCell, borderProperties);
			}
			
			// Create data cells
			int rowCount = 1;
			if(excelType.equals("10")) {
				for (int i = 0; i < list.size(); i++) {
					HashMap map = list.get(i);
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
                    
                    cell00.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPT_NM"))));
                    cell01.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EMP_NM"))));
                    cell02.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_NM"))));
                    cell03.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_GBN_NM"))));
                    cell04.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_CNT_M2"))));
                    cell05.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_CNT_M1"))));
                    cell06.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_CNT_M0"))));
                    cell07.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("SALE_AMT_M2"))));
                    cell08.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("SALE_AMT_M1"))));
                    cell09.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("SALE_AMT_M0"))));
                    cell10.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EDI_AMT_M2"))));
                    cell11.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EDI_AMT_M1"))));
                    cell12.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EDI_AMT_M0"))));
                    cell13.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("RATE_M2"))));
                    cell14.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("RATE_M1"))));
                    cell15.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("RATE_M0"))));
				}
			} else if(excelType.equals("20")) {
				for (int i = 0; i < list.size(); i++) {
					HashMap map = list.get(i);
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
					
                    cell00.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPT_NM"))));
                    cell01.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EMP_NM"))));
                    cell02.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_NM"))));
                    cell03.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_GBN_NM"))));
                    cell04.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("S_DATE"))));
                    cell05.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_TOT_CNT"))));
                    cell06.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_CNT"))));
                    cell07.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_RATE"))));
                    cell08.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_CNT"))));
                    cell09.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_RATE"))));
                    cell10.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_TOT_CNT"))));
                    cell11.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_CNT"))));
                    cell12.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_RATE"))));
                    cell13.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_CNT"))));
                    cell14.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_RATE"))));
				}
			} else if(excelType.equals("30")) {
				for (int i = 0; i < list.size(); i++) {
					HashMap map = list.get(i);
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
					CellUtil.setFont(cell10, contentsFont);
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
                    
                    cell00.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPT_NM"))));
                    cell01.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EMP_NM"))));
                    cell02.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_CNT_M2"))));
                    cell03.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_CNT_M1"))));
                    cell04.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_CNT_M0"))));
                    cell05.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_RATE_M2"))));
                    cell06.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_RATE_M1"))));
                    cell07.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_RATE_M0"))));
                    cell08.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_OK_AVG_CNT"))));
                    cell09.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_CNT_M2"))));
                    cell10.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_CNT_M1"))));
                    cell11.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_CNT_M0"))));
                    cell12.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_RATE_M2"))));
                    cell13.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_RATE_M1"))));
                    cell14.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_RATE_M0"))));
                    cell15.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PLAN_ADD_AVG_CNT"))));
                    cell16.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_CNT_M2"))));
                    cell17.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_CNT_M1"))));
                    cell18.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_CNT_M0"))));
                    cell19.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_RATE_M2"))));
                    cell20.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_RATE_M1"))));
                    cell21.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_RATE_M0"))));
                    cell22.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_OK_AVG_CNT"))));
                    cell23.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_CNT_M2"))));
                    cell24.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_CNT_M1"))));
                    cell25.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_CNT_M0"))));
                    cell26.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_RATE_M2"))));
                    cell27.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_RATE_M1"))));
                    cell28.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_RATE_M0"))));
                    cell29.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_ADD_AVG_CNT"))));
				}
			} else if(excelType.equals("40")) {
				for (int i = 0; i < list.size(); i++) {
					HashMap map = list.get(i);
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
                    
                    cell00.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPT_NM"))));
                    cell01.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EMP_NM"))));
                    cell02.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("SFA_CUST_NM"))));
                    cell03.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("S_DATE"))));
                    cell04.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_GBN_NM"))));
                    cell05.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("VISIT_DATE"))));
                    cell06.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("ST_DATE"))));
                    cell07.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("ED_DATE"))));
                    cell08.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("TIME"))));
                    cell09.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("ACTIVITY_CODE"))));
                    cell10.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("ACTIVITY_DESC"))));
                    cell11.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("PRODUCT_DESC"))));
                    cell12.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("GPS_END_NUM1"))));
                    cell13.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("GPS_END_NUM2"))));
                    cell14.setCellValue(StringUtil.cfCtStatus(StringUtil.nullCheck(String.valueOf(map.get("STATUS")))));
                    cell15.setCellValue(StringUtil.cfCtImigps(StringUtil.nullCheck(String.valueOf(map.get("IMITATION_GPS")))));
                    cell16.setCellValue(StringUtil.cfCtImigps(StringUtil.nullCheck(String.valueOf(map.get("LUTING_YN")))));
                    cell17.setCellValue(StringUtil.cfCtPlan(StringUtil.nullCheck(String.valueOf(map.get("PLAN_GUBUN")))));
				}
			} else if(excelType.equals("50")) {
				for (int i = 0; i < list.size(); i++) {
					HashMap<String,String> map = list.get(i);
					Row courseRow = sheet.createRow(rowCount++);
					Cell cell00 = courseRow.createCell(0);
					Cell cell01 = courseRow.createCell(1);
					Cell cell02 = courseRow.createCell(2);
					Cell cell03 = courseRow.createCell(3);
					Cell cell04 = courseRow.createCell(4);
					Cell cell05 = courseRow.createCell(5);
					Cell cell06 = courseRow.createCell(6);

					CellUtil.setFont(cell00, contentsFont);
					CellUtil.setFont(cell01, contentsFont);
					CellUtil.setFont(cell02, contentsFont);
					CellUtil.setFont(cell03, contentsFont);
					CellUtil.setFont(cell04, contentsFont);
					CellUtil.setFont(cell05, contentsFont);
					CellUtil.setFont(cell06, contentsFont);

					CellUtil.setCellStyleProperties(cell00, borderProperties);
					CellUtil.setCellStyleProperties(cell01, borderProperties);
					CellUtil.setCellStyleProperties(cell02, borderProperties);
					CellUtil.setCellStyleProperties(cell03, borderProperties);
					CellUtil.setCellStyleProperties(cell04, borderProperties);
					CellUtil.setCellStyleProperties(cell05, borderProperties);
					CellUtil.setCellStyleProperties(cell06, borderProperties);
                    
                    cell00.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPT_NM"))));
                    cell01.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("EMP_NM"))));
                    cell02.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("CUS_NM"))));
                    cell03.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("DEPARTMENT_NM"))));
                    cell04.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("JPM_NM"))));
                    cell05.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("ACTIVIT_SUM_CNT"))));
                    cell06.setCellValue(StringUtil.nullCheck(String.valueOf(map.get("SALE_AMT"))));
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
