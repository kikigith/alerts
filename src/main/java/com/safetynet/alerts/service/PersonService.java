package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;

@Service
public class PersonService {

	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

	public List<Person> findAll() throws Exception {

		return dataRepository.findAllPerson();

	}

	public Person findPerson(String firstName, String lastName) throws Exception {

		return dataRepository.findPersonByLastNameAndFirstName(lastName, firstName).get(0);
	}

	public Person savePerson(Person pers) throws Exception {

		return dataRepository.savePerson(pers);
	}

	public Person updatePerson(Person person) throws JsonParseException, JsonMappingException, IOException {

		return dataRepository.savePerson(person);
	}

	public void deletePerson(String lastname, String firstname)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("Deleting the person");
		Person person = dataRepository.findPersonByLastNameAndFirstName(lastname, firstname).get(0);
		dataRepository.deletePerson(person);

	}

}
