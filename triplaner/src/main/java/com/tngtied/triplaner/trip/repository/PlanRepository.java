package com.tngtied.triplaner.trip.repository;

import java.util.List;

import com.tngtied.triplaner.trip.entity.Plan;
import com.tngtied.triplaner.trip.dto.TripThumbnailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query(value = "select p1_0.END_DATE AS endDate,p1_0.START_DATE AS startDate,p1_0.TITLE from PLAN p1_0 left join MEMBER a1_0 on a1_0.USER_ID=p1_0.AUTHOR where a1_0.USERNAME= :user", nativeQuery = true)
    public List<TripThumbnailDTO> findThumbnails(@Param("user") String user);

//    public List<Plan> findByAuthor_Username(String username);

}
