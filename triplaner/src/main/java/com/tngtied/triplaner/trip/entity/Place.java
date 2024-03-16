package com.tngtied.triplaner.trip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(sequenceName = "PLACE_SEQ", name = "PlaceSeq", allocationSize = 50, initialValue = 1)
public class Place {
	@Id
	@GeneratedValue
	private Long placeId;

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "PARENT_PLAN")
	@JsonBackReference
	private TimePlan parentPlan;
	@JsonProperty("lat")
	public double latitude;
	@JsonProperty("lng")
	public double longitude;
}
