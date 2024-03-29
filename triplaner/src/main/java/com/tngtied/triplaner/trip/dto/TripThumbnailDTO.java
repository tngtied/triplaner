package com.tngtied.triplaner.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public interface TripThumbnailDTO {

//    int getId();
    String getTitle();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    LocalDate getStartDate();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    LocalDate getEndDate();
}
