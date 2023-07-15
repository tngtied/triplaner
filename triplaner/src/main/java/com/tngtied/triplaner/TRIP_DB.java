package com.tngtied.triplaner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TRIP_DB extends CrudRepository<Plan, Integer> {

}
