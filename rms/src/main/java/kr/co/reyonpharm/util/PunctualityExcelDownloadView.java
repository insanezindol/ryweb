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

import kr.co.reyonpharm.models.PunctualityInfo;

public class PunctualityExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		try {
			String fileName = (String) model.get("fileName");
			String sheetName = (String) model.get("sheetName");
			List<String> listColumn = (List<String>) model.get("listColumn");

			// GMT set seoul
			String timeZone = "GMT+9:00";

			fileName = createFileName(fileName, timeZone);
			setFileNameToResponse(request, response, fileName);

			// create excel xls sheet
			Sheet sheet = workbook.createSheet(sheetName);

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
			List<PunctualityInfo> listData = (List<PunctualityInfo>) model.get("listData");
			for (int i = 0; i < listData.size(); i++) {
				PunctualityInfo info = listData.get(i);
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

				cell0.setCellValue((i+1));
				cell1.setCellValue(info.getDeptName());
				cell2.setCellValue(info.getKname());
				cell3.setCellValue(info.getPosLog());
				cell4.setCellValue(info.getInDate());
				cell5.setCellValue(info.getActYn());
				cell6.setCellValue(info.getStartWork());
				cell7.setCellValue(info.getEndWork());
				cell8.setCellValue(info.getBigo());
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
