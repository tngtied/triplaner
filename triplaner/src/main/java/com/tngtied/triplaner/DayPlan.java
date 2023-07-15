package com.tngtied.triplaner;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DAYPLAN")
public class DayPlan {

    @Id
    @GeneratedValue
    @Column(name="DAYPLAN_ID")
    private Long dayplan_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PLAN_ID")
    public Plan plan_instance;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="dayplan_instance")
    public Set<TimePlan> timeplan_list= new HashSet();

}
