package com.tngtied.triplaner.repository;

import java.util.List;

import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query(value = "SELECT p.TITLE, p.START_DATE AS startDate, p.END_DATE AS endDate " +
            "FROM Plan p " +
            "WHERE p.author = :user", nativeQuery = true)
    public List<TripThumbnailDTO> findThumbnails(@Param("user") Member user);

    public List<Plan> findByAuthor_Username(String username);

}
