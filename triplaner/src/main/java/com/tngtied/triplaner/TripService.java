package com.tngtied.triplaner;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  @Transactional
  public void make_data() {
    System.out.println("@@@@@@@@@@@@makedata invoked");
    Plan plan1 = new Plan();
    plan1.title = "kyungju";
    plan1.startDate = new Date(2023, 9, 13);
    plan1.endDate = new Date(2023, 8, 12);

    ArrayList<DayPlan> dayplan_list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      DayPlan dayplan = new DayPlan();
      dayplan.setParent(plan1);
      dayplan.planDate = new Date(2023, 9, i);

      if (i==0){
        for (int j=1; j<3; j++) {
          TimePlan timePlan = new TimePlan();
          timePlan.parentPlan = dayplan;
          timePlan.price = "19000";
          timePlan.time = Time.valueOf(LocalTime.now());
          timePlan.place.latitude = 0.123123*j;
          timePlan.place.longitude = 0.111111*j;
          place_repo.save(timePlan.place);
          time_repo.save(timePlan);
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
