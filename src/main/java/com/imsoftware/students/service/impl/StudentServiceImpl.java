package com.imsoftware.students.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.imsoftware.students.repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imsoftware.students.domain.StudentDTO;
import com.imsoftware.students.entity.Student;
import com.imsoftware.students.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {
	
	Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	private final StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}

	@Override
	public Collection<StudentDTO> findAll() {
		return studentRepository.findAll().stream().map(new Function<Student, StudentDTO>() {
			@Override
			public StudentDTO apply(Student student) {
				List<String> programmingLanguagesKnowAbout = student.getSubjects().stream()
						.map(pl -> new String(pl.getName())).collect(Collectors.toList());
				return new StudentDTO(student.getName(), programmingLanguagesKnowAbout);
			}

		}).collect(Collectors.toList());
		
	}

	/**
	 * Obtener la lista de todos los estudiantes
	 * Indicar la materia más concurrida existentes en la BD
	 * Indicar si el estudiante cursa o no la materia más concurrida registrado en la BD.
	 */
	@Override
	public Collection<StudentDTO> findAllAndShowIfHaveAPopularSubject() {
		Collection<StudentDTO> studentsDto = findAll();
		 
		// TreeMap para almacenar las materias y sus repeticiones
		TreeMap<String, Integer> subjectsMap = new TreeMap<>();
		for(StudentDTO studentDto : studentsDto) {
			for(String subject : studentDto.getCurrentSubject()) {
				subjectsMap.put(subject, subjectsMap.getOrDefault(subject, 0) + 1);
			}
		}
		//Buscar materia mas concurrida
        String popularSubject = null;
        int maxRepetition = 0;
        for(Map.Entry<String, Integer> entry: subjectsMap.entrySet()) {
        	if(entry.getValue() > maxRepetition ) {
        		maxRepetition = entry.getValue();
        		popularSubject = entry.getKey();
        	}
        }
        log.info("Materia mas concurrida: {}",popularSubject);
        
		for(StudentDTO studentDto : studentsDto) {
			Boolean isPopular = studentDto.getCurrentSubject().contains(popularSubject);
			studentDto.setCurrentPopularSubject(isPopular);
		}
		return studentsDto;
	}
}
