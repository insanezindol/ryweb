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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.reyonpharm.models.ContractInfo;

public class ContractExcelDownloadView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

		try {
			String excelType = (String) model.get("excelType");
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
			if (excelType.equals("01")) {
				List<ContractInfo> officeList = (List<ContractInfo>) model.get("officeList");
				List<ContractInfo> personList = (List<ContractInfo>) model.get("personList");
				List<ContractInfo> totalList = (List<ContractInfo>) model.get("totalList");
				if (officeList.size() != 1) {
					for (int i = 0; i < officeList.size(); i++) {
						ContractInfo info = officeList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				if (personList.size() != 1) {
					for (int i = 0; i < personList.size(); i++) {
						ContractInfo info = personList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				for (int i = 0; i < totalList.size(); i++) {
					ContractInfo info = totalList.get(i);
					Row courseRow = sheet.createRow(rowCount++);
					Cell cell0 = courseRow.createCell(0);
					Cell cell1 = courseRow.createCell(1);
					Cell cell2 = courseRow.createCell(2);
					Cell cell3 = courseRow.createCell(3);
					Cell cell4 = courseRow.createCell(4);
					Cell cell5 = courseRow.createCell(5);
					Cell cell6 = courseRow.createCell(6);
					Cell cell7 = courseRow.createCell(7);

					CellUtil.setFont(cell0, contentsFont);
					CellUtil.setFont(cell1, contentsFont);
					CellUtil.setFont(cell2, contentsFont);
					CellUtil.setFont(cell3, contentsFont);
					CellUtil.setFont(cell4, contentsFont);
					CellUtil.setFont(cell5, contentsFont);
					CellUtil.setFont(cell6, contentsFont);
					CellUtil.setFont(cell7, contentsFont);

					CellUtil.setCellStyleProperties(cell0, borderProperties);
					CellUtil.setCellStyleProperties(cell1, borderProperties);
					CellUtil.setCellStyleProperties(cell2, borderProperties);
					CellUtil.setCellStyleProperties(cell3, borderProperties);
					CellUtil.setCellStyleProperties(cell4, borderProperties);
					CellUtil.setCellStyleProperties(cell5, borderProperties);
					CellUtil.setCellStyleProperties(cell6, borderProperties);
					CellUtil.setCellStyleProperties(cell7, borderProperties);

					if (info.getPayment().equals("합계")) {
						cell0.setCellValue(info.getPayment());
						sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
						CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
						CellUtil.setFont(cell0, titleFont);
					} else {
						cell0.setCellValue(info.getSaupGubun());
						cell1.setCellValue(info.getUsername());
						cell2.setCellValue(info.getRoadAddr());
						cell3.setCellValue(info.getPayment());
						CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
					}
					cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
					cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
					cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
					cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
					CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
				}
			} else if (excelType.equals("02")) {
				List<ContractInfo> monthlyList = (List<ContractInfo>) model.get("monthlyList");
				List<ContractInfo> yearlyList = (List<ContractInfo>) model.get("yearlyList");
				List<ContractInfo> rentList = (List<ContractInfo>) model.get("rentList");
				List<ContractInfo> possessionList = (List<ContractInfo>) model.get("possessionList");
				List<ContractInfo> totalList = (List<ContractInfo>) model.get("totalList");
				if (monthlyList.size() != 1) {
					for (int i = 0; i < monthlyList.size(); i++) {
						ContractInfo info = monthlyList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				if (yearlyList.size() != 1) {
					for (int i = 0; i < yearlyList.size(); i++) {
						ContractInfo info = yearlyList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				if (rentList.size() != 1) {
					for (int i = 0; i < rentList.size(); i++) {
						ContractInfo info = rentList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				if (possessionList.size() != 1) {
					for (int i = 0; i < possessionList.size(); i++) {
						ContractInfo info = possessionList.get(i);
						Row courseRow = sheet.createRow(rowCount++);
						Cell cell0 = courseRow.createCell(0);
						Cell cell1 = courseRow.createCell(1);
						Cell cell2 = courseRow.createCell(2);
						Cell cell3 = courseRow.createCell(3);
						Cell cell4 = courseRow.createCell(4);
						Cell cell5 = courseRow.createCell(5);
						Cell cell6 = courseRow.createCell(6);
						Cell cell7 = courseRow.createCell(7);

						CellUtil.setFont(cell0, contentsFont);
						CellUtil.setFont(cell1, contentsFont);
						CellUtil.setFont(cell2, contentsFont);
						CellUtil.setFont(cell3, contentsFont);
						CellUtil.setFont(cell4, contentsFont);
						CellUtil.setFont(cell5, contentsFont);
						CellUtil.setFont(cell6, contentsFont);
						CellUtil.setFont(cell7, contentsFont);

						CellUtil.setCellStyleProperties(cell0, borderProperties);
						CellUtil.setCellStyleProperties(cell1, borderProperties);
						CellUtil.setCellStyleProperties(cell2, borderProperties);
						CellUtil.setCellStyleProperties(cell3, borderProperties);
						CellUtil.setCellStyleProperties(cell4, borderProperties);
						CellUtil.setCellStyleProperties(cell5, borderProperties);
						CellUtil.setCellStyleProperties(cell6, borderProperties);
						CellUtil.setCellStyleProperties(cell7, borderProperties);

						if (info.getPayment().equals("소계")) {
							cell0.setCellValue(info.getPayment());
							sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setFont(cell0, titleFont);
						} else {
							cell0.setCellValue(info.getSaupGubun());
							cell1.setCellValue(info.getUsername());
							cell2.setCellValue(info.getRoadAddr());
							cell3.setCellValue(info.getPayment());
							CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
							CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
						}
						cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
						cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
						cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
						cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
						CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
						CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
					}
					sheet.createRow(rowCount++);
				}
				for (int i = 0; i < totalList.size(); i++) {
					ContractInfo info = totalList.get(i);
					Row courseRow = sheet.createRow(rowCount++);
					Cell cell0 = courseRow.createCell(0);
					Cell cell1 = courseRow.createCell(1);
					Cell cell2 = courseRow.createCell(2);
					Cell cell3 = courseRow.createCell(3);
					Cell cell4 = courseRow.createCell(4);
					Cell cell5 = courseRow.createCell(5);
					Cell cell6 = courseRow.createCell(6);
					Cell cell7 = courseRow.createCell(7);

					CellUtil.setFont(cell0, contentsFont);
					CellUtil.setFont(cell1, contentsFont);
					CellUtil.setFont(cell2, contentsFont);
					CellUtil.setFont(cell3, contentsFont);
					CellUtil.setFont(cell4, contentsFont);
					CellUtil.setFont(cell5, contentsFont);
					CellUtil.setFont(cell6, contentsFont);
					CellUtil.setFont(cell7, contentsFont);

					CellUtil.setCellStyleProperties(cell0, borderProperties);
					CellUtil.setCellStyleProperties(cell1, borderProperties);
					CellUtil.setCellStyleProperties(cell2, borderProperties);
					CellUtil.setCellStyleProperties(cell3, borderProperties);
					CellUtil.setCellStyleProperties(cell4, borderProperties);
					CellUtil.setCellStyleProperties(cell5, borderProperties);
					CellUtil.setCellStyleProperties(cell6, borderProperties);
					CellUtil.setCellStyleProperties(cell7, borderProperties);

					if (info.getPayment().equals("합계")) {
						cell0.setCellValue(info.getPayment());
						sheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount - 1, 0, 3));
						CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
						CellUtil.setFont(cell0, titleFont);
					} else {
						cell0.setCellValue(info.getSaupGubun());
						cell1.setCellValue(info.getUsername());
						cell2.setCellValue(info.getRoadAddr());
						cell3.setCellValue(info.getPayment());
						CellUtil.setAlignment(cell0, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell1, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
						CellUtil.setAlignment(cell3, HorizontalAlignment.CENTER);
					}
					cell4.setCellValue(CommonUtils.numberWithCommas(info.getDeposit()));
					cell5.setCellValue(CommonUtils.numberWithCommas(info.getRent()));
					cell6.setCellValue(CommonUtils.numberWithCommas(info.getAdministrativeExpenses()));
					cell7.setCellValue(CommonUtils.numberWithCommas(info.getPaidMoney()));
					CellUtil.setAlignment(cell4, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell5, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell6, HorizontalAlignment.RIGHT);
					CellUtil.setAlignment(cell7, HorizontalAlignment.RIGHT);
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
