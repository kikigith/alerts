package com.safetynet.alerts.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.Person;

@Service
public class PersonService {

	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	public List<Person> findAll() throws Exception {

		List<Person> persons = new ArrayList<>();
//		jsonDataReader().getPersons().forEach(persJson -> {
//			String firstName = persJson.getFirstName().toString();
//			String lastName = persJson.getLastName().toString();
//			String address = persJson.getAddress().toString();
//			String city = persJson.getCity().toString();
//			String zip = persJson.getZip().toString();
//			String phone = persJson.getPhone().toString();
//			String email = persJson.getEmail().toString();
//
//			Person person = new Person(firstName, lastName, address, city, zip, phone, email);
//			persons.add(person);
//		});

		return jsonDataReader().getPersons();

	}

	public Person findPerson(String firstName, String lastName) throws Exception {
		Person person = null;

		for (Person pers : jsonDataReader().getPersons()) {
			if (pers.getFirstName().equalsIgnoreCase(firstName) && pers.getLastName().equalsIgnoreCase(lastName)) {
				person = pers;
			}

		}
		return person;
	}

	public Person savePerson(String fname, String lname, String address, String city, String zip, String phone,
			String email) throws Exception {
		Person person = new Person(fname, lname, address, city, zip, phone, email);
		ObjectMapper objectMapper = new ObjectMapper();
		Resource resource = new ClassPathResource("json/data.json");
		InputStream input = resource.getInputStream();
		AlertsData aData = objectMapper.readValue(resource.getFile(), AlertsData.class);

		return null;
	}

	public Person updatePerson() {

		return null;
	}

	public void deletePerson() {
	}

	public AlertsData jsonDataReader() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Resource resource = new ClassPathResource("json/data.json");
		InputStream input = resource.getInputStream();
		AlertsData aData = objectMapper.readValue(resource.getFile(), AlertsData.class);

		return aData;
	}
}
