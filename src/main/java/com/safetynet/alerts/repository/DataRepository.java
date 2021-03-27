package com.safetynet.alerts.repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

@Repository
public class DataRepository {

	@Value("${json.file.location}")
	Resource jsonFile;

	private void serializeDataToFile(ObjectMapper objectMapper, AlertsData data) {
		try (FileOutputStream fos = new FileOutputStream(jsonFile.getFile())) {
			objectMapper.writeValue(fos, data);
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * jsonDataReader - Deserialize json root node from the json data repository
	 * into AlertsData class object.
	 * 
	 * @return
	 * @throws JsonMappingException
	 */
	private AlertsData jsonDataReader() throws JsonMappingException {
		ObjectMapper objectMapper = new ObjectMapper();
		AlertsData aData = null;
		try {
			aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aData;
	}

	/**
	 * findAllPerson - Deserialize all Person objects from the json data repository
	 * into a List
	 * 
	 * @return
	 */
	public List<Person> findAllPerson() {
		try {
			return jsonDataReader().getPersons();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * findAllFirestation - Deserialize all firestation objects from the json data
	 * repository into a List
	 * 
	 * @return
	 */
	public List<Firestation> findAllFirestation() {
		try {
			return jsonDataReader().getFirestations();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * findAllMedicalRecord - Deserialize all MedicalRecord objects from the json
	 * data repository into a List
	 * 
	 * @return
	 */
	public List<MedicalRecord> findAllMedicalRecord() {
		try {
			return jsonDataReader().getMedicalrecords();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * addPerson - Method that adds|updates a person into the json data repository
	 * 
	 * @param pers
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Person savePerson(Person pers) throws JsonParseException, JsonMappingException, IOException {
		Person person = null;
		ObjectMapper objectMapper = new ObjectMapper();

		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
		List<Person> persons = findAllPerson();
		if (persons.contains(pers)) {// Update
			persons.get(persons.indexOf(pers)).setFirstName(pers.getFirstName());
			persons.get(persons.indexOf(pers)).setLastName(pers.getLastName());
			persons.get(persons.indexOf(pers)).setAddress(pers.getAddress());
			persons.get(persons.indexOf(pers)).setEmail(pers.getEmail());
			persons.get(persons.indexOf(pers)).setPhone(pers.getPhone());
			persons.get(persons.indexOf(pers)).setCity(pers.getCity());
			persons.get(persons.indexOf(pers)).setZip(pers.getZip());
			person = persons.get(persons.indexOf(pers));
		} else {// New person extend the list
			person = new Person(pers.getFirstName(), pers.getLastName(), pers.getAddress(), pers.getCity(),
					pers.getZip(), pers.getPhone(), pers.getEmail());
			persons.add(person);
		}
		aData.setPersons(persons);
		serializeDataToFile(objectMapper, aData);
		return person;
	}

	/**
	 * saveFirestation - Method that adds new Firestation into the json data
	 * repository
	 * 
	 * @param fs
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Firestation saveFirestation(Firestation fs) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Firestation firestation = null;

		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
		List<Firestation> firestations = findAllFirestation();
		if (firestations.contains(fs)) {// Update
			firestations.get(firestations.indexOf(fs)).setAddress(fs.getAddress());
			firestations.get(firestations.indexOf(fs)).setStation(fs.getStation());
			firestation = firestations.get(firestations.indexOf(fs));
		} else {// New
			firestation = new Firestation();
			firestation.setAddress(fs.getAddress());
			firestation.setStation(fs.getStation());
			firestations.add(firestation);
		}
		aData.setFirestations(firestations);
		serializeDataToFile(objectMapper, aData);

		return firestation;

	}

	/**
	 * saveMedicalRecord - Method that adds|updates a new MedicalRecord into the
	 * json data repository
	 * 
	 * @param mr
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public MedicalRecord saveMedicalRecord(MedicalRecord mr)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		MedicalRecord medicalRecord = null;

		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);
		List<MedicalRecord> medicalRecords = findAllMedicalRecord();
		if (medicalRecords.contains(mr)) {// Update
			medicalRecords.get(medicalRecords.indexOf(mr)).setFirstName(mr.getFirstName());
			medicalRecords.get(medicalRecords.indexOf(mr)).setLastName(mr.getLastName());
			medicalRecords.get(medicalRecords.indexOf(mr)).setBirthdate(mr.getBirthdate());
			medicalRecords.get(medicalRecords.indexOf(mr)).setAllergies(mr.getAllergies());
			medicalRecords.get(medicalRecords.indexOf(mr)).setMedications(mr.getMedications());
			medicalRecord = medicalRecords.get(medicalRecords.indexOf(mr));
		} else {// New
			medicalRecord = new MedicalRecord();
			medicalRecord.setFirstName(mr.getFirstName());
			medicalRecord.setLastName(mr.getLastName());
			medicalRecord.setBirthdate(mr.getBirthdate());
			medicalRecord.setAllergies(mr.getAllergies());
			medicalRecord.setMedications(mr.getMedications());
			medicalRecords.add(medicalRecord);
		}
		aData.setMedicalrecords(medicalRecords);

		serializeDataToFile(objectMapper, aData);

		return medicalRecord;

	}

	/**
	 * findPersonByLastNameAndFirstName - search a person base on lastname and
	 * firstname
	 * 
	 * @param lastname
	 * @param firstname
	 * @return
	 * @throws JsonMappingException
	 */
	public List<Person> findPersonByLastNameAndFirstName(String lastname, String firstname)
			throws JsonMappingException {
		List<Person> foundPersons = new ArrayList<Person>();
		List<Person> allPersons = jsonDataReader().getPersons();

		for (Person person : allPersons) {
			if (person.getLastName().equalsIgnoreCase(lastname) && person.getFirstName().equalsIgnoreCase(firstname))
				foundPersons.add(person);
		}
		return foundPersons;
	}

	public void deletePerson(Person person) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);

		List<Person> persons = jsonDataReader().getPersons();
		if (persons.contains(person)) {
			persons.remove(persons.indexOf(person));
			aData.setPersons(persons);
		}

		serializeDataToFile(objectMapper, aData);

	}

	public void deleteFirestation(Integer firestationId) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Firestation firestation = new Firestation(firestationId);
		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);

		List<Firestation> firestations = jsonDataReader().getFirestations();
		if (firestations.contains(firestation)) {
			firestations.remove(firestations.indexOf(firestation));
			aData.setFirestations(firestations);
		}

		serializeDataToFile(objectMapper, aData);
	}

	/**
	 * personAlreadyExists - Check if a given person already exists in json
	 * repository
	 * 
	 * @param lastname
	 * @param firstname
	 * @return
	 */
	public boolean personAlreadyExists(String lastname, String firstname) {
		try {
			if (findPersonByLastNameAndFirstName(lastname, firstname).size() > 0) {
				return true;
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Firestation findFirestation(Integer station) throws JsonMappingException {
		List<Firestation> firestations = jsonDataReader().getFirestations();
		Firestation foundFirestation = null;
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == station)
				foundFirestation = firestation;
		}
		return foundFirestation;
	}

	public List<MedicalRecord> findMedicalRecordByLastNameAndFirstName(String lastname, String firstname)
			throws JsonMappingException {
		List<MedicalRecord> foundMedicalRecords = new ArrayList<MedicalRecord>();
		List<MedicalRecord> allMedicalRecords = jsonDataReader().getMedicalrecords();

		for (MedicalRecord medicalRecord : allMedicalRecords) {
			if (medicalRecord.getLastName().equalsIgnoreCase(lastname)
					&& medicalRecord.getFirstName().equalsIgnoreCase(firstname))
				foundMedicalRecords.add(medicalRecord);
		}
		return foundMedicalRecords;
	}

	public void deleteMedicalRecord(MedicalRecord medicalRecord)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		AlertsData aData = objectMapper.readValue(jsonFile.getFile(), AlertsData.class);

		List<MedicalRecord> medicalRecords = jsonDataReader().getMedicalrecords();
		if (medicalRecords.contains(medicalRecord)) {
			medicalRecords.remove(medicalRecords.indexOf(medicalRecord));
		}
		aData.setMedicalrecords(medicalRecords);

		serializeDataToFile(objectMapper, aData);

	}

	public List<String> getPhonePersonsAtAddress(String address) throws JsonMappingException {
		List<String> personsPhone = new ArrayList<String>();
		for (Person person : jsonDataReader().getPersons()) {
			if (person.getAddress().equalsIgnoreCase(address)) {
				personsPhone.add(person.getPhone());
			}
		}
		return personsPhone;
	}

	public List<Person> getPersonsAtAddress(String address) throws JsonMappingException {
		List<Person> personsPhone = new ArrayList<Person>();
		for (Person person : jsonDataReader().getPersons()) {
			if (person.getAddress().equalsIgnoreCase(address)) {
				personsPhone.add(person);
			}
		}
		return personsPhone;
	}

}
