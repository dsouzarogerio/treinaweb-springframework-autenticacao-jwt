package br.com.treinaweb.javajobs.dto;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.treinaweb.javajobs.enums.Status;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    @NotEmpty
    @Size(min = 3, max = 80)
    private String title;

    private String description;

    @NotEmpty
    @Size(min = 3, max = 80)
    private String company;

    @NotEmpty
    @Size(min = 3, max = 255)
    @Email
    private String email;

    @NotEmpty
    private Set<String> techs;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getTechs() {
		return techs;
	}

	public void setTechs(Set<String> techs) {
		this.techs = techs;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
