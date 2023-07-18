package com.tngtied.triplaner;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface dayplan_repository extends CrudRepository<DayPlan, Integer> {
    public static final String FIND_THUMBNAILS = "SELECT TITLE, START_DATE, END_DATE FROM plan_repository";

    @Query(value=FIND_THUMBNAILS, nativeQuery=true )
    public ArrayList<Object[]> findThumbnails();

}
