package com.tngtied.triplaner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        tripService.make_data();
        //return tripService.getTripList();
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

    // @GetMapping(base_mapping+"/{id}")
    // public Set<DayPlan> get_plan(@PathVariable int id){
    // return (plan_repo.findById(id)
    // .map(getDayplan_list)
    // .orElse(null));
    //
    // }

}
