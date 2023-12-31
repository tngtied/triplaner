package com.tngtied.triplaner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tngtied.triplaner.dto.InitiateTripRequestDTO;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.service.TripService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class MapControllerTest {

    private static final String base_mapping = "/api/v1/trip";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public TripService tripService;

    String objectToJson(Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("plan dto post test")
    void planListPostTest() throws Exception {
        InitiateTripRequestDTO dto1 = new InitiateTripRequestDTO("kyungju", LocalDate.of(2023, 9, 13), LocalDate.of(2023, 9, 13));
        mockMvc.perform(post(base_mapping)
                .content(objectToJson(dto1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("plan-list get test")
    void planListGetTest() throws Exception {
        tripService.make_data();
        mockMvc.perform(get(base_mapping + "s"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("plan get test using id")
    void planGetTest() throws Exception{
        tripService.make_data();
        mockMvc.perform(get(base_mapping+"/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void putTimePlanTest() throws Exception {
        tripService.make_data();
        TimePlan tp = tripService.makeTimeplan(3);
        mockMvc.perform(put(base_mapping+"/1/2023-09-01")
                .content(objectToJson(tp))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void addressToCoord() throws Exception {
        JSONObject postContent = new JSONObject();
        postContent.put("address", "서울특별시 강남구 강남대로 310");

        mockMvc.perform(post(base_mapping+"geocode")
                .content(String.valueOf(postContent))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}