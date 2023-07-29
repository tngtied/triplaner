package com.tngtied.triplaner;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface dayplan_repository extends CrudRepository<DayPlan, Integer> {

    @Query(name = "find_dayplan_with_id_date", nativeQuery = true)
    public DayPlan findByIdDate(@Param("id_in") int id_in, @Param("date_in") Date date_in );
}
