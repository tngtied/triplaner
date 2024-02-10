package com.tngtied.triplaner.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Memo {
	private static final String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";
	public String message;
	@Id
	@GeneratedValue
	public int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
	public LocalDate createdate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = datePattern)
	public LocalDate touchdate;

}
