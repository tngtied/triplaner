package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

public class TripThumbnailDTO {
    private static final String datePattern = "yyyy-MM-dd";
    public String title;

    @Id
    @GeneratedValue
    public int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public Date startDate, endDate;

    public TripThumbnailDTO(String title_, Date startDate_, Date endDate_){
        this.title = title_;
        this.startDate = startDate_;
        this.endDate = endDate_;

    }
}
