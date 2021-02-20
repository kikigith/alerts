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

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

	@Mock
	DataRepository dataRepository;

	@InjectMocks
	private FirestationService firestationService;

	@Test
	public void testSaveFirestation() throws Exception {
		Firestation firestation = new Firestation();

		when(dataRepository.saveFirestation(any(Firestation.class))).thenReturn(firestation);

		Firestation savedFirestation = firestationService.saveFirestation(new Firestation());

		verify(dataRepository).saveFirestation(any(Firestation.class));

		assertThat(savedFirestation).isNotNull();

	}

	@Test
	void testUpdateFirestation() throws Exception {
		Firestation firestation = new Firestation();

		firestationService.updateFirestation(firestation);

		verify(dataRepository).saveFirestation(any());
	}

	@Test
	void testDeleteFirestation() throws Exception {
		List<Firestation> firestations = new ArrayList<Firestation>();
		Firestation frs = new Firestation(1);

//		when(dataRepository.findPersonByLastNameAndFirstName(any(), any())).thenReturn(persons);

		firestationService.deleteFirestation(1);

		verify(dataRepository, times(1)).deleteFirestation(any());
	}

}
