package com.tngtied.triplaner.trip.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "DAYPLAN")
@SequenceGenerator(sequenceName = "DAYPLAN_SEQ", name = "DayplanSeq", allocationSize = 50, initialValue = 1)
public class DayPlan {
	private static final String datePattern = "yyyy-MM-dd";

	public DayPlan() {
	}

	public void setParent(Plan parentPlan) {
		this.parentPlan = parentPlan;
	}

	@Id
	@GeneratedValue
	@Column(name = "DAYPLAN_ID")
	private Long dayplanId;

	@Column(name = "PLAN_DATE")
	@JsonProperty("Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
	public LocalDate planDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_PLAN")
	@JsonBackReference
	public Plan parentPlan;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parentPlan")
	@JsonProperty("Timeplan")
	@JsonManagedReference
	public Set<TimePlan> timeplanList = new HashSet<>();

	@Builder
	public DayPlan(LocalDate planDate, Plan parentPlan) {
		this.planDate = planDate;
		this.parentPlan = parentPlan;
	}
}
