package com.tngtied.triplaner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Place{
    @Id
    @GeneratedValue
    private Long placeId;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name ="PARENT_PLAN")
    @JsonBackReference
    private TimePlan parentPlan;
    @JsonProperty("lat")
    public double latitude;
    @JsonProperty("lng")
    public double longitude;
}
