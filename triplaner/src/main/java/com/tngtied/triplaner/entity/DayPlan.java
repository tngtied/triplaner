package com.tngtied.triplaner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DAYPLAN")
public class DayPlan {
    private static final String datePattern = "yyyy-MM-dd";

    public DayPlan() {
    }

    public void setParent(Plan parent_plan){
        this.parentPlan = parent_plan;
    }

    @Id
    @GeneratedValue
    @Column(name="DAYPLAN_ID")
    private Long dayplan_id;

    @Column(name="PLAN_DATE")
    @JsonProperty("Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
    public LocalDate planDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENT_PLAN")
    @JsonBackReference
    public Plan parentPlan;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="parentPlan")
    @JsonProperty("Timeplan")
    @JsonManagedReference
    public Set<TimePlan> timeplan_list= new HashSet();
}
