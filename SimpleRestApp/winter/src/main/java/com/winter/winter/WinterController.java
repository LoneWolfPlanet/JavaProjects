package com.winter.winter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.Employee;

@RestController
public class WinterController {
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@GetMapping("/getEmployee")
	public Employee getEmployee() {
		
		Employee employee = new Employee("Antet", "Developer");
		return employee;
	}
}
