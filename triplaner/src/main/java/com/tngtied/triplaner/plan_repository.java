package com.tngtied.triplaner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends CrudRepository<Plan, Integer> {

    @Query("select new com.tngtied.triplaner.TripThumbnailDTO(p.planId, p.title, p.startDate, p.endDate) from Plan p")
    public List<TripThumbnailDTO> findThumbnails();
}
