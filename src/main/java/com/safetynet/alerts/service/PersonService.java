package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.exception.CityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.exception.PersonInvalidException;
import com.safetynet.alerts.exception.PersonNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;

@Service
public class PersonService {

	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

	@Autowired
	private MedicalRecordService medicalRecordService;

	public List<Person> findAll() throws Exception {

		return dataRepository.findAllPerson();

	}

	public Person findPerson(String lastName, String firstName) throws Exception {
		List<Person> foundPersons = dataRepository.findPersonByLastNameAndFirstName(lastName, firstName);
		if (foundPersons.size() == 0) {
			throw new PersonNotFoundException(
					"La person nom:'" + firstName + "' Prénom:'" + lastName + ", n'existe pas");
		}
		return foundPersons.get(0);
	}

	public Person savePerson(Person pers) throws Exception {
		if (pers.getFirstName().isEmpty() || pers.getLastName().isEmpty()) {
			throw new PersonInvalidException("Le champ nom/prénom ne peut être vide");
		}
		return dataRepository.savePerson(pers);
	}

	public Person updatePerson(Person person) throws JsonParseException, JsonMappingException, Exception {
//		if (person.getFirstName().isEmpty() || person.getLastName().isEmpty()) {
//			throw new InvalidPersonException("Le champ nom/prénom ne peut être vide");
//		}
//		if (findPerson(person.getFirstName(), person.getLastName()) == null) {
//			throw new PersonIntrouvableException("La personne de nom:" + person.getLastName() + " et prénom:"
//					+ person.getFirstName() + "n'existe pas");
//		}
		logger.info("Updating  person nom: '" + person.getFirstName() + "' prénom: '" + person.getLastName());
		Person updatedPerson = dataRepository.savePerson(person);
		logger.info("Updated  to: " + person.toString());
		return updatedPerson;
	}

	public void deletePerson(String lastname, String firstname)
			throws JsonParseException, JsonMappingException, Exception {
//		if (lastname.isEmpty() || firstname.isEmpty()) {
//			throw new InvalidPersonException("Le champ nom/prénom ne peut être vide");
//		}
//		if (findPerson(firstname, lastname) == null) {
//			throw new PersonIntrouvableException(
//					"La personne de nom:" + lastname + " et prénom:" + firstname + "n'existe pas");
//		}
		logger.info("Deleting the person");
		Person person = findPerson(lastname, firstname);
//		Person person = dataRepository.findPersonByLastNameAndFirstName(lastname, firstname).get(0);
		dataRepository.deletePerson(person);
		logger.info("Person [nom:" + firstname + " prénom:" + lastname + "] successfully deleted");

	}

	public List<PersonInfoDTO> getPersonInfos(String lastname, String firstname) {
		List<MedicalRecord> mrs = new ArrayList<>();

		try {
			mrs = medicalRecordService.findMedicalRecords(lastname, firstname);

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return medicalRecordService.convertMedicalRecordsToPersonInfos(mrs);
	}

	/**
	 * findPersonByAddress - Retrieve a list of persons living at a given address
	 * @param address
	 * @return
	 */
	public List<Person> findPersonByAddress(String address) {
		List<Person> personsAtAddress = new ArrayList<>();
		dataRepository.findAllPerson().forEach(pers -> {
			if (pers.getAddress().equalsIgnoreCase(address)) {
				personsAtAddress.add(pers);
			}
		});
		return personsAtAddress;
	}

	/**
	 * getCommunityEmail - Returns a list of city inhabitant email addresses
	 *
	 * @param city
	 * @return
	 */
	public List<String> getCommunityEmail(String city) {
		List<String> cityInhabitantsEmails=new ArrayList<>();

		dataRepository.findAllPerson().forEach(pers -> {
			if (pers.getCity().equalsIgnoreCase(city)) {
				cityInhabitantsEmails.add(pers.getEmail());
			}
		});
		if(cityInhabitantsEmails.size()==0){
			throw new CityNotFoundException("La ville "+city +" est introuvable");
		}

		return cityInhabitantsEmails;
	}

}
