package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@Mock
	DataRepository dataRepository;

	@Mock
	MedicalRecordService medicalRecordService;

	@InjectMocks
	PersonService personService;

	List<MedicalRecord> mr;
	List<PersonInfoDTO> personInfo;
	List<Person> persons;
	Person person, pers1, pers2, pers3;

	@BeforeEach
	void initTest() {

		person = new Person();
		person.setFirstName("mike");
		person.setLastName("fiver");

		mr = new ArrayList<>();
		MedicalRecord mr1 = new MedicalRecord();
		mr1.setFirstName("Aline");
		mr1.setLastName("Dupont");
		mr.add(mr1);
		MedicalRecord mr2 = new MedicalRecord();
		mr2.setFirstName("Aline");
		mr2.setLastName("Dupond");
		mr.add(mr2);

		personInfo = new ArrayList<>();
		PersonInfoDTO pi1 = new PersonInfoDTO();
		pi1.setNom("Aline");
		pi1.setPrenom("Dupont");
		pi1.setAge(20);
		PersonInfoDTO pi2 = new PersonInfoDTO();
		pi2.setNom("Aline");
		pi2.setPrenom("Dupond");
		personInfo.add(pi1);
		personInfo.add(pi2);

		persons = new ArrayList<Person>();
		pers1 = new Person();
		pers1.setFirstName("mike");
		pers1.setLastName("fred");
		pers1.setCity("cotonou");
		pers1.setEmail("fmike@gmail.com");
		pers1.setAddress("23 rue dubond");
		persons.add(pers1);
		pers2 = new Person();
		pers2.setFirstName("Julie");
		pers2.setLastName("Oulard");
		pers2.setCity("cotonou");
		pers2.setEmail("ojulie@outlook.com");
		pers2.setAddress("45 avenue 2");
		persons.add(pers2);
		pers3 = new Person();
		pers3.setFirstName("Benon");
		pers3.setLastName("Fiacre");
		pers3.setCity("Allada");
		pers3.setEmail("fbenon@yahoo.fr");
		pers3.setAddress("45 avenue 2");
		persons.add(pers3);


		persons.add(person);
	}

	@Test
	void testSavePerson() throws Exception {

		when(dataRepository.savePerson(any(Person.class))).thenReturn(person);

		Person savedPerson = personService.savePerson(person);

		verify(dataRepository).savePerson(any(Person.class));

		assertThat(savedPerson).isNotNull();
	}

	@Test
	void testUpdatePerson() throws Exception {

		when(dataRepository.savePerson(any(Person.class))).thenReturn(person);
//		when(personService.findPerson("mike", "fiver")).thenReturn(person);
		Person updatedPerson = personService.updatePerson(person);

		verify(dataRepository).savePerson(person);
	}

	@Test
	void testDeletePerson() throws Exception {

		when(dataRepository.findPersonByLastNameAndFirstName(any(), any())).thenReturn(persons);

		personService.deletePerson(pers1.getLastName(), pers1.getFirstName());

		verify(dataRepository, times(1)).deletePerson(any());
	}

	@Test
	void testGetPersons() throws Exception {

		when(dataRepository.findAllPerson()).thenReturn(persons);

		List<Person> allPersons = (List<Person>) personService.findAll();

		verify(dataRepository).findAllPerson();

		assertThat(allPersons).hasSize(4);
	}

	@Test
	void testGetPersonInfos() throws Exception {
//		MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class);
		when(medicalRecordService.findMedicalRecords("Aline", "Dupont")).thenReturn(mr);
		when(medicalRecordService.convertMedicalRecordsToPersonInfos(any())).thenReturn(personInfo);

//		utils.when(() -> Utils.calculateAge(new Date("21/03/2000"))).thenReturn(25);
//		when(((Utils) utils).calculateAge(any())).thenReturn(9);

		List<PersonInfoDTO> pinfo = personService.getPersonInfos("Aline", "Dupont");

		verify(medicalRecordService).findMedicalRecords("Aline", "Dupont");
		verify(medicalRecordService).convertMedicalRecordsToPersonInfos(any());
		assertEquals(20, pinfo.get(0).getAge());

//		verify(utils).ca
//		assertThat(pinfo).hasSameSizeAs(personInfo);

	}

	@Test
	public void given_A_city_should_return_community_emails() throws Exception{
		when(dataRepository.findAllPerson()).thenReturn(persons);

		List<String> cotonoucommunity=personService.getCommunityEmail("cotonou");
		assertThat(cotonoucommunity).hasSize(2);
		assertThat(cotonoucommunity.contains(pers1.getEmail()));
	}

	@Test
	public void given_An_Adress_should_return_Persons() throws Exception{
		when(dataRepository.findAllPerson()).thenReturn(persons);

		List<Person> personsFound=personService.findPersonByAddress("45 avenue 2");
		assertThat(personsFound).hasSize(2);
		//assertThat(personsFound.contains(pers3.getEmail());
	}

}
