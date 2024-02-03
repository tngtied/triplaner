package com.tngtied.triplaner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import com.tngtied.triplaner.dto.NGeocodeDTO;
import com.tngtied.triplaner.entity.*;
import com.tngtied.triplaner.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final PlanRepository planRepository;
	private final DayPlanRepository dayPlanRepository;
	private final TimePlanRepository timePlanRepository;
	private final PlaceRepository placeRepository;
	private final UserRepository userRepository;

	public TimePlan makeTimeplan(int j) {
		TimePlan timePlan = new TimePlan();
		timePlan.price = "19000";
		timePlan.time = Time.valueOf(LocalTime.now());
		timePlan.place.latitude = 0.123123 * j;
		timePlan.place.longitude = 0.111111 * j;
		placeRepository.save(timePlan.place);
		timePlanRepository.save(timePlan);
		return timePlan;
	}

	@Transactional
	public void makeDummyData() {
		logger.debug("println: make_data invoked");
		Optional<Member> optionalMember = userRepository.findByUsername("username");
		if (optionalMember.isEmpty()) {
			throw new NotFoundException();
		}
		Member member = optionalMember.get();

		Plan plan1 = new Plan();
		plan1.title = "kyungju";
		plan1.startDate = LocalDate.of(2023, 8, 13);
		plan1.endDate = LocalDate.of(2023, 9, 12);
		plan1.author = member;

		ArrayList<DayPlan> dayPlanList = new ArrayList<>();
		for (int i = 1; i < 3; i++) {
			DayPlan dayplan = new DayPlan();
			dayplan.setParent(plan1);
			dayplan.planDate = LocalDate.of(2023, 9, i);

			if (i == 1) {
				for (int j = 1; j < 3; j++) {
					TimePlan tp = makeTimeplan(j);
					tp.parentPlan = dayplan;
					timePlanRepository.save(tp);
				}
			}
			dayPlanRepository.save(dayplan);

			System.out.println("Dayplan saved as: ");
			System.out.println(dayplan.planDate);

			dayPlanList.add(dayplan);
		}

		plan1.dayplan_list = dayPlanList;
		planRepository.save(plan1);
		logger.debug(plan1.toString());
	}

	public String readFromReader(InputStreamReader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String result = "";
		try {
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			result = response.toString();
		} catch (Exception e) {
			System.out.println("error occured at readFromReader");
			System.out.println(e);
		} finally {
			br.close();
		}
		return result;
	}

	public void saveTimePlanToDayPlan(DayPlan dayPlan, TimePlan timePlan) {
		placeRepository.save(timePlan.place);
		timePlan.parentPlan = dayPlan;
		timePlanRepository.save(timePlan);
		//date validation needed
		dayPlan.timePlanSet.add(timePlan);
		dayPlanRepository.save(dayPlan);
	}

	public Place nGeoDTOToPlace(NGeocodeDTO geoDTO) {
		Place place = new Place();
		place.latitude = Double.parseDouble(geoDTO.getAddresses()[0].x);
		place.longitude = Double.parseDouble(geoDTO.getAddresses()[0].y);
		placeRepository.save(place);
		return place;
	}
}
