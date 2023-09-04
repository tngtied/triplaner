package com.tngtied.triplaner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.tngtied.triplaner.dto.NGeocodeDTO;
import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Place;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.repository.dayplan_repository;
import com.tngtied.triplaner.repository.place_repository;
import com.tngtied.triplaner.repository.plan_repository;
import com.tngtied.triplaner.repository.timeplan_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

  @Autowired
  public plan_repository plan_repo;
  @Autowired
  public dayplan_repository day_repo;
  @Autowired
  public timeplan_repository time_repo;
  @Autowired
  public place_repository place_repo;


  public TimePlan makeTimeplan(int j){
    TimePlan timePlan = new TimePlan();
    timePlan.price = "19000";
    timePlan.time = Time.valueOf(LocalTime.now());
    timePlan.place.latitude = 0.123123*j;
    timePlan.place.longitude = 0.111111*j;
    place_repo.save(timePlan.place);
    time_repo.save(timePlan);
    return timePlan;
  }

  @Transactional
  public void make_data() {
    System.out.println("println: make_data invoked");
    Plan plan1 = new Plan();
    plan1.title = "kyungju";
    plan1.startDate = LocalDate.of(2023, 8, 13);
    plan1.endDate =  LocalDate.of(2023, 9, 12);

    ArrayList<DayPlan> dayplan_list = new ArrayList<>();
    for (int i = 1; i < 3; i++) {
      DayPlan dayplan = new DayPlan();
      dayplan.setParent(plan1);
      dayplan.planDate = LocalDate.of(2023, 9, i);

      if (i==1){
        for (int j=1; j<3; j++){
          TimePlan tp = makeTimeplan(j);
          tp.parentPlan = dayplan;
          time_repo.save(tp);
        }
      }
      day_repo.save(dayplan);

      System.out.println("Dayplan saved as: ");
      System.out.println(dayplan.planDate);

      dayplan_list.add(dayplan);
    }

    plan1.dayplan_list = dayplan_list;
    plan_repo.save(plan1);
    System.out.println(plan1);
  }

  public String readFromReader(InputStreamReader r) throws IOException {
    BufferedReader br = new BufferedReader(r);
    String result="";
    try{
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      result = response.toString();
    }catch (Exception e){
      System.out.println("error occured at readFromReader");
      System.out.println(e);
    }finally {
      br.close();
    }
    return result;
  }

  public void saveTimePlanToDayPlan(DayPlan d, TimePlan t){
    place_repo.save(t.place);
    t.parentPlan = d;
    time_repo.save(t);
    //date validation needed
    d.timeplan_list.add(t);
    day_repo.save(d);
  }

  public Place nGeoDTOToPlace(NGeocodeDTO geoDTO){
    Place place = new Place();
    place.latitude = Double.parseDouble(geoDTO.getAddresses()[0].x);
    place.longitude = Double.parseDouble(geoDTO.getAddresses()[0].y);
    place_repo.save(place);
    return place;
  }
}
