package com.tngtied.triplaner.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@RequiredArgsConstructor
public class UserSignupDTO {
	@JsonProperty
	@Column(unique = true, name = "USERNAME")
	@NotNull
	@Size(min = 2, max = 20)
	String username;

	@JsonProperty
	@Length(min = 8, max = 20)
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
