package com.tngtied.triplaner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface plan_repository extends CrudRepository<Plan, Integer> {

}
