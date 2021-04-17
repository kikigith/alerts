package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import com.safetynet.alerts.exception.MedicalRecordInvalidException;
import com.safetynet.alerts.exception.MedicalRecordNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

	@Mock
	DataRepository dataRepository;

	@Mock
	PersonService personService;

	@InjectMocks
	private MedicalRecordService medicalRecordService;

	private MockedStatic<Utils> utilsMock;
	private MedicalRecord medRecord;
	private Person person;
	private PersonInfoDTO persInfo;


	//@BeforeEach
	//public void initTest(){

	//}

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
		List<String> allergies=new ArrayList<>();
		allergies.add("phenol");
		medicalRecord.setAllergies(allergies);
		List<String> medications=new ArrayList<>();
		medications.add("cipro");
		medicalRecord.setMedications(medications);
		Calendar calendar = new GregorianCalendar(2000, 11, 03);
		medicalRecord.setBirthdate(calendar.getTime());

        //when(dataRepository.findMedicalRecordByLastNameAndFirstName("Gink","Bill")).thenReturn(mrs);

		when(dataRepository.saveMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
		MedicalRecord mrec = medicalRecordService.updateMedicalRecord(medicalRecord);

		assertThat(mrec).isNotNull();

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


	@Test
	void when_lastname_or_firstname_empty_or_is_not_set_update_should_raise_exception() throws Exception{
		MedicalRecord mRecord = new MedicalRecord();
		Assertions.assertThrows(MedicalRecordInvalidException.class, ()->{
			medicalRecordService.updateMedicalRecord(mRecord);
		});
	}

	@Test
	void when_lastname_empty_or_is_not_set_update_should_raise_exception() throws Exception{
		MedicalRecord mRecord = new MedicalRecord();
		mRecord.setFirstName("Test");
		mRecord.setLastName(null);
		Assertions.assertThrows(MedicalRecordInvalidException.class, ()->{
			medicalRecordService.updateMedicalRecord(mRecord);
		});
	}

	@Test
	void when_firstname_empty_or_is_not_set_update_should_raise_exception() throws Exception{
		MedicalRecord mRecord = new MedicalRecord();
		mRecord.setFirstName("");
		mRecord.setLastName("Test");
		Assertions.assertThrows(MedicalRecordInvalidException.class, ()->{
			medicalRecordService.updateMedicalRecord(mRecord);
		});
	}

	@Test
	void when_lastname_or_firstname_empty_or_is_not_set_findmedicalrecord_should_raise_exception() throws Exception{

		when(dataRepository.findMedicalRecordByLastNameAndFirstName(any(),any())).thenReturn(
				Collections.emptyList()
		);

		Assertions.assertThrows(MedicalRecordNotFoundException.class, ()->{
			medicalRecordService.findMedicalRecord("Mike","Fink");
		});
	}


	@Test
	void given_a_medicalRecord_should_convert_into_personInfoDTO() throws Exception {


		utilsMock= Mockito.mockStatic(Utils.class);
		medRecord = new MedicalRecord();

		medRecord.setLastName("Mike");
		medRecord.setFirstName("Fink");
		medRecord.setMedications(new ArrayList<String>() {
			{
				add("azenol:30mg");
				add("phenol:45mg");
			}
		});
		medRecord.setAllergies(new ArrayList<String>() {
			{
				add("citine");
				add("carotène");
			}
		});

		Calendar calendar = new GregorianCalendar(2000, 11, 03);

		medRecord.setBirthdate(calendar.getTime());

		person=new Person();
		person.setLastName("Mike");
		person.setFirstName("Fink");
		person.setEmail("fmike@gmail.com");
		person.setPhone("75 456 024");



		persInfo=new PersonInfoDTO();
		persInfo.setNom("Mike");
		persInfo.setPrenom("Fink");

		when(personService.findPerson("Mike", "Fink")).thenReturn(person);

		utilsMock.when(()->Utils.calculateAge(medRecord.getBirthdate())).thenReturn(20);

		PersonInfoDTO piDTO=medicalRecordService.convertMedicalRecordToPersonInfo(medRecord);

		assertThat(piDTO.getNom()).isSameAs(person.getFirstName());


	}

	@Test
	public void given_a_person_should_return_his_medical_record() throws Exception{

		person=new Person();
		person.setLastName("Mike");
		person.setFirstName("Fink");
		person.setEmail("fmike@gmail.com");
		person.setPhone("75 456 024");
		MedicalRecord mr=new MedicalRecord();
		mr.setLastName("Mike");
		mr.setFirstName("Fink");

		List<MedicalRecord> mrs=new ArrayList<>();
		mrs.add(mr);
		when(dataRepository.findAllMedicalRecord()).thenReturn(mrs);

		MedicalRecord mrFound=medicalRecordService.getMedicalRecordForAPerson(person);

		assertThat(mrFound.getFirstName()).isSameAs(person.getFirstName());
	}



	@Test
	public void given_A_list_of_MedicalRecord_Should_return_A_list_of_personInfoDTO() throws Exception{

		PersonInfoDTO pi1 = new PersonInfoDTO();
		pi1.setPrenom("Fink");
		pi1.setNom("Mike");
		PersonInfoDTO pi2 = new PersonInfoDTO();
		pi2.setPrenom("Alison");
		pi2.setNom("Fred");
		List<PersonInfoDTO> pInfos = new ArrayList<>();
		pInfos.add(pi1);
		pInfos.add(pi2);

		Person person1=new Person();
		person1.setLastName("Mike");
		person1.setFirstName("Fink");
		person1.setEmail("fmike@gmail.com");
		person1.setPhone("75 456 024");
		Person person2=new Person();
		person2.setLastName("Fred");
		person2.setFirstName("Alison");
		person2.setEmail("afred@gmail.com");
		person2.setPhone("25 987 024");

		MedicalRecord mr1=new MedicalRecord();
		mr1.setLastName("Mike");
		mr1.setFirstName("Fink");
		Calendar cal1 = new GregorianCalendar(2000, 11, 03);

		mr1.setBirthdate(cal1.getTime());
		MedicalRecord mr2=new MedicalRecord();
		mr2.setLastName("Fred");
		mr2.setFirstName("Alison");
		Calendar cal2 = new GregorianCalendar(2002, 10, 23);

		mr2.setBirthdate(cal2.getTime());
		List<MedicalRecord> mrs=new ArrayList<>();
		mrs.add(mr1);
		mrs.add(mr2);
		when(personService.findPerson("Mike","Fink")).thenReturn(person1);
		when(personService.findPerson("Fred","Alison")).thenReturn(person2);

		List<PersonInfoDTO> persInfos=medicalRecordService.convertMedicalRecordsToPersonInfos(mrs);

		assertThat(persInfos.get(0).getNom()).isSameAs(person2.getFirstName()).isSameAs(mr2.getFirstName());


	}

}
