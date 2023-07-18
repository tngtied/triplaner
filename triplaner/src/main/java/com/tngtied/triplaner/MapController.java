package com.tngtied.triplaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@RestController
public class MapController {

    @Autowired
    public plan_repository plan_repo;
    @Autowired
    public  dayplan_repository day_repo;
    private static final String base_mapping = "/api/v1/trip";


    @GetMapping(base_mapping+"s")
    public Iterable<Object[]> trip_list(){
        Plan plan1 = new Plan();
        plan1.title = "kyungju";
        plan1.startDate = new Date(2023,9,13);
        plan1.endDate = new Date(2023, 8, 12);
        plan1.dayplan_list = new ArrayList<>();

        for (int i =0; i<3; i++){
            DayPlan dayplan = new DayPlan(plan1);
            day_repo.save(dayplan);
            plan1.dayplan_list.add(dayplan);
        }

        plan_repo.save(plan1);

        Iterable<Plan> thumbnail_list = plan_repo.findAll();
        Plan temp = plan_repo.findById(0).orElse(null);
        return (thumbnail_list);
        //형태가 이게 맞는지 확인 한 번 해봐야 함
    }

    @PostMapping(base_mapping)
    public Plan initiate_trip(@RequestBody TripThumbnailDTO trip_dto){
        Plan plan_instance = new Plan();
        plan_instance.title = trip_dto.title;
        plan_instance.startDate = trip_dto.startDate;
        plan_instance.endDate = trip_dto.endDate;

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
