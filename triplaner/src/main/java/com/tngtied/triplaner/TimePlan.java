package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Table (name = "TIMEPLAN")
public class TimePlan {
    public TimePlan(){
        this.place = new Place();
    }

    @Id
    @GeneratedValue
    @Column(name="TIMEPLAN_ID")
    private int timeplan_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "HH:mm")
    @Column(name = "TIME")
    public Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="PARENT_PLAN")
    @JsonBackReference
    public DayPlan parentPlan;


    @OneToOne(fetch = FetchType.EAGER, mappedBy="parentPlan")
    @JsonManagedReference
    public Place place;

    @Column(name="PRICE")
    public String price;
}
