package com.tngtied.triplaner.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Plan;

@Repository
public interface DayPlanRepository extends CrudRepository<DayPlan, Integer> {

	//    String ByIdAndDate = "SELECT * " +
	//            "FROM DayPlan d " +
	//            "WHERE " +
	//            //"(SELECT PLAN_ID FROM (SELECT d.PARENT_PLAN))=:id_in AND " +
	//            "d.PARENT_PLAN.PLAN_ID=:id_in AND " +
	//            "d.PLAN_DATE=:date_in";
	//    @Query(value = ByIdAndDate, nativeQuery = true)
	public DayPlan findByParentPlanAndPlanDate(Plan plan, LocalDate planDate);
}
