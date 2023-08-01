package com.tngtied.triplaner;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tngtied.triplaner.dto.TripThumbnailDTO;
import com.tngtied.triplaner.entity.DayPlan;
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


  public List<TripThumbnailDTO> getTripList() {
    ArrayList<TripThumbnailDTO> list = new ArrayList<>();

    for (Plan p : plan_repo.findAll()) {
      list.add(new TripThumbnailDTO(p.title, p.startDate, p.endDate));
      System.out.println(p.toString());
    }

    return list;
  }

  public List<TripThumbnailDTO> getTripList_query(){
    List<TripThumbnailDTO> list = plan_repo.findThumbnails();
    return list;
  }

  public Date dateParser(String str){
    DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    sdf.setLenient(true);
    try{return sdf.parse(str);}
    catch(ParseException e){return null;}
  }

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
    plan1.startDate = new Date(2023, 9, 13);
    plan1.endDate = new Date(2023, 8, 12);

    ArrayList<DayPlan> dayplan_list = new ArrayList<>();
    for (int i = 1; i < 3; i++) {
      DayPlan dayplan = new DayPlan();
      dayplan.setParent(plan1);
      dayplan.planDate = new Date(2023, 9, i);

      if (i==0){
        for (int j=1; j<3; j++){
          TimePlan tp = makeTimeplan(j);
          tp.parentPlan = dayplan;
          time_repo.save(tp);
        }
      }
      day_repo.save(dayplan);
      dayplan_list.add(dayplan);
    }

    plan1.dayplan_list = dayplan_list;
    plan_repo.save(plan1);
    System.out.println(plan1);
  }
}
