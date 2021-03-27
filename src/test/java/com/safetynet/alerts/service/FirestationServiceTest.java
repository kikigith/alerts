package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

	@InjectMocks
	private FirestationService firestationService;

	@InjectMocks
	private MedicalRecordService medicalRecordService = new MedicalRecordService();

	List<PersonInfoDTO> personInfos;
	String address;
	List<String> phones;

	@BeforeEach
	public void initTest() {

		personInfos = new ArrayList<>();
		PersonInfoDTO pi1 = new PersonInfoDTO();
		pi1.setNom("Aline");
		pi1.setPrenom("Dupont");
		pi1.setAge(20);
		PersonInfoDTO pi2 = new PersonInfoDTO();
		pi2.setNom("Aline");
		pi2.setPrenom("Dupond");
		personInfos.add(pi1);
		personInfos.add(pi2);

		address = "Rue 69, cotonou";

		phones = new ArrayList<>();

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
	void testUpdateFirestation() throws Exception {
		Firestation firestation = new Firestation();

		firestationService.updateFirestation(firestation);

		verify(dataRepository).saveFirestation(any());
	}

	@Test
	void testDeleteFirestation() throws Exception {
		List<Firestation> firestations = new ArrayList<Firestation>();
		Firestation frs = new Firestation(1);

//		when(dataRepository.findPersonByLastNameAndFirstName(any(), any())).thenReturn(persons);

		firestationService.deleteFirestation(1);

		verify(dataRepository, times(1)).deleteFirestation(any());
	}

	@Test
	void given_A_Station_Id_Return_A_List_of_PersonWithAge_covered() throws Exception {// test mal architecturé
		when(medicalRecordService.convertMedicalRecordsToPersonInfos(any())).thenReturn(personInfos);

		when(dataRepository.findFirestation(1).getAddress()).thenReturn(address);
		List<PersonInfoDTO> personsCovered = firestationService.getPersonsCoveredByStation(1);

		assertThat(personsCovered.get(0).equals(personInfos.get(0)));
		verify(medicalRecordService).retrieveMedicalRecordFromPersons(any());
	}

	@Test
	void given_A_Station_Id_Should_Return_Resident_Phones() {
//		when(dataRepository.getPhonePersonsAtAddress(address)).thenReturn(phones);
	}
}
