package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@Mock
	DataRepository dataRepository;
	@InjectMocks
	PersonService personService;

	@Test
	void testSavePerson() throws Exception {
		Person person = new Person();

		when(dataRepository.savePerson(any(Person.class))).thenReturn(person);

		Person savedPerson = personService.savePerson(new Person());

		verify(dataRepository).savePerson(any(Person.class));

		assertThat(savedPerson).isNotNull();
	}

	@Test
	void testUpdatePerson() throws Exception {
		Person person = new Person();

		personService.updatePerson(person);

		verify(dataRepository).savePerson(any());
	}

	@Test
	void testDeletePerson() throws Exception {
		List<Person> persons = new ArrayList<Person>();
		Person pers1 = new Person();
		pers1.setFirstName("mike");
		pers1.setLastName("fred");
		persons.add(pers1);
		Person pers2 = new Person();
		pers2.setFirstName("Julie");
		pers2.setLastName("Oulard");
		persons.add(pers2);

		when(dataRepository.findPersonByLastNameAndFirstName(any(), any())).thenReturn(persons);

		personService.deletePerson(pers1.getLastName(), pers1.getFirstName());

		verify(dataRepository, times(1)).deletePerson(any());
	}

	@Test
	void testGetPersons() throws Exception {
		Person person = new Person();

		List<Person> persons = new ArrayList<Person>();

		persons.add(person);

		when(dataRepository.findAllPerson()).thenReturn(persons);

		List<Person> allPersons = (List<Person>) personService.findAll();

		verify(dataRepository).findAllPerson();

		assertThat(allPersons).hasSize(1);
	}

}
