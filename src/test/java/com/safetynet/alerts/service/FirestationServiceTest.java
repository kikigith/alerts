package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.exception.FirestationInvalidException;
import com.safetynet.alerts.exception.FirestationNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

	@Mock
	DataRepository dataRepository;

	@Mock
	private MedicalRecordService medicalRecordService;

	@InjectMocks
	private FirestationService firestationService;



	List<PersonInfoDTO> personInfos;
	PersonInfoDTO pi1, pi2;
	List<Person> persons;
	Person p1, p2;

	List<MedicalRecord> medRecs;
	MedicalRecord mr1,mr2;
	String address;
	List<String> phones;

	Firestation fs;

	@BeforeEach
	public void initTest() throws Exception{
		address = "Rue 69, cotonou";
		persons = new ArrayList<>();
        p1=new Person();
		p1.setFirstName("Aline");
		p1.setLastName("Dupont");
		p1.setAddress(address);
		p2 = new Person();
		p2.setFirstName("Aline");
		p2.setLastName("Dupond");
		p2.setAddress(address);
		persons.add(p1);
		persons.add(p2);
		personInfos = new ArrayList<>();
		pi1 = new PersonInfoDTO();
		pi1.setNom("Aline");
		pi1.setPrenom("Dupont");
		pi1.setAge(20);
		pi2 = new PersonInfoDTO();
		pi2.setNom("Aline");
		pi2.setPrenom("Dupond");
		pi2.setAge(10);
		personInfos.add(pi1);
		personInfos.add(pi2);

		mr1=new MedicalRecord();
		mr1.setFirstName("Aline");
		mr1.setLastName("Dupont");
		mr2=new MedicalRecord();
		mr2.setFirstName("Aline");
		mr2.setLastName("Dupont");

		medRecs=new ArrayList<>();
		medRecs.add(mr1);
		medRecs.add(mr2);


		fs=new Firestation();
		fs.setStation(1);
		fs.setAddress(address);

		phones = new ArrayList<>();
		phones.add("57 985 267");
		phones.add("78 908 526");



	}

	@Test
	public void testSaveFirestation() throws Exception {
		Firestation firestation = new Firestation();

		when(dataRepository.saveFirestation(any(Firestation.class))).thenReturn(firestation);

		Firestation savedFirestation = firestationService.saveFirestation(new Firestation());

		verify(dataRepository).saveFirestation(any(Firestation.class));

		assertThat(savedFirestation).isNotNull();

	}

	@Test
	void given_A_station_ID_should_be_updated() throws Exception {
		//Firestation firestation = new Firestation();
        //when(dataRepository.saveFirestation(any(Firestation.class))).thenReturn(fs);
		when(dataRepository.findFirestation(1)).thenReturn(fs);
		firestationService.updateFirestation(fs);

		verify(dataRepository).saveFirestation(any());
	}

	@Test
	void given_A_station_ID_update_should_raise_exception_when_station_does_not_exist() throws Exception{
		when(dataRepository.findFirestation(2)).thenReturn(null);
		Firestation firestation = new Firestation();
		firestation.setStation(2);

		Assertions.assertThrows(FirestationNotFoundException.class, ()-> {
			firestationService.updateFirestation(firestation);
		});
	}

	@Test
	void given_An_invalid_station_ID_update_should_raise_exception() throws Exception{
		Firestation firestation = new Firestation();
		firestation.setStation(0);

		Assertions.assertThrows(FirestationInvalidException.class, ()-> {
			firestationService.updateFirestation(firestation);
		});
	}

	@Test
	void given_A_station_ID_should_Delete_Firestation_if_exists() throws Exception {
		when(dataRepository.findFirestation(1)).thenReturn(fs);
		firestationService.deleteFirestation(1);

		verify(dataRepository, times(1)).deleteFirestation(any());
	}



	@Test
	void given_A_Station_Id_Return_A_List_of_PersonWithAge_covered() throws Exception {// test mal architecturé
		when(dataRepository.getPersonsAtAddress(any())).thenReturn(persons);
//when(dataRepository.findAllMedicalRecord()).thenReturn(medRecs);
		when(dataRepository.findFirestation(1)).thenReturn(fs);
		when(medicalRecordService.getMedicalRecordForAPerson(p1)).thenReturn(mr1);
		when(medicalRecordService.getMedicalRecordForAPerson(p2)).thenReturn(mr2);

//when(medicalRecordService.convertMedicalRecordsToPersonInfos(medRecs)).thenReturn(personInfos);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr1)).thenReturn(pi1);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr2)).thenReturn(pi2);


		PersonsCoveredByStation personsCovered = firestationService.getPersonsCoveredByStation(1);

		assertThat(personsCovered.getPersonsCovered().get(0).equals(personInfos.get(0)));
//verify(medicalRecordService).retrieveMedicalRecordFromPersons(any());
	}

	@Test
	void given_A_Station_Id_Should_Return_Resident_Phones() throws Exception{
		//when(dataRepository.findFirestation(1).getAddress()).thenReturn(address);
		when(dataRepository.findFirestation(1)).thenReturn(fs);
		when(dataRepository.getPersonsPhoneNumberAtAddress(address)).thenReturn(phones);

		List<String> residentPhones=firestationService.getResidentPhone(fs.getStation());

		assertThat(residentPhones.get(0)).isSameAs(phones.get(0));
	}
}
