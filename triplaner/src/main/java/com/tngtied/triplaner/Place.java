package com.tngtied.triplaner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Place{
    @Id
    @GeneratedValue
    private Long placeId;

    @OneToOne
    @JoinColumn(name ="PARENT_PLAN")
    @JsonBackReference
    private TimePlan parentPlan;
    @JsonProperty("lat")
    public double latitude;
    @JsonProperty("alt")
    public double longitude;
}
