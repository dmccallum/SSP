package org.jasig.ssp.model.external;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Immutable
@Table(name = "v_external_course")
public class ExternalCourse extends AbstractExternalData implements ExternalData, Serializable {

	private static final long serialVersionUID = 1529384456357160956L;
	
	@Column(nullable = false, length = 20)
	@NotNull
	@NotEmpty
	@Size(max = 20)
	private String code,formattedCourse,subjectAbbreviation, title;
	
	@Column(nullable = false, length = 2000)
	@NotNull
	@NotEmpty
	@Size(max = 2000)	
	private String description;
	
	
	@Column(nullable = false)
	private Integer maxCreditHours,minCreditHours;
	
	@Column(nullable = false)
	private Boolean isDev;
	
	

	public String getSubjectAbbreviation() {
		return subjectAbbreviation;
	}

	public void setSubjectAbbreviation(String subjectAbbreviation) {
		this.subjectAbbreviation = subjectAbbreviation;
	}

	public String getFormattedCourse() {
		return formattedCourse;
	}

	public void setFormattedCourse(String formattedCourse) {
		this.formattedCourse = formattedCourse;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMaxCreditHours() {
		return maxCreditHours;
	}

	public void setMaxCreditHours(Integer maxCreditHours) {
		this.maxCreditHours = maxCreditHours;
	}

	public Integer getMinCreditHours() {
		return minCreditHours;
	}

	public void setMinCreditHours(Integer minCreditHours) {
		this.minCreditHours = minCreditHours;
	}

	public Boolean getIsDev() {
		return isDev;
	}

	public void setIsDev(Boolean isDev) {
		this.isDev = isDev;
	}
}