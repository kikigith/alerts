package com.safetynet.alerts.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest extends AbstractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	@Test
	public void testGetPersons() throws Exception {
		mockMvc.perform(get("/persons")).andExpect(status().isOk());

	}

	@Test
	public void testCreatePerson() throws Exception {
		Person person = new Person();
		person.setLastName("Martin");
		person.setFirstName("Bruce");
		person.setEmail("bmartin@gmail.com");
		person.setAddress("25, Avenue RIO");
		person.setCity("Porto");
		person.setPhone("32002020");

		String personJson = mapToJson(person);
		int status = mockMvc.perform(post("/person?firstname=Bruce&lastname=Martin")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(personJson)).andReturn().getResponse()
				.getStatus();
		assertEquals(201, status);
	}

	@Test
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
	public void testDeletePerson() throws Exception {
		String uri = "/person?lastname=mike&firstname=karl";
		int status = mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
		assertEquals(202, status);
	}

}
