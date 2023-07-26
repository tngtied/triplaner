package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
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
    public Date startDate;

    @Column(name="END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public Date endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent_plan")
    @JsonManagedReference
    public ArrayList<DayPlan> dayplan_list = new ArrayList();

    @Override
    public String toString(){
        return "Plan[" +
                "id: " + planId +
                "title: " + title +
                "startDate: " + startDate +
                "endDate: " + endDate +
                "]";
    }
}
