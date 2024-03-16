package com.tngtied.triplaner.trip.entity;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class Route {
	class RequestParameters {
		int busCount;
		int expressbusCount;
		int subwayCount;
		int airplaneCount;
		String locale;
		String endY;
		String endX;
		int wideareaRouteCount;
		int subwayBusCount;
		String startY;
		String startX;
		int ferryCount;
		int trainCount;
		String reqDttm;
	}

	RequestParameters requestParameters;

	class Plan {

		class Iternaries {
			int totalTime;
			int transferCount;
			int totalWalkDistance;
			int totalDistance;
			int totalWalkTime;

			JsonNode fare;

			class Legs {
				int distance;
				int sectionTime;
				String mode;

				class Start {
					String name;
					String lon;
					String lat;
				}

				class End {
					String name;
					String lon;
					String lat;
				}

				Start start;
				End end;

				class Steps {
					double distance;
					String streetName;
					String description;
					String linestring;
				}

				Steps steps;

				class PassShape {
					String linestring;

				}

				PassShape passShape;

				class PassStopList {
					class Stations {
						Number index;
						String stationID;
						String stationName;
						String lon;
						String lat;
					}

				}

			}

			List<Legs> legs;

		}
	}

}