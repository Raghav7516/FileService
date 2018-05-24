package com.file.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.file.model.UserDetails;

public class UserExcelView extends AbstractXlsxView {
	

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> map, Workbook book,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.addHeader("Content-Disposition", "attachment;filename=\"UserData.xlsx\"");
		List<UserDetails> uomList=(List<UserDetails>) map.get("userList");
		Sheet sheet=book.createSheet("user");
		setHead(sheet);
		setBody(sheet,uomList);
	}
	private void setHead(Sheet sheet) {
		Row row=sheet.createRow(0);
		row.createCell(0).setCellValue("USER ID");
		row.createCell(1).setCellValue("USER NAME");
		row.createCell(2).setCellValue("PASSWORD");
		row.createCell(3).setCellValue("EMAIL");
	}

	private void setBody(Sheet sheet, List<UserDetails> userList) {
		int rowNum=1;
		for(UserDetails user: userList){
			Row row=sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(user.getUserId());
			row.createCell(1).setCellValue(user.getUsername());
			row.createCell(2).setCellValue(user.getPassword());
			row.createCell(3).setCellValue(user.getEmail());
		}
		
	}


}
