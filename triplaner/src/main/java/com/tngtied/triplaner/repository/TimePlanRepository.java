package com.tngtied.triplaner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tngtied.triplaner.entity.TimePlan;

@Repository
public interface TimePlanRepository extends JpaRepository<TimePlan, Integer> {

}
