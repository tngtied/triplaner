package com.tngtied.triplaner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="PLAN")
public class Plan {
    private static final String datePattern = "yyyy-MM-dd";
    @Id
    @GeneratedValue
    @Column(name = "PLAN_ID")
    @JsonProperty("id")
    public Long planId;
    //starts from 1, not 0
    //당연하지만 id와 같은 값들을 private으로 만들면 쿼리 조회 후 객체 리턴시
    //해당 변수가 json에 표현되지 않는다.
    //그럼 public으로 해야하는건가? 보안이슈?

    @Column(name="TITLE")
    public String title;

    @Column(name="START_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public LocalDate startDate;

    @Column(name="END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentPlan")
    @JsonManagedReference
    public List<DayPlan> dayplan_list;

    @Override
    public String toString(){
        return "Plan[" +
                "id: " + planId +
                ", title: " + title +
                ", startDate: " + startDate +
                ", endDate: " + endDate +
                "]";
    }
}
