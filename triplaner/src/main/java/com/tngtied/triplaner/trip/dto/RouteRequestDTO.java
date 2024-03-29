package com.tngtied.triplaner.trip.dto;

import lombok.Data;

@Data
public class RouteRequestDTO {
	String startX;
	String startY;
	String endX;
	String endY;
	int count;
	int lang;
	String format;
	String searchDttm;

	public RouteRequestDTO(String startX, String startY, String endX, String endY, int count, int lang, String format,
		String date, String time) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.count = count;
		this.lang = lang;
		this.format = format;
		this.searchDttm = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10) + time.substring(0, 2)
			+ time.substring(3, 5);
		System.out.println(">> searchDttm" + searchDttm);
	}

}
