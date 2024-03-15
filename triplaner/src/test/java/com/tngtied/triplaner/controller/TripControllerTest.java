package com.tngtied.triplaner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tngtied.triplaner.UserRole;
import com.tngtied.triplaner.dto.InitiateTripRequestDTO;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.presentation.authentication.jwt.JwtTokenProvider;
import com.tngtied.triplaner.presentation.authentication.jwt.TokenInfo;
import com.tngtied.triplaner.repository.UserRepository;
import com.tngtied.triplaner.service.TripService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class TripControllerTest {

	private static final String base_mapping = "/api/v1/trip";

	@Value("${base.path}")
	private String base_path;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	public TripService tripService;

	@Autowired
	public JwtTokenProvider jwtTokenProvider;

	@Autowired
	public UserRepository userRepository;

	String objectToJson(Object obj) {
		try {
			return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	String getAccessToken(String username) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		Member member = new Member(username, "password", "USER", "email@gmail.com");
		userRepository.save(member);
		TokenInfo tokenInfo = jwtTokenProvider.generateToken(member);
		return tokenInfo.getAccessToken();
	}

	@Test
	@DisplayName("plan dto post test")
	void planListPostTest() throws Exception {
		InitiateTripRequestDTO dto1 = new InitiateTripRequestDTO("kyungju", LocalDate.of(2023, 9, 13),
			LocalDate.of(2023, 9, 13));
		String accessToken = getAccessToken("user1");
		mockMvc.perform(post(base_mapping)
				.header("Authorization", "Bearer " + accessToken)
				.content(objectToJson(dto1))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("plan-list get test")
	void planListGetTest() throws Exception {
		String accessToken = getAccessToken("user2");
		tripService.make_data("user2");
		mockMvc.perform(get(base_mapping + "/list")
				.header("Authorization", "Bearer " + accessToken)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("plan get test using id")
	void planGetTest() throws Exception {
		String accessToken = getAccessToken("user3");
		Long planId = tripService.make_data("user3").getPlanId();
		mockMvc.perform(get(base_mapping + "/" + planId)
				.header("Authorization", "Bearer " + accessToken)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void putTimePlanTest() throws Exception {
		String accessToken = getAccessToken("user4");
		Long planId = tripService.make_data("user4").getPlanId();
		TimePlan timePlan = tripService.makeTimeplan(3);
		mockMvc.perform(put(base_mapping + "/" + planId + "/2023-09-01")
				.header("Authorization", "Bearer " + accessToken)
				.content(objectToJson(timePlan))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void addressToCoord() throws Exception {
		JSONObject postContent = new JSONObject();
		postContent.put("address", "서울특별시 서대문구 연세로 50");
		String accessToken = getAccessToken("user5");
		mockMvc.perform(post(base_mapping + "/geocode")
				.header("Authorization", "Bearer " + accessToken)
				.content(String.valueOf(postContent))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void getRouteTest() throws Exception {
		String accessToken = getAccessToken("user6");

		String routeRequestString = "{\"startX\": \"127.0315025\", \"startY\": \"37.4909898\", \"endX\":\"126.9390292\", \"endY\": \"37.5671131\", \"date\": \"2024-02-04\", \"time\":\"11:00\"}";
		mockMvc.perform(get(base_mapping + "/route")
				.header("Authorization", "Bearer " + accessToken)
				.content(routeRequestString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}
}