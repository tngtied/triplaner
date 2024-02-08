package com.tngtied.triplaner.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserSignupDTO {
	@JsonProperty
	@Column(unique = true, name = "USERNAME")
	@NotNull
	@Size(min = 2, max = 8)
	String username;

	@JsonProperty
	@Length(min = 8, max = 16)
	@NotNull
	String password;

	@JsonProperty
	@NotNull
	@Email
	@Column(unique = true, name = "EMAIL")
	String email;

	public UserSignupDTO(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
}
