package com.tngtied.triplaner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DB extends CrudRepository<Memo, Integer> {
}
