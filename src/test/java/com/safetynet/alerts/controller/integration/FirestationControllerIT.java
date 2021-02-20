package com.safetynet.alerts.controller.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.model.Firestation;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerIT extends AbstractITControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private FirestationController firestationController;

	private static Firestation newFirestation, upFirestation, delFirestation;

	@BeforeAll
	public static void initTest() {
		newFirestation = new Firestation();
		newFirestation.setStation(10);
		newFirestation.setAddress("67 Rue michon");

		upFirestation = new Firestation();
		upFirestation.setStation(10);
		upFirestation.setAddress("69 Rue machon");

		delFirestation = new Firestation();
		delFirestation.setStation(10);
	}

	@Test
	public void testGetFirestations() throws Exception {

		mockMvc.perform(get("/firestations").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$[0].station").value("3"));
	}

	@Test
	public void testCreateFirestation() throws Exception {

		mockMvc.perform(post("/firestation").content(asJsonString(newFirestation))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.station").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.station").value("10"))
				.andDo(MockMvcResultHandlers.print());
//				.andExpect(MockMvcResultMatchers.jsonPath("$.id"), is("4L"));

	}

	@Test
	public void testUpdateFirestation() throws Exception {

		mockMvc.perform(put("/firestation?stationId=10").content(asJsonString(upFirestation))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.address").value("69 Rue machon"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.station").value("10"));
	}

	@Test
	public void testDeleteFirestation() throws Exception {

		mockMvc.perform(delete("/firestation?stationId=10")).andExpect(status().isOk());
	}
}
