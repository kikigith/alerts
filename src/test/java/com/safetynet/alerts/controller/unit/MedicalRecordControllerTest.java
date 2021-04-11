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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest extends AbstractControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@Test
	public void testGetMedicalRecords() throws Exception {
		mockMvc.perform(get("/medicalRecords")).andExpect(status().isOk());

	}

	@Test
	public void testCreateMedicalRecord() throws Exception {
		// Arrange
		MedicalRecord mr = new MedicalRecord();
		mr.setFirstName("Aline");
		mr.setLastName("Dupont");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		mr.setBirthdate(sdf.parse("25-02-2000"));
		List<String> allergies = new ArrayList<String>();
		allergies.add("spatonine");
		allergies.add("alacol");
		mr.setAllergies(allergies);
		List<String> medications = new ArrayList<String>();
		medications.add("beotim:200mg");
		medications.add("flavoquine:400mg");
		mr.setMedications(medications);

		String medicalRecordJson = mapToJson(mr);

		doReturn(mr).when(medicalRecordService).saveMedicalRecord(any());

		// Act and Assert
		int status = mockMvc
				.perform(
						post("/medicalRecord").contentType(MediaType.APPLICATION_JSON_VALUE).content(medicalRecordJson))
				.andExpect(jsonPath("$.firstName", is(mr.getFirstName()))).andReturn().getResponse().getStatus();
		assertEquals(201, status);
	}

	@Test
	public void testUpdateMedicalRecord() throws Exception {
		String uri = "/medicalRecord?firstname=Aline&lastname=Dupont";
		MedicalRecord mr = new MedicalRecord();
		mr.setFirstName("Aline");
		mr.setLastName("Durand");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		mr.setBirthdate(sdf.parse("15-02-2001"));
		List<String> allergies = new ArrayList<String>();
		allergies.add("spatomine");
		allergies.add("beoziol");
		mr.setAllergies(allergies);
		List<String> medications = new ArrayList<String>();
		medications.add("quinine:200mg");
		medications.add("chloroquine:400mg");
		mr.setMedications(medications);

		String medicalRecordJson = mapToJson(mr);
		int status = mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(medicalRecordJson))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testDeleteMedicalRecord() throws Exception {
		String uri = "/medicalRecord?lastname=Dupond&firstname=Aline";
		int status = mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
		assertEquals(202, status);
	}

}
