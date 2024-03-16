package com.tngtied.triplaner.trip.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtied.triplaner.trip.dto.NGeocodeDTO;
import com.tngtied.triplaner.trip.entity.DayPlan;
import com.tngtied.triplaner.member.entity.Member;
import com.tngtied.triplaner.trip.entity.Place;
import com.tngtied.triplaner.trip.entity.Plan;
import com.tngtied.triplaner.trip.entity.TimePlan;
import com.tngtied.triplaner.trip.exception.TripErrorCode;
import com.tngtied.triplaner.trip.exception.TripException;
import com.tngtied.triplaner.trip.repository.DayPlanRepository;
import com.tngtied.triplaner.trip.repository.PlaceRepository;
import com.tngtied.triplaner.trip.repository.PlanRepository;
import com.tngtied.triplaner.trip.repository.TimePlanRepository;
import com.tngtied.triplaner.member.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

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
	public Plan make_data(String username) {
		System.out.println(">> make_data invoked");
		Member member = userRepository.findByUsername(username).get();

		Plan plan1 = createPlan("kyungju"
			, LocalDate.of(2023, 8, 13)
			, LocalDate.of(2023, 9, 12)
			, member);

		return plan1;
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
		dayPlan.timeplanList.add(timePlan);
		dayPlanRepository.save(dayPlan);
	}

	public Place nGeoDTOToPlace(NGeocodeDTO geoDTO) {
		Place place = new Place();
		place.latitude = Double.parseDouble(geoDTO.getAddresses()[0].x);
		place.longitude = Double.parseDouble(geoDTO.getAddresses()[0].y);
		placeRepository.save(place);
		return place;
	}

	public Plan createPlan(String title, LocalDate startDate, LocalDate endDate, Member author) {
		Plan plan = Plan.builder()
			.title(title)
			.startDate(startDate)
			.endDate(endDate)
			.author(author)
			.build();
		planRepository.save(plan);
		for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
			DayPlan dayPlan = DayPlan.builder()
				.planDate(currentDate)
				.parentPlan(plan)
				.build();
			dayPlanRepository.save(dayPlan);
			plan.dayPlanList.add(dayPlan);
		}
		planRepository.save(plan);
		return plan;
	}

	public Plan loadValidatePlan(Member member, int planId) {
		Plan plan = planRepository.findById(planId).orElseThrow(() -> new TripException(TripErrorCode.PLAN_NOT_FOUND));
		if (!plan.author.getUserId().equals(member.getUserId())) {
			throw new TripException(TripErrorCode.AUTHOR_NOT_MATCH);
		}
		return plan;
	}

}
