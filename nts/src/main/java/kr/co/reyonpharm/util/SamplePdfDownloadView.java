package kr.co.reyonpharm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
//POI libraries to read Excel File
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

//itext libraries to write PDF file
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SamplePdfDownloadView extends AbstractView {

	public SamplePdfDownloadView() {
		setContentType("application/download; utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		try {
			FileInputStream input_document = new FileInputStream(new File("C:\\Users\\18021201\\developer\\nts\\test1.xls"));
			HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
			HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
			Iterator<Row> rowIterator = my_worksheet.iterator();
			Document iText_xls_2_pdf = new Document();
			PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("C:\\Users\\18021201\\developer\\nts\\test1.pdf"));
			iText_xls_2_pdf.open();
			PdfPTable my_table = new PdfPTable(2);
			PdfPCell table_cell;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
						my_table.addCell(table_cell);
						break;
					}
				}

			}
			iText_xls_2_pdf.add(my_table);
			iText_xls_2_pdf.close();
			input_document.close();
			
			File file = new File("C:\\Users\\18021201\\developer\\nts\\test1.pdf");
			// 한글 파일명 깨짐 현상 조치
			String downloadFileName = URLEncoder.encode(file.getName(), "utf-8");

			response.setContentType(getContentType());
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");

			OutputStream out = response.getOutputStream();

			FileInputStream fis = null;

			try {
				fis = new FileInputStream(file);
				FileCopyUtils.copy(fis, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}