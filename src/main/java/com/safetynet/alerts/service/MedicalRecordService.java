package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.safetynet.alerts.exception.MedicalRecordInvalidException;
import com.safetynet.alerts.exception.MedicalRecordNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.utils.Utils;;

@Service
public class MedicalRecordService extends AbstractService {
	private final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private DataRepository dataRepository;

	@Autowired
	private PersonService personService;

	public List<MedicalRecord> findAll() throws Exception {
		return jsonDataReader().getMedicalrecords();
	}

	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord)
			throws JsonParseException, JsonMappingException, IOException {
		return dataRepository.saveMedicalRecord(medicalRecord);
	}

	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord)
			throws JsonParseException, JsonMappingException, IOException {
		if (medicalRecord == null) throw new MedicalRecordInvalidException("Medical record must not be null");
		if (medicalRecord.getLastName() == null || medicalRecord.getFirstName().isEmpty()) {
			throw new MedicalRecordInvalidException("Le champ prenom ne peut être vide");
		}
		if (medicalRecord.getFirstName() == null || medicalRecord.getLastName().isEmpty()) {
			throw new MedicalRecordInvalidException("Le champ nom ne peut être vide");
		}

		logger.info(
				"Updating  person nom: '" + medicalRecord.getFirstName() + "' prénom: '" + medicalRecord.getLastName());
		MedicalRecord updatedMedicalRecord = dataRepository.saveMedicalRecord(medicalRecord);
		logger.info("Updated  to: " + medicalRecord.toString());
		return updatedMedicalRecord;
	}

	public MedicalRecord findMedicalRecord(String firstname, String lastname) {
		MedicalRecord mRecord = null;
		try {
			List<MedicalRecord> medicalRecords = dataRepository.findMedicalRecordByLastNameAndFirstName(lastname, firstname);
			if(medicalRecords.isEmpty())
				throw new MedicalRecordNotFoundException("medical ");
			mRecord = medicalRecords.get(0);
		} catch (JsonMappingException jme) {
			logger.error(jme.getMessage());
		}

		return mRecord;
	}

	public List<MedicalRecord> findMedicalRecords(String lastname, String firstname) throws JsonMappingException {
		return dataRepository.findMedicalRecordByLastNameAndFirstName(lastname, firstname);
	}

	public void deleteMedicalRecord(String lastname, String firstname)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("Deleting the person");
		MedicalRecord medicalRecord = dataRepository.findMedicalRecordByLastNameAndFirstName(lastname, firstname)
				.get(0);
		dataRepository.deleteMedicalRecord(medicalRecord);

	}

	/**
	 * convertMedicalRecordsToPersonInfos - convert a list of MedicalRecord into
	 * PersonInfo
	 * 
	 * @param mrs
	 * @return
	 */
	public List<PersonInfoDTO> convertMedicalRecordsToPersonInfos(List<MedicalRecord> mrs) {
		List<PersonInfoDTO> personInfos = new ArrayList<>();

		mrs.forEach((mr) -> {
			try {
				Person pers = personService.findPerson(mr.getLastName(),mr.getFirstName());
				PersonInfoDTO personInfo = new PersonInfoDTO(mr.getFirstName(), mr.getLastName(),
						Utils.calculateAge(mr.getBirthdate()), pers.getEmail(), pers.getPhone(), pers.getAddress(),
						mr.getAllergies(), mr.getMedications());
				personInfos.add(personInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		});

		return personInfos;
	}

	public PersonInfoDTO convertMedicalRecordToPersonInfo(MedicalRecord mr){
		PersonInfoDTO personInfo=null;
		try {
			Person pers = personService.findPerson(mr.getLastName(), mr.getFirstName());
			personInfo = new PersonInfoDTO(mr.getFirstName(), mr.getLastName(),
					Utils.calculateAge(mr.getBirthdate()), pers.getEmail(), pers.getPhone(), pers.getAddress(),
					mr.getAllergies(), mr.getMedications());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return personInfo;
	}

	/**
	 * retrieveMedicalRecordFromPersons - Retrieve medicalRecords for a list of
	 * persons
	 * 
	 * @param persons
	 * @return
	 */
	/*public List<MedicalRecord> retrieveMedicalRecordFromPersons(List<Person> persons) {
		List<MedicalRecord> mrs = new ArrayList<>();

		persons.forEach(pers -> {
			mrs.add(getMedicalRecordForAPerson(pers));
		});

		return mrs;
	}*/

	/**
	 * getMedicalRecordForAPerson - Retrieve a person's medical record
	 * 
	 * @param person
	 * @return
	 */
	public MedicalRecord getMedicalRecordForAPerson(Person person) {
		MedicalRecord personMR = new MedicalRecord();
		for (MedicalRecord mr : dataRepository.findAllMedicalRecord()) {
			if (mr.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& mr.getLastName().equalsIgnoreCase(person.getLastName())) {
				personMR = mr;
			}
		}
		return personMR;
	}

	/**
	 *
	 * @return
	 */
	//public PersonsCoveredByStation convertMedicalRecordToPersonsCovered(MedicalRecord mr){

	//	return null;
	//}



}
