package com.tngtied.triplaner.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.tngtied.triplaner.JwtTokenProvider;
import com.tngtied.triplaner.dto.*;
import com.tngtied.triplaner.entity.*;
import com.tngtied.triplaner.repository.DayPlanRepository;
import com.tngtied.triplaner.repository.PlanRepository;
import com.tngtied.triplaner.repository.TimePlanRepository;
import com.tngtied.triplaner.repository.UserRepository;
import com.tngtied.triplaner.service.TripService;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.path}" + "/trip")
public class MapController {

	//private static final String base_mapping = "/api/v1/trip";

	@Value("${NAVER-KEY}")
	private String naverKey;
	@Value("${NAVER-CLIENT}")
	private String naverClientId;
	@Value("${TMAP-KEY}")
	private String tmapKey;

	private final JwtTokenProvider jwtTokenProvider;
	private final TripService tripService;
	private final UserRepository userRepository;
	private final PlanRepository planRepository;
	private final DayPlanRepository day_repo;
	private final TimePlanRepository timePlanRepository;

	Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/home")
	public void home_get() {

	}

	@GetMapping("/list")
	public List<TripThumbnailDTO> getTripList(@RequestHeader("Authorization") String authorization) {
		System.out.println(">> ${base.path}" + "/trips accessed");
		Member user = userRepository.findByUsername(jwtTokenProvider.getUsername(authorization.substring(7))).get();
		//        return plan_repo.findByAuthor_Username(user.getUsername());
		return planRepository.findThumbnails(user.getUsername());
	}

	@PostMapping()
	public Plan postTrip(@RequestBody InitiateTripRequestDTO trip_dto) {
		Plan plan = new Plan();
		plan.title = trip_dto.title;
		plan.startDate = trip_dto.startDate;
		plan.endDate = trip_dto.endDate;

		planRepository.save(plan);
		return (plan);
	}

	@GetMapping("/{id}")
	public Plan getPlan(@PathVariable int id) {
		// TODO 해당 plan의 주인만이 조회할 수 있도록 해야 한다.
		Plan plan = planRepository.findById(id).orElse(null);
		if (plan == null) {
			System.out.println("found plan was null");
		} else {
			System.out.println(plan);
		}
		return (plan);
	}

	@PutMapping("/{id}/{date}")
	public DayPlan putTimePlan(@PathVariable long id, @PathVariable String date, @RequestBody TimePlan newTimePlan) {
		// TODO 해당 plan의 주인인지 검증하는 로직 필요
		LocalDate pathDate = LocalDate.parse(date);
		System.out.println("Date search started with date: ");
		System.out.println(pathDate);
		DayPlan dayPlanFound = day_repo.findByParentPlanPlanIdAndPlanDate(id, pathDate);
		tripService.saveTimePlanToDayPlan(dayPlanFound, newTimePlan);
		return dayPlanFound;
	}

	@PostMapping("/{id}/time")
	public void setTimePlan(@PathVariable int id, @RequestBody SetTimeDTO setTimeDTO) {
		// TODO 해당 plan의 주인인지 검증하는 로직 필요
		TimePlan timePlan = timePlanRepository.findById(id).get();
		timePlan.setTime(setTimeDTO.time);
	}

	@GetMapping("/route")
	public JsonObject getRoute(@RequestBody Map<String, Object> map) throws
		IOException,
		ParseException {
		URL url = new URL("https://apis.openapi.sk.com/transit/routes");
		HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("accept", "application/json");
		httpURLConnection.setRequestProperty("appKey", tmapKey);
		httpURLConnection.setRequestProperty("content-type", "application/json");
		httpURLConnection.setDoOutput(true);

		OutputStream outputStream = httpURLConnection.getOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO(map.get("startX").toString(),
			map.get("startY").toString(), map.get("endX").toString(), map.get("endY").toString(), 5, 0, "json",
			map.get("date").toString(), map.get("time").toString());
		outputStreamWriter.write(routeRequestDTO.toString());
		outputStreamWriter.flush();
		outputStreamWriter.close();
		outputStream.close();

		httpURLConnection.connect();

		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "utf-8");
			JSONParser jsonParser = new JSONParser(inputStreamReader);
			return (JsonObject)jsonParser.parse();
		} catch (IOException exception) {
			inputStreamReader = new InputStreamReader(httpURLConnection.getErrorStream(), "utf-8");
			String errorMessage = tripService.readFromReader(inputStreamReader);
			logger.debug(errorMessage);
			throw new UnexpectedException(errorMessage);
		}
	}

	@PostMapping("/geocode")
	public Place addressToCoord(@RequestBody HashMap<String, Object> map) throws IOException {
		URL url = new URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(
			map.get("address").toString(), "utf-8"));
		System.out.println("url is: " + url.toString());
		HttpURLConnection geoCon = (HttpURLConnection)url.openConnection();
		geoCon.setRequestProperty("Content-Type", "application/json");
		geoCon.setRequestMethod("GET");
		geoCon.setRequestProperty("X-NCP-APIGW-API-KEY-ID", naverClientId);
		geoCon.setRequestProperty("X-NCP-APIGW-API-KEY", naverKey);

		geoCon.connect();

		var gson = new Gson();
		InputStreamReader inputStreamReader;
		int responseCode = geoCon.getResponseCode();
		if (responseCode != 200) {
			if (responseCode == 400 || responseCode == 500) {
				inputStreamReader = new InputStreamReader(geoCon.getErrorStream(), "utf-8");
				String errorMessage = tripService.readFromReader(inputStreamReader);
				System.out.println(errorMessage);
				if (responseCode == 400) {
					throw new BadRequestException();
				} else {
					throw new UnexpectedException(errorMessage);
				}
			} else {
				//response json from Naver geocode received
				inputStreamReader = new InputStreamReader(geoCon.getInputStream(), "utf-8");
				JsonReader jsonReader = new JsonReader(inputStreamReader);
				jsonReader.setLenient(true);
				NGeocodeWithErrDTO nGeocodeWithErrDTO = gson.fromJson(jsonReader, NGeocodeWithErrDTO.class);
				throw new UnexpectedException(nGeocodeWithErrDTO.getErrorMessage());
			}
		} else {
			inputStreamReader = new InputStreamReader(geoCon.getInputStream(), "utf-8");
			JsonReader jsonReader = new JsonReader(inputStreamReader);
			jsonReader.setLenient(true);
			NGeocodeDTO nGeocodeDTO = gson.fromJson(jsonReader, NGeocodeDTO.class);
			System.out.println(nGeocodeDTO.toString());
			return (tripService.nGeoDTOToPlace(nGeocodeDTO));
		}
	}

}


