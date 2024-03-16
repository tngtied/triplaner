package com.tngtied.triplaner.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tngtied.triplaner.trip.entity.TimePlan;

@Repository
public interface TimePlanRepository extends JpaRepository<TimePlan, Integer> {

}
