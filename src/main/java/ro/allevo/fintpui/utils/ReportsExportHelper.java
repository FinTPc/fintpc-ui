/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ro.allevo.fintpui.controllers.ReportsController;

public class ReportsExportHelper {
	
	private static Logger logger = LogManager.getLogger(ReportsController.class
			.getName());
	
	public static ResponseEntity<byte[]> ExportToPDF(
			String fileName,
			List<String> header,
			List<List<String>> data
			) {
		
		//tmp pdf
		Document document = new Document();
		document.setPageSize(PageSize.A3.rotate());
		document.setMargins(10f, 10f, 10f, 10f);
        // step 2
		byte[] contents = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try {
	        PdfWriter.getInstance(document, os);
	        // step 3
	        document.open();
	        // step 4
	        
	        PdfPTable table = null;
	        
	        //header
	        
			//JSONArray a = new JSONArray(allRequestParams.get("columnHeaders"));
			
			table = new PdfPTable(header.size());
			table.setWidthPercentage(100);
			
			Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
			for (String head : header) {
				Phrase p = new Phrase(head, boldFont);
				table.addCell(p);
			}
			
			Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
			
			for (List<String> l : data) {
				for (String v : l) {
					Phrase p = new Phrase(v, normalFont);
					table.addCell(v);
				}
			}
	    
			document.add(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // step 5
        document.close();
        
        contents = os.toByteArray();
		
        logger.debug("Writing to PDF file finished ...");
        

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    
	    headers.setContentDispositionFormData(fileName, fileName);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
	}

	public static ResponseEntity<byte[]> ExportToExcel(
			String fileName,
			List<String> header,
			List<List<String>> data
			) {
		
		XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        
        //header
        Row row = sheet.createRow(0);
        
		for (int i=0;i<header.size();i++){ 
			Cell c = row.createCell(i);
			c.setCellValue(header.get(i));
	   	}
        
		int rownum = 1;
		
		for (List<String> l : data) {
			row = sheet.createRow(rownum++);
			int cellnum = 0;
			
			for (String v : l) {
				Cell c = row.createCell(cellnum++);
				c.setCellValue(v);
			}
		}
        
		byte[] contents = null;
		
        // open an OutputStream to save written data into XLSX file
        try {
	        //FileOutputStream os = new FileOutputStream(tmp);
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        wb.write(os);
	        contents = os.toByteArray();
	        os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        logger.debug("Writing to XLSX file finished ...");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    
	    headers.setContentDispositionFormData(fileName, fileName);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
	}
}