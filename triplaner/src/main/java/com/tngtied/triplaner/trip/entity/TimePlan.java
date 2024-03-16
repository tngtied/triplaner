package com.tngtied.triplaner.trip.entity;

import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TIMEPLAN")
@SequenceGenerator(sequenceName = "TIMEPLAN_SEQ", name = "TimeplanSeq", allocationSize = 50, initialValue = 1)
public class TimePlan {
	public TimePlan() {
		this.place = new Place();
	}

	@Id
	@GeneratedValue
	@Column(name = "TIMEPLAN_ID")
	private Long timeplanId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "TIME")
	public Time time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_PLAN")
	@JsonBackReference
	public DayPlan parentPlan;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "parentPlan")
	@JsonManagedReference
	public Place place;

	@Column(name = "PRICE")
	public String price;
}
