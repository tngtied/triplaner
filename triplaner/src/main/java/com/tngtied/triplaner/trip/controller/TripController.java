package com.tngtied.triplaner.trip.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tngtied.triplaner.member.entity.Member;
import com.tngtied.triplaner.member.service.UserDetailsServiceImpl;
import com.tngtied.triplaner.trip.dto.InitiateTripRequestDTO;
import com.tngtied.triplaner.trip.dto.NGeocodeDTO;
import com.tngtied.triplaner.trip.dto.RouteRequestDTO;
import com.tngtied.triplaner.trip.dto.SetTimeDTO;
import com.tngtied.triplaner.trip.dto.TripThumbnailDTO;
import com.tngtied.triplaner.trip.entity.DayPlan;
import com.tngtied.triplaner.trip.entity.Place;
import com.tngtied.triplaner.trip.entity.Plan;
import com.tngtied.triplaner.trip.entity.TimePlan;
import com.tngtied.triplaner.trip.repository.DayPlanRepository;
import com.tngtied.triplaner.trip.repository.PlanRepository;
import com.tngtied.triplaner.trip.repository.TimePlanRepository;
import com.tngtied.triplaner.trip.service.TripService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.path}" + "/trip")
public class TripController {

	//private static final String base_mapping = "/api/v1/trip";

	@Value("${NAVER-KEY}")
	private String naverKey;
	@Value("${NAVER-CLIENT}")
	private String naverClientId;

	@Value("${TMAP-KEY}")
	private String tmapKey;

	private final TripService tripService;
	private final UserDetailsServiceImpl userDetailsService;
	private final RestTemplate restTemplate;

	private final PlanRepository planRepository;
	private final DayPlanRepository dayPlanRepository;

	@Autowired
	public TimePlanRepository timePlanRepository;

	@GetMapping("/home")
	public void getHome() {

	}

	@GetMapping("/list")
	public List<TripThumbnailDTO> getTripList(@RequestHeader("Authorization") String authorization) {
		Member user = userDetailsService.getUserFromAuthorization(authorization);
		return planRepository.findThumbnails(user.getUsername());
	}

	@PostMapping()
	public Plan postTrip(@RequestHeader("Authorization") String authorization,
		@RequestBody InitiateTripRequestDTO initiateTripRequestDTO) {
		return tripService.initiateTrip(authorization, initiateTripRequestDTO);
	}

	@GetMapping("/{planId}")
	public Plan getPlan(@RequestHeader("Authorization") String authorization, @PathVariable int planId) {
		return tripService.loadValidatePlan(authorization, planId);
	}

	@PutMapping("/{planId}/{date}")
	public DayPlan putTimePlan(@RequestHeader("Authorization") String authorization, @PathVariable int planId,
		@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
		@RequestBody TimePlan timePlan) {
		Plan plan = tripService.loadValidatePlan(authorization, planId);
		LocalDate pathDate = LocalDate.parse(date);
		DayPlan dayPlan = dayPlanRepository.findByParentPlanAndPlanDate(plan, pathDate);
		tripService.saveTimePlanToDayPlan(dayPlan, timePlan);
		return dayPlan;
	}

	@PostMapping("/{timePlanId}/time")
	public void setTimePlan(@PathVariable int timePlanId, @RequestBody SetTimeDTO setTimeDTO) {
		TimePlan timePlan = timePlanRepository.findById(timePlanId).get();
		timePlan.setTime(setTimeDTO.time);
	}

	@GetMapping("/route")
	public String getRoute(@RequestBody HashMap<String, Object> hashMap) {
		URI uri = UriComponentsBuilder
			.fromUriString("https://apis.openapi.sk.com")
			.path("/transit/routes")
			.queryParam("appKey", tmapKey)
			.encode()
			.build()
			.toUri();

		RouteRequestDTO routeRequestDTO = new RouteRequestDTO(hashMap.get("startX").toString(),
			hashMap.get("startY").toString(), hashMap.get("endX").toString(), hashMap.get("endY").toString(), 5, 0,
			"json",
			hashMap.get("date").toString(), hashMap.get("time").toString());
		RequestEntity<RouteRequestDTO> requestEntity = RequestEntity.post(uri).body(routeRequestDTO);

		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		return responseEntity.getBody();
	}

	@PostMapping("/geocode")
	public Place addressToCoord(@RequestBody HashMap<String, Object> map) throws IOException, URISyntaxException {
		URI uri = UriComponentsBuilder
			.fromUriString("https://naveropenapi.apigw.ntruss.com")
			.path("/map-geocode/v2/geocode")
			.queryParam("query", map.get("address").toString())
			.encode()
			.build()
			.toUri();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
		httpHeaders.set("X-NCP-APIGW-API-KEY", naverKey);

		HttpEntity httpEntity = new HttpEntity(httpHeaders);
		ResponseEntity<NGeocodeDTO> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
			NGeocodeDTO.class);
		return tripService.nGeoDTOToPlace(responseEntity.getBody());

	}

}


