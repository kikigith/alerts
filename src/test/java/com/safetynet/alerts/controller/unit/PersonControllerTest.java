package com.safetynet.alerts.controller.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.exception.PersonInvalidException;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.exception.PersonNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonService;

@WebMvcTest(controllers = PersonController.class)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerTest extends AbstractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	@Mock
	private DataRepository dataRepository;

	private List<String> emails;
	private String city;

	@BeforeEach
	void initTest(){
		city="Parakou";
		emails=new ArrayList<>();
		emails.add("vidossou@gmail.com");
		emails.add("bkarl@outlook.com");
	}

	@Test
	@Order(1)
	public void testGetPersons() throws Exception {
		mockMvc.perform(get("/persons")).andExpect(status().isOk());

	}

	@Test
	@Order(2)
	public void testCreatePerson() throws Exception {
		Person person = new Person();
		person.setLastName("Martin");
		person.setFirstName("Bruce");
		person.setEmail("bmartin@gmail.com");
		person.setAddress("25, Avenue RIO");
		person.setCity("Porto");
		person.setPhone("32002020");

		String personJson = mapToJson(person);

		doReturn(person).when(personService).savePerson(any());

		int status = mockMvc
				.perform(post("/person?firstname=Bruce&lastname=Martin").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(personJson))
				.andExpect(jsonPath("$.firstName", is(person.getFirstName()))).andReturn().getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	@Order(3)
	public void testUpdatePerson() throws Exception {
		String uri = "/person?firstname=LORD&lastname=Franklin";
		Person person = new Person();
		person.setFirstName("LORD");
		person.setLastName("Franklin");
		person.setEmail("flord@test.com");
		person.setPhone("0203003");
		person.setZip("8484848");
		person.setAddress("43 Bruyère");
		person.setCity("Albatros");

		String personJson = mapToJson(person);
		int status = mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(personJson))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	@Order(4)
	public void testDeletePerson() throws Exception {
		String uri = "/person?lastname=mike&firstname=karl";
		Person person = new Person();
		person.setFirstName("karl");
		person.setLastName("mike");

		String personJson = mapToJson(person);

		when(personService.findPerson("mike", "karl")).thenReturn(person);
		int status = mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
		assertEquals(202, status);
	}

	@Test
	@Order(6)
	public void given__a_person_with_empty_firstname_or_lastname_save_should_raise_InvalidPersonException()
			throws Exception {
		String uri = "/person?lastname=&firstname=";
		Person person = new Person("", "");

		String personJson = mapToJson(person);
		mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPersonException));

	}

//	@Test
//	@Order(7)
//	public void given_non_existing_person_update_should_raise_PersonIntrouvableException() throws Exception {
//		String uri = "/person?lastname=mike&firstname=karl";
//		Person person = new Person("mike", "karl");
//
//		when(personService.findPerson("mike", "karl")).thenReturn(null);
////		doThrow(new PersonIntrouvableException()).when(personService.findPerson("mike", "karl"));
////		doThrow(new PersonIntrouvableException()).when(personService).updatePerson(person);
//		mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
//				.andDo(print()).andDo(MockMvcResultHandlers.print())
//				.andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonIntrouvableException));
//	}

	@Test
	@Order(7)
	public void given_non_existing_person_delete_should_raise_PersonIntrouvableException() throws Exception {
		String uri = "/person?lastname=mike&firstname=karl";
		Person person = new Person("mike", "karl");

		when(personService.findPerson("mike", "karl")).thenReturn(null);

		mockMvc.perform(delete(uri).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonNotFoundException));
//		assertThrows(PersonIntrouvableException.class, () -> {
//			mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
//		});
	}

	@Test
	@Order(8)
	public void when_firstname_or_lastname_is_empty_delete_should_raise_exception() throws Exception{
		String uri = "/person?lastname=mike&firstname=";
		Person person = new Person("mike", "karl");

		when(personService.findPerson("mike", "")).thenReturn(null);

		mockMvc.perform(delete(uri).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof PersonInvalidException));
	}

	@Test
	@Order(9)
	public void testListPersonInfos() throws Exception {
		List<PersonInfoDTO> personInfo = new ArrayList<>();
		PersonInfoDTO pi1 = new PersonInfoDTO();
		pi1.setNom("Aline");
		pi1.setPrenom("Dupont");
		PersonInfoDTO pi2 = new PersonInfoDTO();
		pi2.setNom("Aline");
		pi2.setPrenom("Dupond");
		personInfo.add(pi1);
		personInfo.add(pi2);

		String personInfoJson = mapToJson(personInfo);

		Person aline = new Person("Aline", "Dupont");
		when(personService.getPersonInfos("Aline", "Dupont")).thenReturn(personInfo);
		String uri = "/personInfo?lastname=Aline&firstname=Dupont";
		mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(personInfoJson)).andDo(print())
				.andExpect(jsonPath("$[0].nom", is("Aline"))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@Order(10)
	public void when_Given_An_City_should_return_Residents_emails() throws Exception{
		String uri="/communityEmail?city="+city;
		doReturn(emails).when(personService).getCommunityEmail(city);
		int status = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0]",is("vidossou@gmail.com")))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}



}
