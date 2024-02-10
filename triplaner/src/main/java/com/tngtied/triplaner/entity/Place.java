package com.tngtied.triplaner.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
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
