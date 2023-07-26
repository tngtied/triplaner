package com.tngtied.triplaner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MapController.class)
class MapControllerTest {

    private static final String base_mapping = "/api/v1/trip";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public plan_repository plan_repo;

    @MockBean
    public dayplan_repository dayplan_repo;

    @MockBean
    private TripService tripService;

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
        mockMvc.perform(get(base_mapping + "s"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void trip_list() {
    }

    @Test
    void initiate_trip() {
    }
}