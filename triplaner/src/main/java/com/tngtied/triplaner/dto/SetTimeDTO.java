package com.tngtied.triplaner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.sql.Time;

@Data
public class SetTimeDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd")
    public Local date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "HH:mm")
    @Column(name = "TIME")
    public Time time;

}
