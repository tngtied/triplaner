package com.tngtied.triplaner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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

    public String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("plan dto post test")
    void planListPostTest() throws Exception {
        TripThumbnailDTO dto1 = new TripThumbnailDTO("kyungju", new Date(2023, 9, 13), new Date(2023, 9, 13));
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
    void initiate_trip() {
    }
}