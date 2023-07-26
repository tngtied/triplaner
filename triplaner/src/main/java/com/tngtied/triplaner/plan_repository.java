package com.tngtied.triplaner;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends CrudRepository<Plan, Integer> {


    public static final String FIND_THUMBNAILS = "SELECT new com.tngtied.triplaner.TripThumbnailDTO(p.TITLE, p.START_DATE, p.END_DATE) FROM Plan p";

    @Query(value=FIND_THUMBNAILS, nativeQuery=true )
    public List<TripThumbnailDTO> findThumbnails();

}
