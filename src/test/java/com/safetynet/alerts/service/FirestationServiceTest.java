package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.exception.FirestationInvalidException;
import com.safetynet.alerts.exception.FirestationNotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildrenCoveredDTO;
import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
import com.safetynet.alerts.model.dto.StationCoverageDTO;
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

	StationCoverageDTO stationCoverage1, stationCoverage2;
	List<StationCoverageDTO> stationsCoverage;
	Firestation station1, station2;
	Person person1, person2, person3, person4;
	List<Person> personsCovered1, personsCovered2;

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

		station1 = new Firestation();
		station1.setStation(1);
		station1.setAddress("24 rue Toho");
		station2 = new Firestation();
		station2.setStation(2);
		station2.setAddress("98 station Balneaire");

		stationCoverage1 = new StationCoverageDTO();
		stationCoverage1.setAddress("24 rue Toho");
		person1 = new Person();
		person1.setFirstName("Alice");
		person2 = new Person();
		person2.setFirstName("Dossa");
		personsCovered1  = new ArrayList<>();
		personsCovered1.add(person1);
		personsCovered1.add(person2);
		stationCoverage1.setPersonsCovered(personsCovered1);

		stationCoverage2 = new StationCoverageDTO();
		stationCoverage2.setAddress("98 station Balneaire");
		person3 = new Person();
		person3.setFirstName("Victoire");
		person4 = new Person();
		person4.setFirstName("Dossa");
		personsCovered2 = new ArrayList<>();
		personsCovered2.add(person3);
		personsCovered2.add(person4);
		stationCoverage2.setPersonsCovered(personsCovered2);

		stationsCoverage = new ArrayList<>();
		stationsCoverage.add(stationCoverage1);
		stationsCoverage.add(stationCoverage2);



	}

	@Test
	public void given_A_Station_should_be_Saved() throws Exception {
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


	@Test
	void given_an_Address_Should_return_children_Covered() throws Exception {
		when(dataRepository.getPersonsAtAddress(address)).thenReturn(persons);
		when(medicalRecordService.getMedicalRecordForAPerson(p1)).thenReturn(mr1);
		when(medicalRecordService.getMedicalRecordForAPerson(p2)).thenReturn(mr2);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr1)).thenReturn(pi1);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr2)).thenReturn(pi2);

		ChildrenCoveredDTO childrenCovered=firestationService.getChildrenCovered(fs.getAddress());

		assertThat(childrenCovered.getEnfants().get(0).getPrenom()).isSameAs(pi2.getPrenom());
	}


	@Test
	void given_an_Address_should_return_residents_and_station() throws Exception{
		when(dataRepository.getPersonsAtAddress(address)).thenReturn(persons);
		when(dataRepository.findFirestationByAddress(address)).thenReturn(fs);
		when(medicalRecordService.getMedicalRecordForAPerson(p1)).thenReturn(mr1);
		when(medicalRecordService.getMedicalRecordForAPerson(p2)).thenReturn(mr2);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr1)).thenReturn(pi1);
		when(medicalRecordService.convertMedicalRecordToPersonInfo(mr2)).thenReturn(pi2);

		PersonsCoveredByStation personsCoveredByStation= firestationService.getStationAndPersonsCoveredAtAddress(address);
		assertThat(personsCoveredByStation.getPersonsCovered().get(0).getPrenom()).isSameAs(pi2.getPrenom());
	}


	@Test
	void given_A_stationID_should_return_covered_homes () throws Exception{
		when(dataRepository.findFirestation(1)).thenReturn(station1);
		when(dataRepository.getPersonsAtAddress(station1.getAddress())).thenReturn(personsCovered1);

		StationCoverageDTO stationCoverage=firestationService.generateStationCoverage(1);
		assertThat(stationCoverage.getPersonsCovered().get(0).getFirstName()).isSameAs(person1.getFirstName());

	}

	@Test
	void given_A_list_of_stationID_should_return_covered_homes () throws Exception{
		when(dataRepository.findFirestation(1)).thenReturn(station1);
		when(dataRepository.getPersonsAtAddress(station1.getAddress())).thenReturn(personsCovered1);
		when(dataRepository.findFirestation(2)).thenReturn(station2);
		when(dataRepository.getPersonsAtAddress(station2.getAddress())).thenReturn(personsCovered2);
		List<Integer> stations = new ArrayList<>();
		stations.add(1);
		stations.add(2);
		List<StationCoverageDTO> stationsCoverage=firestationService.getStationsCoverage(stations);

		assertThat(stationsCoverage.get(0).getPersonsCovered().get(0).getFirstName()).isSameAs(person1.getFirstName());
		assertThat(stationsCoverage.get(1).getPersonsCovered().get(0).getFirstName()).isSameAs(person3.getFirstName());
	}

}
