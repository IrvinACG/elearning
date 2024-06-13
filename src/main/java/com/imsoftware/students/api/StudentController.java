package com.imsoftware.students.api;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imsoftware.students.domain.StudentDTO;
import com.imsoftware.students.service.IStudentService;

@RestController
public class StudentController {

	private final IStudentService studentService;

	public StudentController(IStudentService studentService) {
		super();
		this.studentService = studentService;
	}

	@GetMapping("/students")
	Collection<StudentDTO> all() {
		return studentService.findAll();
	}
	
	@GetMapping("/v2/students")
	Collection<StudentDTO> allWithPopularSubject(){
		return studentService.findAllAndShowIfHaveAPopularSubject();
	}
}
