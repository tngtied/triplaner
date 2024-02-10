package com.tngtied.triplaner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tngtied.triplaner.dto.UserLoginDTO;
import com.tngtied.triplaner.dto.UserSignupDTO;
import com.tngtied.triplaner.repository.UserRepository;
import com.tngtied.triplaner.service.UserDetailsServiceImpl;

import jakarta.transaction.Transactional;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@AutoConfigureRestDocs
class UserControllerTest {

	@Value("${base.path}")
	private String base_path;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDetailsServiceImpl userService;

	@MockBean
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	UserSignupDTO makeValidUser() {
		UserSignupDTO siteUser = new UserSignupDTO("test", "password", "email@gmail.com");
		return siteUser;
	}

	String objectToJson(Object obj) {
		try {
			return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void signupMvc(String jsonContent) throws Exception {

		mockMvc.perform(post(base_path + "/user/signup")
				.content(jsonContent)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("Test: post signup valid")
	void signupTestValid() throws Exception {
		String siteUser = objectToJson(makeValidUser());
		mockMvc.perform(post(base_path + "/user/signup")
				.content(siteUser)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("Test: post signup invalid field [username field]")
	void signupTestInvalidUsername() throws Exception {
		String jsonString = "{\"username\": \"a\", \"password\": \"password\", \"email\":\"email@gmail.com\"}";
		signupMvc(jsonString);

		//response body
		//{"hasErr":true,"fieldErrorList":[{"field":"username","error":"Size"}],"objectErrorList":[]}
	}

	@Test
	@DisplayName("Test: post signup invalid field [password field]")
	void signupTestInvalidPassword() throws Exception {
		String jsonString = "{\"username\": \"username\", \"password\": \"pass\", \"email\":\"email@gmail.com\"}";
		signupMvc(jsonString);

		//response body
		//{"hasErr":true,"fieldErrorList":[{"field":"password","error":"Size"}],"objectErrorList":[]}
	}

	@Test
	@DisplayName("Test: post signup duplicate field [username]")
	void signupTestDuplicateUsername() throws Exception {
		signupMvc(objectToJson(makeValidUser()));
		signupMvc(objectToJson(makeValidUser()));
		//        String jsonString = "{\"username\": \"test\", \"password\": \"password1\", \"email\":\"emailmail@gmail.com\"}";
		//        signupMvc(jsonString);
	}

	@Test
	@DisplayName("Test: post signup duplicate field [email]")
	void signupTestDuplicateEmail() throws Exception {
		signupMvc(objectToJson(makeValidUser()));
		String jsonString = "{\"username\": \"test1\", \"password\": \"password1\", \"email\":\"email@gmail.com\"}";
		signupMvc(jsonString);
	}

	@Test
	void loginTestSuccess() throws Exception {

		signupMvc(objectToJson(makeValidUser()));
		System.out.println(">> make valid user complete");
		UserLoginDTO loginDTO = new UserLoginDTO("test", "password");

		mockMvc.perform(post(base_path + "/user/login")
				.content(objectToJson(loginDTO))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void loginTestFail() throws Exception {

		UserLoginDTO loginDTO = new UserLoginDTO("test", "password");
		mockMvc.perform(post(base_path + "/user/login")
				.content(objectToJson(loginDTO))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}

	@Test
	void SessionTestSuccess() throws Exception {

	}

}