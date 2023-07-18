package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DAYPLAN")
public class DayPlan {
    public DayPlan(Plan parent_plan) {
        this.parent_plan = parent_plan;
    }

    @Id
    @GeneratedValue
    @Column(name="DAYPLAN_ID")
    private Long dayplan_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PLAN_ID")
    @JsonBackReference
    public Plan parent_plan;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="parent_dayplan")
    @JsonManagedReference
    public Set<TimePlan> timeplan_list= new HashSet();
}
