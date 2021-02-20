package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;;

@Service
public class MedicalRecordService extends AbstractService {
	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

	public List<MedicalRecord> findAll() throws Exception {
		return jsonDataReader().getMedicalrecords();
	}

	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord)
			throws JsonParseException, JsonMappingException, IOException {
		return dataRepository.saveMedicalRecord(medicalRecord);
	}

	public MedicalRecord findMedicalRecord(String firstname, String lastname) throws JsonMappingException {
		return dataRepository.findMedicalRecordByLastNameAndFirstName(lastname, firstname).get(0);
	}

	public void deleteMedicalRecord(String lastname, String firstname)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("Deleting the person");
		MedicalRecord medicalRecord = dataRepository.findMedicalRecordByLastNameAndFirstName(lastname, firstname)
				.get(0);
		dataRepository.deleteMedicalRecord(medicalRecord);

	}
}
