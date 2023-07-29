package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@NamedNativeQuery(
        name="find_trip_thumbnail_dto",
        query="SELECT" +
                " p.TITLE AS title, " +
                " p.START_DATE AS startDate, " +
                " p.END_DATE AS endDate" +
                " FROM Plan p",
        resultSetMapping = "trip_thumbnail_dto"
)
@SqlResultSetMapping(
        name="trip_thumbnail_dto",
        classes = @ConstructorResult(
                targetClass = TripThumbnailDTO.class,
                columns = {
                        @ColumnResult(name="title", type=String.class),
                        @ColumnResult(name="startDate", type= Date.class),
                        @ColumnResult(name="endDate", type=Date.class)
                }
        )
)
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
    public Date startDate;

    @Column(name="END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=datePattern)
    public Date endDate;

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
