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
    public class addressDTO {
        String roadAddress;
        String jibunAddress;
        String englishAddress;
        public String x;
        public String y;
        double distance;
    }
    addressDTO addresses[];
}
