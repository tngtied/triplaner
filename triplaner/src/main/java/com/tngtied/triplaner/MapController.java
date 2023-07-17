package com.tngtied.triplaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

@RestController
public class MapController {

    @Autowired
    public plan_repository plan_repo;
    private static final String base_mapping = "/api/v1/trip";


    @GetMapping(base_mapping)
    public Iterable<Plan> trip_list(){
        Iterable<Plan> thumbnail_instance = plan_repo.findAll();
        return (thumbnail_instance);
        //형태가 이게 맞는지 확인 한 번 해봐야 함
    }

    @PostMapping(base_mapping)
    public Plan initiate_trip(@RequestBody TripThumbnailDTO trip_dto){
        Plan plan_instance = new Plan();
        plan_instance.title = trip_dto.title;
        plan_instance.StartDate = trip_dto.startdate;
        plan_instance.EndDate = trip_dto.enddate;
        plan_repo.save(plan_instance);
        return (plan_instance);
    }

//    @GetMapping(base_mapping+"/{id}")
//    public Set<DayPlan> get_plan(@PathVariable int id){
//        return (plan_repo.findById(id)
//                        .map(getDayplan_list)
//                                .orElse(null));
//
//    }


}
