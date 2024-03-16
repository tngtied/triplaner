package com.tngtied.triplaner.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NGeocodeDTO {
	String status;
	String errorMessage;

	static class metaDTO {
		Number totalCount;
		Number page;
		Number count;
	}

	metaDTO meta;

	@Data
	static public class addressDTO {
		String roadAddress;
		String jibunAddress;
		String englishAddress;
		public String x;
		public String y;
		double distance;
	}

	addressDTO addresses[];
}
