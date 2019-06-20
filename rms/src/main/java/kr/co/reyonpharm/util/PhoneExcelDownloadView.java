package kr.co.reyonpharm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.PhoneInfo;
import net.sf.jxls.transformer.XLSTransformer;

public class PhoneExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		InputStream is = null;
		try {
			String fileName = (String) model.get("fileName");
			String templateFileName = (String) model.get("templateFileName");
			int totalCnt = (int) model.get("totalCnt");
			PhoneInfo info = (PhoneInfo) model.get("info");

			setFileNameToResponse(request, response, info.getRegDate().substring(0, 10) + "_" + fileName + ".xlsx");
			is = new ClassPathResource(templateFileName).getInputStream();
			os = response.getOutputStream();

			XLSTransformer transformer = new XLSTransformer();
			Workbook excel = transformer.transformXLS(is, model);
			
			// merged cell
			Sheet sheet = excel.getSheetAt(0);
			for (int rowIndex = 4; rowIndex <= totalCnt; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				Cell cell1 = row.getCell(1);
				Cell cell2 = row.getCell(2);
				Cell cell3 = row.getCell(3);
				Cell cell5 = row.getCell(5);
				Cell cell6 = row.getCell(6);
				Cell cell7 = row.getCell(7);

				if (cell1 != null) {
					if (cell1.getStringCellValue().equals("")) {
						sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
					}
				}
				if (cell2 != null) {
					if (cell2.getStringCellValue().equals("")) {
						cell2.setCellValue(cell3.getStringCellValue());
						sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
					}
				}
				if (cell5 != null) {
					if (cell5.getStringCellValue().equals("")) {
						sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 5));
					}
				}
				if (cell6 != null) {
					if (cell6.getStringCellValue().equals("")) {
						cell6.setCellValue(cell7.getStringCellValue());
						sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
					}
				}
			}
			
			// auto size cell width
			for (int i = 0; i < 8; i++) {
				sheet.autoSizeColumn(i, true);
			}
						
			//set print setup
			excel.setPrintArea(0, 0, 8, 0, totalCnt);
			sheet.setFitToPage(true);
			
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
