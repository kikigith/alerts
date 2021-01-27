package com.safetynet.alerts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;

	private final Logger logger = LoggerFactory.getLogger(PersonController.class);

	/**
	 * Read - Get all persons from the repository
	 * 
	 * @return an iterable list of persons
	 */
	@GetMapping("/persons")
	public List<Person> getPersons() throws Exception {
		logger.info("GET /persons called");
		return personService.findAll();
	}

	@GetMapping("/tests")
	public String test() {
		return "hello";
	}

}
