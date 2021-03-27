package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;

@Service
public class FirestationService extends AbstractService {

	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

	@Autowired
	private MedicalRecordService medicalRecordService;

	public List<Firestation> findAll() throws Exception {
		return jsonDataReader().getFirestations();
	}

	public Firestation saveFirestation(Firestation firestation)
			throws JsonParseException, JsonMappingException, IOException {
		return dataRepository.saveFirestation(firestation);
	}

	public Firestation updateFirestation(Firestation firestation)
			throws JsonParseException, JsonMappingException, IOException {
		return dataRepository.saveFirestation(firestation);

	}

	public void deleteFirestation(Integer firestationId) throws JsonParseException, JsonMappingException, IOException {
		dataRepository.deleteFirestation(firestationId);
	}

	public Firestation findFirestation(Integer station) throws JsonMappingException {
		return dataRepository.findFirestation(station);
	}

	/**
	 * getResidentPhone - Given a station Id returns list of residents
	 * 
	 * @param station
	 * @return
	 * @throws JsonMappingException
	 */
	public Map<String, List<String>> getResidentPhone(Integer station) throws JsonMappingException {
		String address = dataRepository.findFirestation(station).getAddress();
		Map<String, List<String>> lesResidents = new HashMap<String, List<String>>();

		List<String> personsPhone = dataRepository.getPhonePersonsAtAddress(address);
		lesResidents.put(address, personsPhone);

		return lesResidents;
	}

	/**
	 * getPersonsCoveredByStation - Given a station id, return a List (Person, Age)
	 * of persons covered by the station
	 * 
	 * @param station
	 * @return
	 * @throws JsonMappingException
	 */
	public List<PersonInfoDTO> getPersonsCoveredByStation(Integer station) throws JsonMappingException {
		String address = dataRepository.findFirestation(station).getAddress();
		List<PersonInfoDTO> lesResidents = new ArrayList<>();

		List<Person> personsAtAddress = dataRepository.getPersonsAtAddress(address);

		return medicalRecordService.convertMedicalRecordsToPersonInfos(
				medicalRecordService.retrieveMedicalRecordFromPersons(personsAtAddress));
	}

}
