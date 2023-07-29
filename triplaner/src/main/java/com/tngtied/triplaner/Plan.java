package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    public List<DayPlan> dayplan_list;

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
