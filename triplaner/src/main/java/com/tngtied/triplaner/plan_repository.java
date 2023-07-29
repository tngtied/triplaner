package com.tngtied.triplaner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends JpaRepository<Plan, Integer> {

    @Query(name="find_trip_thumbnail_dto", nativeQuery=true )
    public List<TripThumbnailDTO> findThumbnails();

}
