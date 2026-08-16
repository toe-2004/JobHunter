package com.jobhunter.JobHunter.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDto {

    public MultipartFile getCurriculumVitae() {
		return curriculumVitae;
	}

	public void setCurriculumVitae(MultipartFile curriculumVitae) {
		this.curriculumVitae = curriculumVitae;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

	private MultipartFile curriculumVitae;

    private String coverLetter;
}