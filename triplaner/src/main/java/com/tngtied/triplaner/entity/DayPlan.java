package com.tngtied.triplaner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedNativeQuery(
        name="find_dayplan_with_id_date",
        query="SELECT d" +
                "FROM DayPlan d " +
                "WHERE (SELECT PLAN_ID FROM d.PARENT_PLAN) := id_in " +
                "AND d.PLAN_DATE := date_in"
)
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
    public Date planDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENT_PLAN")
    @JsonBackReference
    private Plan parentPlan;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="parentPlan")
    @JsonProperty("Timeplan")
    @JsonManagedReference
    public Set<TimePlan> timeplan_list= new HashSet();
}
