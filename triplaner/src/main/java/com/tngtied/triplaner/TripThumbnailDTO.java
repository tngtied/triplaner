package com.tngtied.triplaner;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface TripThumbnailDTO {

    long getId();

    String getTitle();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getStartDate();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getEndDate();
}
