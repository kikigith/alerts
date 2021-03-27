package com.safetynet.alerts.controller.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT extends AbstractITControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PersonController personController;

	@Test
	@Order(1)
	public void testGetPersons() throws Exception {

		mockMvc.perform(get("/persons").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", is("John")));
	}

	@Test
	@Order(2)
	public void testCreatePerson() throws Exception {
		Person pers = new Person();
		pers.setLastName("Benett");
		pers.setFirstName("Trump");
		pers.setEmail("btrump@trump.world");
		pers.setCity("Parakou");
		pers.setPhone("00495929949");
		pers.setZip("000000");

		mockMvc.perform(post("/person?firstname=Trump&lastname=Benett").content(asJsonString(pers))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists()).andDo(MockMvcResultHandlers.print());
//				.andExpect(MockMvcResultMatchers.jsonPath("$.id"), is("4L"));

	}

	@Test
	@Order(3)
	public void testUpdatePerson() throws Exception {
		Person pers = new Person();
		pers.setLastName("Benett");
		pers.setFirstName("Trump");
		pers.setEmail("btrump@trump.world");
		pers.setCity("Cotonou");
		pers.setPhone("093939292");
		pers.setZip("000000");
		mockMvc.perform(put("/person?lastname=Benett&firstname=Trump").content(asJsonString(pers))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Trump"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Cotonou"));
	}

	@Test
	@Order(4)
	public void testDeletePerson() throws Exception {
		Person pers = new Person();
		pers.setLastName("Benett");
		pers.setFirstName("Trump");
		mockMvc.perform(delete("/person?lastname=Benett&firstname=Trump").content(asJsonString(pers))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}
}
