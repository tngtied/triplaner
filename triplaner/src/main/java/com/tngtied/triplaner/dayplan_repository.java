package com.tngtied.triplaner;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface dayplan_repository extends CrudRepository<DayPlan, Integer> {
}
