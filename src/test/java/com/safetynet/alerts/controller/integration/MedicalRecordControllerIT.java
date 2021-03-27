package com.safetynet.alerts.controller.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class MedicalRecordControllerIT extends AbstractITControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MedicalRecordController medicalRecordController;

	@Test
	@Order(1)
	public void testGetMedicalrecords() throws Exception {

		mockMvc.perform(get("/medicalRecords").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", is("John")));
	}

	@Test
	@Order(2)
	public void testCreateMedicalRecord() throws Exception {
		MedicalRecord mr = new MedicalRecord();
		mr.setFirstName("Aline");
		mr.setLastName("Dupont");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		mr.setBirthdate(sdf.parse("25-02-2000"));
		List<String> allergies = new ArrayList<>();
		allergies.add("spatonine");
		allergies.add("alacol");
		mr.setAllergies(allergies);
		List<String> medications = new ArrayList<>();
		medications.add("beotim:200mg");
		medications.add("flavoquine:400mg");
		mr.setMedications(medications);

		mockMvc.perform(post("/medicalRecord?firstname=Aline&lastname=Dupont").content(asJsonString(mr))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Aline"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	@Order(3)
	public void testUpdateMedicalRecord() throws Exception {
		MedicalRecord mr = new MedicalRecord();
		mr.setFirstName("Aline");
		mr.setLastName("Dupont");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		mr.setBirthdate(sdf.parse("15-02-2001"));
		List<String> allergies = new ArrayList<>();
		allergies.add("spatomine");
		allergies.add("beoziol");
		mr.setAllergies(allergies);
		List<String> medications = new ArrayList<>();
		medications.add("quinine:200mg");
		medications.add("chloroquine:400mg");
		mr.setMedications(medications);
		mockMvc.perform(put("/medicalRecord?firstname=Aline&lastname=Dupont").content(asJsonString(mr))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Aline"));
	}

	@Test
	@Order(4)
	public void testDeletePerson() throws Exception {
		MedicalRecord mr = new MedicalRecord();
		mr.setFirstName("Aline");
		mr.setLastName("Dupont");
		mockMvc.perform(delete("/medicalRecord?firstname=Aline&lastname=Dupont")).andExpect(status().isAccepted());
	}
}
