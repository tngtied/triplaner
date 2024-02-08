package com.tngtied.triplaner.dto;

import java.sql.Time;

import javax.persistence.Column;

import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SetTimeDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Local date;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "TIME")
	public Time time;

}
