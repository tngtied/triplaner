package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table (name = "TIMEPLAN")
public class TimePlan {

    @Id
    @GeneratedValue
    @Column(name="TIMEPLAN_ID")
    public int timeplan_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "HH:mm")
    @Column(name = "TIME")
    public Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="DAYPLAN_ID")
    @JsonBackReference
    public DayPlan parent_dayplan;

    class place{
        public double latitude;
        public double longitude;
    }
}
