package com.tngtied.triplaner.repository;

import com.tngtied.triplaner.entity.DayPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface dayplan_repository extends CrudRepository<DayPlan, Integer> {

    String ByIdAndDate = "SELECT * " +
            "FROM DayPlan d " +
            "WHERE " +
            //"(SELECT PLAN_ID FROM (SELECT d.PARENT_PLAN))=:id_in AND " +
            "d.PARENT_PLAN.PLAN_ID=:id_in AND " +
            "d.PLAN_DATE=:date_in";
    @Query(value = ByIdAndDate, nativeQuery = true)
    public DayPlan findByIdAndDate(@Param("id_in") int id_in, @Param("date_in") Date date_in );
}
