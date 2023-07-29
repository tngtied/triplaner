package com.tngtied.triplaner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends CrudRepository<Plan, Integer> {

    @Query("SELECT p FROM Plan p")
    public List<TripThumbnailDTO> findThumbnails();
}
