package com.safetynet.alerts.model.dto;

import java.util.List;

public class PersonInfoDTO {
	String nom;
	String prenom;
	int age;
	String email;
	String phone;
	String address;
	List<String> allergies;
	List<String> medications;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public PersonInfoDTO() {
		super();
	}

	public PersonInfoDTO(String nom, String prenom, int age, String email, String phone, String address,
			List<String> allergies, List<String> medications) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.allergies = allergies;
		this.medications = medications;
	}

}
