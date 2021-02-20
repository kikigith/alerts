package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.DataRepository;

@Service
public class FirestationService extends AbstractService {

	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

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
}
