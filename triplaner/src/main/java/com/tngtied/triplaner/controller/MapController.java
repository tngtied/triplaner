package com.tngtied.triplaner.controller;

import java.util.List;

import com.tngtied.triplaner.*;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.repository.dayplan_repository;
import com.tngtied.triplaner.repository.plan_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MapController {

    private static final String base_mapping = "/api/v1/trip";

    @Autowired
    public TripService tripService;

    @Autowired
    public plan_repository plan_repo;

    @Autowired
    public dayplan_repository day_repo;

    @GetMapping(base_mapping + "s")
    public List<TripThumbnailDTO> trip_list() {
        return tripService.getTripList_query();
    }

    @PostMapping(base_mapping)
    public Plan initiate_trip(@RequestBody TripThumbnailDTO trip_dto) {
        Plan plan_instance = new Plan();
        plan_instance.title = trip_dto.title;
        plan_instance.startDate = trip_dto.startDate;
        plan_instance.endDate = trip_dto.endDate;

        plan_repo.save(plan_instance);
        return (plan_instance);
    }

     @GetMapping(base_mapping+"/{id}")
     public Plan get_plan(@PathVariable int id){
        Plan p = plan_repo.findById(id).orElse(null);
        if (p==null){System.out.println("found plan was null");}
        else{System.out.println(p.toString());}
        return(p);
     }

     @PutMapping(base_mapping+"/{id}/{date}")
     public DayPlan put_timeplan(@PathVariable int id, @PathVariable int date, @RequestBody TimePlan newPlan){

     }
}
