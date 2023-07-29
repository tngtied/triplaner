package com.tngtied.triplaner;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.NonNull;

public interface TripThumbnailDTO {

    long getPlanId();

    @NonNull
    String getTitle();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull
    Date getStartDate();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull
    Date getEndDate();
}
