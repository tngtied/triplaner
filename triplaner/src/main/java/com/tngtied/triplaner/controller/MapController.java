package com.tngtied.triplaner.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.tngtied.triplaner.*;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Place;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.repository.dayplan_repository;
import com.tngtied.triplaner.repository.plan_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class MapController {

    private static final String base_mapping = "/api/v1/trip";

    @Value("${NAVER-KEY}")
    private String naverKey;
    @Value("${NAVER-CLIENT}")
    private String naverClientId;

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
     public DayPlan put_timeplan(@PathVariable long id, @PathVariable String date, @RequestBody TimePlan newTimePlan){
        LocalDate pathDate = LocalDate.parse(date);
        if (pathDate == null){return null;}
        //이거 프론트에 어케 전달할지몰르겟음
        System.out.println("Date search started with date: ");
        System.out.println(pathDate);
        DayPlan dayPlanFound = day_repo.findByParentPlanPlanIdAndPlanDate(id, pathDate);
        tripService.saveTimePlanToDayPlan(dayPlanFound, newTimePlan);
        return  dayPlanFound;
     }

     @PostMapping(base_mapping+"geocode")
    public Place addressToCoord(@RequestBody HashMap<String, Object> map) throws IOException {
        URL url = new URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + map.get("address"));
         HttpURLConnection geoCon = (HttpURLConnection) url.openConnection();
         geoCon.setRequestMethod("GET");
         geoCon.setRequestProperty("X-NCP-APIGW-API-KEY-ID", naverClientId);
         geoCon.setRequestProperty("X-NCP-APIGW-API-KEY", naverKey);

         geoCon.connect();

         if (geoCon.getResponseCode()!=200){

         }

         //print response body

     }
}
