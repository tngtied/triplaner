package com.tngtied.triplaner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class Place {
	@Id
	@GeneratedValue
	private Long placeId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PARENT_PLAN")
	@JsonBackReference
	private TimePlan parentPlan;
	@JsonProperty("lat")
	public double latitude;
	@JsonProperty("lng")
	public double longitude;

	// TODO 주소와 해당 장소명 저장하기
}
