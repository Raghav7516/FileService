package com.file.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.file.download.UserExcelView;
import com.file.model.UserDetails;
import com.file.repo.UserRepository;
import com.file.service.UserService;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


@RestController
@RequestMapping("/file")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	
	@PostMapping("/csv")
	public String uploadCSV(@RequestParam("file")MultipartFile file) {
		return userService.uploadCSV(file);
	}
	
	@PostMapping("/excel")
	public String uploadEXCEL(@RequestParam("file") MultipartFile file) {
		return userService.uploadEXCEL(file);
	}
	@GetMapping("/download/excel")
	public ModelAndView userExcel(){
		List<UserDetails> users=userRepository.findAll();
		return new ModelAndView(new UserExcelView(),"userList",users);
	}
	@GetMapping("/download/csv")
	public void userCSV(HttpServletResponse response) throws IOException {
		userService.generateUserReport(response);
		String csvFileName = "user_details-" + new Date().getTime() + ".csv";
		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] header = { "userId", "username", "password", "email" };
		csvWriter.writeHeader(header);
		List<UserDetails> user=userService.getAll();
		for (UserDetails userDetails : user) {
			csvWriter.write(userDetails, header);
		}
		csvWriter.close();
	}
}
