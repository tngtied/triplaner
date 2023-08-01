package com.tngtied.triplaner.repository;

import java.util.List;

import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends JpaRepository<Plan, Integer> {

    @Query(name="find_trip_thumbnail_dto", nativeQuery=true )
    public List<TripThumbnailDTO> findThumbnails();

}
