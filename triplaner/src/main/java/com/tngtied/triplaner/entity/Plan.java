package com.tngtied.triplaner.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "PLAN")
public class Plan {
	private static final String datePattern = "yyyy-MM-dd";
	@Id
	@GeneratedValue
	@Column(name = "PLAN_ID")
	@JsonProperty("id")
	public Long planId;

	@Column(name = "TITLE")
	public String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTHOR")
	@JsonBackReference
	public Member author;

	@Column(name = "START_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
	public LocalDate startDate;

	@Column(name = "END_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
	public LocalDate endDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentPlan")
	@JsonManagedReference
	public List<DayPlan> dayplan_list;

	@Override
	public String toString() {
		return "Plan[" +
			"id: " + planId +
			", title: " + title +
			", startDate: " + startDate +
			", endDate: " + endDate +
			"]";
	}

	@Builder
	public Plan(String title, LocalDate startDate, LocalDate endDate, Member author) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.author = author;
		this.dayplan_list = new ArrayList<>();
	}
}
