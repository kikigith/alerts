package com.safetynet.alerts.controller.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;

@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest extends AbstractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	@Test
	public void testGetFirestations() throws Exception {
		mockMvc.perform(get("/firestations")).andExpect(status().isOk());

	}

	@Test
	public void testCreateFirestation() throws Exception {
		Firestation frs = new Firestation();
		frs.setStation(1);
		frs.setAddress("Rue 34 Cotonou");

		String firestationJson = mapToJson(frs);

		doReturn(frs).when(firestationService).saveFirestation(any());

		int status = mockMvc
				.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON_VALUE).content(firestationJson))
				.andExpect(jsonPath("$.station", is(frs.getStation()))).andReturn().getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	public void testUpdateFirestation() throws Exception {
		String uri = "/firestation?stationId=1";
		Firestation frs = new Firestation();
		frs.setStation(1);
		frs.setAddress("Rue 35 Cotonou");

		String firestationJson = mapToJson(frs);
		int status = mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(firestationJson))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testDeleteFirestation() throws Exception {
		String uri = "/firestation?stationId=1";
		int status = mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
		assertEquals(202, status);
	}
}
