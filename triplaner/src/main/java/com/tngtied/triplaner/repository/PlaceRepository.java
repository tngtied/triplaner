package com.tngtied.triplaner.repository;

import com.tngtied.triplaner.entity.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Integer> {

}
