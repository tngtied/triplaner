package com.tngtied.triplaner.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.BadRequestException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.tngtied.triplaner.dto.InitiateTripRequestDTO;
import com.tngtied.triplaner.dto.NGeocodeDTO;
import com.tngtied.triplaner.dto.NGeocodeWithErrDTO;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Place;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.repository.DayPlanRepository;
import com.tngtied.triplaner.repository.PlanRepository;
import com.tngtied.triplaner.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trip")
public class MapController {

    //private static final String base_mapping = "/api/v1/trip";

    @Value("${NAVER-KEY}")
    private String naverKey;
    @Value("${NAVER-CLIENT}")
    private String naverClientId;

    @Autowired
    public TripService tripService;
//    @Autowired
//    public MemberService memberService;

    @Autowired
    public PlanRepository plan_repo;
    @Autowired
    public DayPlanRepository day_repo;

    @GetMapping("s")
    public List<TripThumbnailDTO> trip_list() {

        return plan_repo.findThumbnails();
    }

    @PostMapping()
    public Plan initiate_trip(@RequestBody InitiateTripRequestDTO trip_dto) {
        Plan plan_instance = new Plan();
        plan_instance.title = trip_dto.title;
        plan_instance.startDate = trip_dto.startDate;
        plan_instance.endDate = trip_dto.endDate;

        plan_repo.save(plan_instance);
        return (plan_instance);
    }

     @GetMapping("/{id}")
     public Plan get_plan(@PathVariable int id){
        Plan p = plan_repo.findById(id).orElse(null);
        if (p==null){System.out.println("found plan was null");}
        else{System.out.println(p);}
        return(p);
     }

     @PutMapping("/{id}/{date}")
     public DayPlan putTimePlan(@PathVariable long id, @PathVariable String date, @RequestBody TimePlan newTimePlan){
        LocalDate pathDate = LocalDate.parse(date);
        if (pathDate == null){return null;}
        //이거 프론트에 어케 전달할지몰르겟음
        System.out.println("Date search started with date: ");
        System.out.println(pathDate);
        DayPlan dayPlanFound = day_repo.findByParentPlanPlanIdAndPlanDate(id, pathDate);
        tripService.saveTimePlanToDayPlan(dayPlanFound, newTimePlan);
        return  dayPlanFound;
     }

    @PostMapping("/geocode")
    public Place addressToCoord(@RequestBody HashMap<String, Object> map) throws IOException {
        URL url = new URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(map.get("address").toString(), "utf-8"));
        System.out.println("url is: "+ url.toString());
         HttpURLConnection geoCon = (HttpURLConnection) url.openConnection();
         geoCon.setRequestProperty("Content-Type", "application/json");
         geoCon.setRequestMethod("GET");
         geoCon.setRequestProperty("X-NCP-APIGW-API-KEY-ID", naverClientId);
         geoCon.setRequestProperty("X-NCP-APIGW-API-KEY", naverKey);

         geoCon.connect();

         var gson = new Gson();
         InputStreamReader inputStreamReader;
         int responseCode = geoCon.getResponseCode();
         if (responseCode!=200 ){
             if(responseCode!= 400 && responseCode!=500) {
                 inputStreamReader = new InputStreamReader(geoCon.getErrorStream(), "utf-8");
                 String errorMessage =tripService.readFromReader(inputStreamReader);
                 System.out.println(errorMessage);
                 if (responseCode == 400){throw new BadRequestException();}
                 else{throw new UnexpectedException(errorMessage);}
             }
             else{
                 //response json from Naver geocode received
                 inputStreamReader = new InputStreamReader(geoCon.getInputStream(), "utf-8");
                 JsonReader jsonReader = new JsonReader(inputStreamReader);
                 jsonReader.setLenient(true);
                 NGeocodeWithErrDTO nGeocodeWithErrDTO = gson.fromJson(jsonReader, NGeocodeWithErrDTO.class);
                 throw new UnexpectedException(nGeocodeWithErrDTO.getErrorMessage());
             }
         }else{
             inputStreamReader = new InputStreamReader(geoCon.getInputStream(), "utf-8");
             JsonReader jsonReader = new JsonReader(inputStreamReader);
             jsonReader.setLenient(true);
             NGeocodeDTO nGeocodeDTO = gson.fromJson(jsonReader, NGeocodeDTO.class);
             System.out.println(nGeocodeDTO.toString());
             return (tripService.nGeoDTOToPlace(nGeocodeDTO));
         }
     }
}


