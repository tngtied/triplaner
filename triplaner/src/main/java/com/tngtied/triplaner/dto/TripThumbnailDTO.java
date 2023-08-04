package com.tngtied.triplaner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TripThumbnailDTO {
    private static final String datePattern = "yyyy-MM-dd";
    public String title;

    @Id
    @GeneratedValue
    public int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public LocalDate endDate;

    public TripThumbnailDTO(String title_, LocalDate startDate_, LocalDate endDate_){
        this.title = title_;
        this.startDate = startDate_;
        this.endDate = endDate_;

    }
}
