package com.safetynet.alerts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.safetynet.alerts.exception.FirestationInvalidException;
import com.safetynet.alerts.exception.FirestationNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.dto.ChildrenCoveredDTO;
import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
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

		if(firestation.getStation()<=0 || (firestation.getStation()%1==0)){
			throw new FirestationInvalidException("Vous devez préciser un ID pour la station à mettre à jour");
		}
		if(findFirestation(firestation.getStation())==null){
			throw new FirestationNotFoundException("La station d'ID "+ firestation.getStation()+" n'existe pas");
		}

		return dataRepository.saveFirestation(firestation);

	}

	public void deleteFirestation(Integer firestationId) throws JsonParseException, JsonMappingException, IOException {
		if(findFirestation(firestationId)==null){
			throw new FirestationNotFoundException("La station d'ID "+ firestationId+" n'existe pas");
		}
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
	public List<String> getResidentPhone(Integer station) throws JsonMappingException {
		String address = dataRepository.findFirestation(station).getAddress();
		List<String> personsPhone = dataRepository.getPersonsPhoneNumberAtAddress(address);
		return personsPhone;
	}

	/**
	 * getPersonsCoveredByStation - Given a station id, return a List (Person, Age)
	 * of persons covered by the station
	 * 
	 * @param station
	 * @return
	 * @throws JsonMappingException
	 *
	 */
	public PersonsCoveredByStation getPersonsCoveredByStation(Integer station) throws JsonMappingException {
		PersonsCoveredByStation personsCoveredByStation=new PersonsCoveredByStation();
		personsCoveredByStation.setStationID(station);

		Firestation firestation=dataRepository.findFirestation(station);

		AtomicInteger adultCount= new AtomicInteger();
		AtomicInteger childCount= new AtomicInteger();
		List<Person> personsAtAddress = dataRepository.getPersonsAtAddress(firestation.getAddress());
		List<PersonInfoDTO> personInfos =new ArrayList<>();
				personsAtAddress.forEach(person -> {
			MedicalRecord recordForAPerson = medicalRecordService.getMedicalRecordForAPerson(person);
			PersonInfoDTO personInfoDTO = medicalRecordService.convertMedicalRecordToPersonInfo(recordForAPerson);
			if(personInfoDTO.isChild()){
				childCount.getAndIncrement();
			}else{
				adultCount.getAndIncrement();
			}
			personInfos.add(personInfoDTO);
		});




		/*List<MedicalRecord> mrs=new ArrayList<>();

		personsAtAddress.forEach(pers->{
			mrs.add(medicalRecordService.getMedicalRecordForAPerson(pers));
		});
		List<PersonInfoDTO> personInfos = medicalRecordService.convertMedicalRecordsToPersonInfos(mrs);
		for (PersonInfoDTO pInfo:personInfos) {
			if(pInfo.isChild()){
				childCount.getAndIncrement();
			}else{
                adultCount.getAndIncrement();
			}
		}*/
		personsCoveredByStation.setNbEnfants(childCount.get());
		personsCoveredByStation.setNbAdult(adultCount.get());
		personsCoveredByStation.setPersonsCovered(personInfos);
		return personsCoveredByStation;
	}

	/**
	 *
	 * @param address
	 * @return
	 * @throws JsonMappingException
	 * endpoint=http://localhost:8080/childAlert?address=<address>
	 */
	public ChildrenCoveredDTO getChildrenCovered(String address) throws JsonMappingException{
		   ChildrenCoveredDTO chCovered=new ChildrenCoveredDTO();

		List<Person> personsAtAddress = dataRepository.getPersonsAtAddress(address);
		List<PersonInfoDTO> enfants=new ArrayList<>();
		List<PersonInfoDTO> others=new ArrayList<>();

		personsAtAddress.forEach(pers->{
			PersonInfoDTO pInfo=medicalRecordService.convertMedicalRecordToPersonInfo(medicalRecordService.getMedicalRecordForAPerson(pers));
			if(pInfo.isChild()){
				enfants.add(pInfo);
			}else{
				others.add(pInfo);
			}
		});
		chCovered.setEnfants(enfants);
		chCovered.setOthers(others);

		return chCovered;
	}


	/**
	 *
	 * @param address
	 * @return
	 * @throws JsonMappingException
	 */
	public PersonsCoveredByStation getStationAndPersonsCoveredAtAddress(String address) throws JsonMappingException{
		PersonsCoveredByStation personsCoveredByStation=new PersonsCoveredByStation();

		Firestation firestation=getStationCoveringAddress(address);

		List<Person> personsAtAddress = dataRepository.getPersonsAtAddress(address);
		List<PersonInfoDTO> personsCovered=new ArrayList<>();

		personsAtAddress.forEach(pers->{
			PersonInfoDTO pInfo=medicalRecordService.convertMedicalRecordToPersonInfo(medicalRecordService.getMedicalRecordForAPerson(pers));
			personsCovered.add(pInfo);
		});

		personsCoveredByStation.setStationID(firestation.getStation());
		personsCoveredByStation.setPersonsCovered(personsCovered);

		return personsCoveredByStation;
	}


	public Firestation getStationCoveringAddress(String address) throws JsonMappingException {
		return dataRepository.findFirestationByAddress(address);
	}
}
