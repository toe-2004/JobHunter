package com.jobhunter.JobHunter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerRegistrationDto {

@NotBlank(message = "Company name is required")
private String companyName;

public String getCompanyName() {
	return companyName;
}

public void setCompanyName(String companyName) {
	this.companyName = companyName;
}

public String getCompanyEmail() {
	return companyEmail;
}

public void setCompanyEmail(String companyEmail) {
	this.companyEmail = companyEmail;
}

public String getCompanyPhone() {
	return companyPhone;
}

public void setCompanyPhone(String companyPhone) {
	this.companyPhone = companyPhone;
}

public String getCompanyDescription() {
	return companyDescription;
}

public void setCompanyDescription(String companyDescription) {
	this.companyDescription = companyDescription;
}

public String getCompanyLocation() {
	return companyLocation;
}

public void setCompanyLocation(String companyLocation) {
	this.companyLocation = companyLocation;
}

@NotBlank(message = "Company email is required")
@Email(message = "Please provide a valid company email address")
private String companyEmail;

@NotBlank(message = "Company phone is required")
private String companyPhone;

@NotBlank(message = "Company description is required")
private String companyDescription;

@NotBlank(message = "Company location is required")
private String companyLocation;
}