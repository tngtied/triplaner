package com.tngtied.triplaner;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripThumbnailDTO {
    private static final String datePattern = "yyyy-MM-dd";

    public long id;
    public String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
    public Date startDate, endDate;
}
