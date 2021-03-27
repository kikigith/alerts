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

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

	@Mock
	DataRepository dataRepository;

	@InjectMocks
	private MedicalRecordService medicalRecordService;

	@Test
	public void testSaveMedicalRecord() throws Exception {
		MedicalRecord medicalRecord = new MedicalRecord();

		when(dataRepository.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

		MedicalRecord savedMedicalRecord = medicalRecordService.saveMedicalRecord(medicalRecord);

		verify(dataRepository).saveMedicalRecord(any());

		assertThat(savedMedicalRecord).isNotNull();

	}

	@Test
	void testUpdateMedicalRecord() throws Exception {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName("Bill");
		medicalRecord.setLastName("Gink");

		medicalRecordService.updateMedicalRecord(medicalRecord);

		verify(dataRepository).saveMedicalRecord(any());

	}

	@Test
	void testDeleteMedicalRecord() throws Exception {
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName("Bill");
		medicalRecord.setLastName("Gink");
		medicalRecords.add(medicalRecord);

		when(dataRepository.findMedicalRecordByLastNameAndFirstName(any(), any())).thenReturn(medicalRecords);

		medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());

		verify(dataRepository, times(1)).deleteMedicalRecord(any());
	}

}
