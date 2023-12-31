package com.tngtied.triplaner.repository;

import com.tngtied.triplaner.entity.TimePlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimePlanRepository extends CrudRepository<TimePlan, Integer> {

}
