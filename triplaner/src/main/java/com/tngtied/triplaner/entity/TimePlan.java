package com.tngtied.triplaner.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TIMEPLAN")
public class TimePlan {
	public TimePlan() {
		this.place = new Place();
	}

	@Id
	@GeneratedValue
	@Column(name = "TIMEPLAN_ID")
	private Long timeplan_id;

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
