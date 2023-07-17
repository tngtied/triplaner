package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="PLAN")
public class Plan {
    private static final String datePattern = "yyyy-MM-dd";
    @Id
    @GeneratedValue
    @Column(name = "PLAN_ID")
    private Long planId;

    @Column(name="TITLE")
    public String title;

    @Column(name="START_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public Date StartDate;

    @Column(name="END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public Date EndDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent_plan")
    public Set<DayPlan> dayplan_list = new HashSet();

}
