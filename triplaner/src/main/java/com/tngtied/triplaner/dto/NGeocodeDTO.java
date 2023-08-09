package com.tngtied.triplaner.dto;

import lombok.Data;

import java.lang.reflect.Array;

@Data
public class NGeocodeDTO {
    String status;
    String errorMessage;


    class metaDTO{
        Number totalCount;
        Number page;
        Number count;
    }
    metaDTO meta;

    @Data
    class addressDTO {
        String roadAddress;
        String jibunAddress;
        String englishAddress;
        String x;
        String y;
        double distance;
    }
    addressDTO addresses[];
}
