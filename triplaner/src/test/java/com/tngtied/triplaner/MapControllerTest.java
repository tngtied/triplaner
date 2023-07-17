package com.tngtied.triplaner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MapController.class)
//@Import(plan_repository.class)
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    public plan_repository plan_repo;
    private static final String base_mapping = "/api/v1/trip";

    public String objectToJson(Object obj){
        try{return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("plan-list post test")
    void planListPostTest() throws Exception {
        TripThumbnailDTO dto1 = new TripThumbnailDTO();
        dto1.title = "kyungju";
        dto1.startdate = new Date(2023,9,13);
        dto1.enddate = new Date(2023, 8, 12);

        mockMvc.perform(post(base_mapping)
                .content(objectToJson(dto1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("plan-list get test")
    void planListGetTest() throws Exception {
        Plan plan1 = plan_repo.findById(0).orElse(null);

        for (int i =0; i<3; i++){
            plan1.dayplan_list.add(new DayPlan(plan1));
        }
        plan_repo.save(plan1);

        mockMvc.perform(get(base_mapping))
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