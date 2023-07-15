package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table (name = "TIMEPLAN")
public class TimePlan {

    @Id
    @GeneratedValue
    @Column(name="TIMPLAN_ID")
    public int timeplan_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "HH:mm")
    public Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="DAYPLAN_ID")
    public DayPlan dayplan_instance;

    class place{
        public double latitude;
        public double longitude;
    }
}
