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
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.VehicleInfo;

public class VehicleExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		try {
			String excelType = (String) model.get("excelType");
			String fileName = (String) model.get("fileName");
			String sheetName = (String) model.get("sheetName");
			String standardDate = (String) model.get("standardDate");
			List<String> listColumn = (List<String>) model.get("listColumn");
			List<VehicleInfo> list = (List<VehicleInfo>) model.get("list");

			// GMT set seoul
			String timeZone = "GMT+9:00";

			fileName = createFileName(fileName, timeZone);
			setFileNameToResponse(request, response, fileName);

			// create excel xls sheet
			Sheet sheet = workbook.createSheet(sheetName);

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
			bgGreyStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			bgGreyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// create title row
			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("법 인 차 량 현 황 (" + (list.size() - 1) + "대)");
			CellUtil.setAlignment(titleCell, HorizontalAlignment.CENTER);
			CellUtil.setFont(titleCell, topFont);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

			// create date row
			Row dateRow = sheet.createRow(1);
			Cell dateCell = dateRow.createCell(0);
			dateCell.setCellValue(standardDate);
			CellUtil.setAlignment(dateCell, HorizontalAlignment.RIGHT);
			CellUtil.setFont(dateCell, titleFont);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

			// create header row
			Row header = sheet.createRow(2);
			header.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
			for (int i = 0; i < listColumn.size(); i++) {
				Cell headerCell = header.createCell(i);
				headerCell.setCellValue(listColumn.get(i));
				headerCell.setCellStyle(bgGreyStyle);
				CellUtil.setAlignment(headerCell, HorizontalAlignment.CENTER);
				CellUtil.setVerticalAlignment(headerCell, VerticalAlignment.CENTER);
				CellUtil.setFont(headerCell, titleFont);
				CellUtil.setCellStyleProperties(headerCell, borderProperties);
			}

			// Create data cells
			int rowCount = 3;
			if (excelType.equals("01")) {
				if (list.size() != 1) {
					for (int i = 0; i < list.size(); i++) {
						VehicleInfo info = list.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);
						Cell cell8 = courseRow.createCell(8);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);
						CellUtil.setFont(cell8, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);
						CellUtil.setCellStyleProperties(cell8, borderProperties);

						CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell4, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell5, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell8, HorizontalAlignment.RIGHT);

						if (info.getPayment().equals("가합계")) {
							cell0.setCellValue("합계");
							cell6.setCellValue(CommonUtils.numberWithCommas(info.getInsuranceMoney()));
							cell8.setCellValue(CommonUtils.numberWithCommas(info.getRentMoney()));
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 4));
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(rowCount - 3);
							cell1.setCellValue(info.getVehicleType());
							cell2.setCellValue(info.getVehicleNo());
							cell3.setCellValue(info.getUsername());
							cell4.setCellValue(info.getPayment());
							if (info.getInsuranceStartDate() != null && info.getInsuranceEndDate() != null) {
								cell5.setCellValue(info.getInsuranceStartDate() + " ~ " + info.getInsuranceEndDate());
							} else {
								cell5.setCellValue("");
							}
							cell6.setCellValue(CommonUtils.numberWithCommas(info.getInsuranceMoney()));
							if (info.getPayment().equals("보유")) {
								cell7.setCellValue(info.getRentStartDate());
							} else {
								cell7.setCellValue(info.getRentStartDate() + " ~ " + info.getRentEndDate());
							}
							cell8.setCellValue(CommonUtils.numberWithCommas(info.getRentMoney()));
						}
					}
					sheet.createRow(rowCount++);
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
