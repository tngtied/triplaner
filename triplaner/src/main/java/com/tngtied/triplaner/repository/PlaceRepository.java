package com.tngtied.triplaner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tngtied.triplaner.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

}
