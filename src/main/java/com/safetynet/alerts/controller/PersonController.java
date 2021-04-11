package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.exception.PersonInvalidException;
import com.safetynet.alerts.exception.PersonNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
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

	/**
	 * Create - Create a new person in the repository
	 * 
	 * @return - a new instance of person
	 */
	@PostMapping("/person")
	public ResponseEntity<Person> createPerson(@RequestParam("firstname") final String firstname,
			@RequestParam("lastname") final String lastname, @RequestBody Person person) {

		logger.info("Request = @RequestBody = {}", person.toString());
		Person persistedPerson = new Person();
		try {
			persistedPerson = personService.savePerson(person);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		logger.info("Response = @ResponseBody = {}", persistedPerson.toString());
		return ResponseEntity
				.created(URI.create(String.format("/person?firstname=" + firstname + "&lastname=" + lastname)))
				.body(persistedPerson);
	}

	/**
	 * Search - Search a person in the repository by firstName & lastName
	 * 
	 * @return - an instance of person if found
	 */
	@GetMapping("/person")
	public ResponseEntity<Person> searchPerson(@RequestParam("firstname") final String firstname,
			@RequestParam("lastname") final String lastname) throws Exception {

		Person foundPerson = personService.findPerson(firstname, lastname);
		return new ResponseEntity<Person>(foundPerson, HttpStatus.OK);
	}

	/**
	 * UpdatePerson - Update an existing person in the repository
	 * 
	 * @return - an instance of ResponseEntity<Person>
	 */
	@PutMapping("/person")
	public ResponseEntity<Person> updatePerson(@RequestParam("lastname") final String lastname,
			@RequestParam("firstname") final String firstname, @RequestBody Person person) {
		logger.info("Request = @RequestBody = {}", person.toString());
		ResponseEntity<Person> response = null;
		try {
			Person pers = personService.findPerson(lastname, firstname);
//			if (pers == null) {
//				response = new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
//				throw new PersonIntrouvableException();
//			}
			String fname = person.getFirstName();
			if (fname != null) {
				pers.setFirstName(fname);
			}
			String lname = person.getLastName();
			if (lname != null) {
				pers.setLastName(lname);
			}
			String email = person.getEmail();
			if (email != null) {
				pers.setEmail(email);
			}
			String phone = person.getPhone();
			if (phone != null) {
				pers.setPhone(phone);
			}
			String address = person.getAddress();
			if (address != null) {
				pers.setAddress(address);
			}
			String zip = person.getZip();
			if (zip != null) {
				pers.setZip(zip);
			}
			String city = person.getCity();
			if (city != null) {
				pers.setCity(city);
			}
			logger.info("Response = @ResponseBody = {}", pers.toString());
			personService.savePerson(pers);
			response = ResponseEntity.ok().body(pers);
//					.created(URI.create(String.format("/person?firstname=" + firstname + "&lastname=" + lastname)))
//					.body(pers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
		return response;

	}

	/**
	 * Delete - Delete a person from the repository
	 * 
	 * @param lastname  - the lastname of the person to be deleted
	 * @param firstname - the firstname of the person to be deleted
	 * @throws JsonMappingException
	 * 
	 */
	@DeleteMapping("/person")
	public ResponseEntity<Person> deletePerson(@RequestParam("lastname") final String lastname,
			@RequestParam("firstname") final String firstname)
			throws JsonParseException, JsonMappingException, Exception {
		ResponseEntity<Person> response = null;
		logger.info("Request Delete person firstname {}, lastname {}", firstname, lastname);
		if (lastname.isEmpty() || firstname.isEmpty()) {
			response = new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
			throw new PersonInvalidException("Les champs nom/prénoms ne peuvent être vides");
		}
		if (personService.findPerson(lastname, firstname) == null) {
			response = new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
			throw new PersonNotFoundException(
					"la personne nom:" + lastname + " prénom:" + firstname + " est introuvable");

		}

		personService.deletePerson(lastname, firstname);
		response = new ResponseEntity<Person>(HttpStatus.ACCEPTED);

		return response;
	}

	/**
	 * getPersonInfos() - display person infos : firstname, lastname, age, email,
	 * allergies and medication
	 * 
	 * http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	 * 
	 * @return List<MedicalRecord>
	 * @throws Exception
	 */
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam("lastname") String lastname,
															 @RequestParam("firstname") String firstname) throws Exception {

		logger.info("Request Delete person firstname {}, lastname {}", firstname, lastname);

			List<PersonInfoDTO> searchResult = personService.getPersonInfos(lastname, firstname);
			return new ResponseEntity<List<PersonInfoDTO>>(searchResult, HttpStatus.OK);
	}

	/**
	 * getCommunityEmail - List email addresses for inhabitants of a given city
	 * @param city
	 * @return
	 *
	 * http://localhost:8080/communityEmail?city=<city>
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getCommunityEmail(@RequestParam("city") String city){
		logger.info("Request community {} emails ", city );
		List<String> emails= personService.getCommunityEmail(city);

		return new ResponseEntity<List<String>>(emails, HttpStatus.OK);
	}

}
