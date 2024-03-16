package com.tngtied.triplaner.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tngtied.triplaner.trip.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

}
