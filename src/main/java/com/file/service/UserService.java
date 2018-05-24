package com.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.model.UserDetails;
import com.file.repo.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{

		File convFile = new File(multipart.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multipart.getBytes());
		fos.close();
		return convFile;
	}

	@SuppressWarnings("resource")
	public String uploadCSV(MultipartFile file) {
		try {
			Reader reader=new FileReader(multipartToFile(file));
			CSVParser csvparser=new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			List<CSVRecord> csvRecords=csvparser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				if(csvRecord.get(0).isEmpty() || csvRecord.get(1).isEmpty() || csvRecord.get(2).isEmpty() || csvRecord.get(3).isEmpty())
					return "data not found";
				UserDetails user=new UserDetails();
				user.setUsername(csvRecord.get(1));
				user.setPassword(csvRecord.get(2));
				user.setEmail(csvRecord.get(3));
				userRepository.save(user);
			}
			
		} catch (Exception e) {
		}
		return "file upload";
	}

	@SuppressWarnings("resource")
	public String uploadEXCEL(MultipartFile file) {
		try {
			InputStream is=file.getInputStream();
			XSSFWorkbook book=new XSSFWorkbook(is);
			XSSFSheet sheet=book.getSheet("user");
			for (Row row : sheet) {
				UserDetails user=new UserDetails();
				user.setUsername(row.getCell(0).getStringCellValue());
				user.setPassword(row.getCell(1).getStringCellValue());
				user.setEmail(row.getCell(2).getStringCellValue());
				userRepository.save(user);
				
			}
		} catch (Exception e) {
		}
		
		return "file upload";
	}
	
	public void generateUserReport(HttpServletResponse response) {
	}

	public List<UserDetails> getAll() {
	List<UserDetails> user=	userRepository.findAll();
		return user;
	}
}
