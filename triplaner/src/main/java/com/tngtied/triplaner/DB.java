package com.tngtied.triplaner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DB extends CrudRepository<Memo, Integer> {

    //directions api(위경도 사이를 오가는 경로 검색) geocoding api(사람이 쓰는 주소를 위경도로
    //
}
