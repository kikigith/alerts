package com.safetynet.alerts.controller.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildrenCoveredDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.model.dto.PersonsCoveredByStation;
import com.safetynet.alerts.model.dto.StationCoverageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alerts.controller.FirestationController;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest extends AbstractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	private String address, city;
	private List<String> phones;
	private List<PersonInfoDTO> personsInfo, childrenInfo;
    PersonsCoveredByStation personsCovered;
    ChildrenCoveredDTO childrenCovered;

	StationCoverageDTO stationCoverage1, stationCoverage2;
	List<StationCoverageDTO> stationsCoverage;
	Firestation station1, station2;
	Person person1, person2, person3, person4;
	List<Person> personsCovered1, personsCovered2;


	@BeforeEach
	public void initTest(){
		address="25 rue Beauchamp";
		phones=new ArrayList<>();
		phones.add("24 132 45");
		PersonInfoDTO pi1=new PersonInfoDTO();
		pi1.setNom("Frank");
		pi1.setPrenom("Alain");
		PersonInfoDTO pi2=new PersonInfoDTO();
		pi2.setNom("Bill");
		pi2.setPrenom("Black");
		personsInfo=new ArrayList<>();
		personsInfo.add(pi1);
		personsInfo.add(pi2);

		personsCovered = new PersonsCoveredByStation();
		personsCovered.setStationID(1);
		personsCovered.setPersonsCovered(personsInfo);

		PersonInfoDTO ci1=new PersonInfoDTO();
		ci1.setNom("Karl");
		ci1.setPrenom("Benett");
		ci1.setAge(14);
		PersonInfoDTO ci2 = new PersonInfoDTO();
		ci2.setNom("Idossou");
		ci2.setPrenom("Victor");
		ci2.setAge(16);
		childrenInfo=new ArrayList<>();
		childrenInfo.add(ci1);
		childrenInfo.add(ci2);
		childrenCovered = new ChildrenCoveredDTO();
		childrenCovered.setEnfants(childrenInfo);
		childrenCovered.setOthers(personsInfo);

		city="Parakou";

		station1 = new Firestation();
		station1.setStation(1);
		station1.setAddress("24 rue Toho");
		station2 = new Firestation();
		station2.setStation(2);
		station2.setAddress("98 station Balneaire");

		stationCoverage1 = new StationCoverageDTO();
		stationCoverage1.setAddress("24 rue Toho");
		person1 = new Person();
		person1.setFirstName("Alice");
		person2 = new Person();
		person2.setFirstName("Dossa");
		personsCovered1  = new ArrayList<>();
		personsCovered1.add(person1);
		personsCovered1.add(person2);
		stationCoverage1.setPersonsCovered(personsCovered1);

		stationCoverage2 = new StationCoverageDTO();
		stationCoverage2.setAddress("98 station Balneaire");
		person3 = new Person();
		person3.setFirstName("Victoire");
		person4 = new Person();
		person4.setFirstName("Dossa");
		personsCovered2 = new ArrayList<>();
		personsCovered2.add(person3);
		personsCovered2.add(person4);
		stationCoverage2.setPersonsCovered(personsCovered2);

		stationsCoverage = new ArrayList<>();
		stationsCoverage.add(stationCoverage1);
		stationsCoverage.add(stationCoverage2);

	}

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
	public void when_Given_A_station_ID_should_Delete_Firestation() throws Exception {
		String uri = "/firestation?stationId=1";
		int status = mockMvc.perform(delete(uri)).andReturn().getResponse().getStatus();
		assertEquals(202, status);
	}

	@Test
	public void when_Given_A_station_ID_should_return_Residents_phones() throws Exception{
		String uri="/phoneAlert?firestation=1";
		doReturn(phones).when(firestationService).getResidentPhone(1);
		int status = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$[0]", is("24 132 45"))).andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}


	@Test
	public void given_A_station_ID_should_return_persons_Covered_by_station() throws Exception{
		String uri="/firestation?stationNumber=1";
		doReturn(personsCovered).when(firestationService).getPersonsCoveredByStation(1);
		int status = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.personsCovered[0].nom",is("Frank")))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void given_An_address_should_return_children_Covered_by_station() throws Exception{
		String uri="/childAlert?address="+address;

		doReturn(childrenCovered).when(firestationService).getChildrenCovered(address);

		int status = mockMvc.perform(get(uri))
				.andExpect(jsonPath("$.enfants[0].nom", is("Karl")))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void given_An_address_should_return_persons_Covered_at_address() throws Exception{
		String uri="/fire?address="+address;

		doReturn(personsCovered).when(firestationService).getStationAndPersonsCoveredAtAddress(address);
		int status = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.personsCovered[0].nom",is("Frank")))
				.andReturn().getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void given_A_list_of_stations_should_return_Covered_homes() throws Exception{
		String uri="/flood/stations?stations=1";
		List<Integer> stations=new ArrayList<>();
		stations.add(1);
		stations.add(2);

		doReturn(stationsCoverage).when(firestationService.getStationsCoverage(stations));
		int status = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(jsonPath("$[0].personsCovered[0].nom",is(person1.getFirstName())))
				.andReturn().getResponse().getStatus();
		assertEquals(200,status);
	}
}
