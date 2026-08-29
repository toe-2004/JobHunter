package com.jobhunter.JobHunter.dto;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerProfileDto {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company email is required")
    @Email(message = "Please provide a valid company email address")
    private String companyEmail;

    @NotBlank(message = "Company phone is required")
    private String companyPhone;

    @NotBlank(message = "Company description is required")
    private String companyDescription;

    @NotBlank(message = "Company location is required")
    private String companyLocation;


    @NotBlank(message = "Your name is required")
    private String name;
    
    private String profilePhoto;
    
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public static EmployerProfileDto from(User user, Employer employer) {

        EmployerProfileDto dto = new EmployerProfileDto();

        dto.setProfilePhoto(user.getProfilePhoto());
        dto.setCompanyName(employer.getCompanyName());
        dto.setCompanyEmail(employer.getCompanyEmail());
        dto.setCompanyPhone(employer.getCompanyPhone());
        dto.setCompanyDescription(employer.getCompanyDescription());
        dto.setCompanyLocation(employer.getCompanyLocation());
        dto.setName(user.getName());
        

        return dto;
    }

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}