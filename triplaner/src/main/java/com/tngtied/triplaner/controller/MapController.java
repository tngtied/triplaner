package com.tngtied.triplaner.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.BadRequestException;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.tngtied.triplaner.JwtTokenProvider;
import com.tngtied.triplaner.dto.InitiateTripRequestDTO;
import com.tngtied.triplaner.dto.NGeocodeDTO;
import com.tngtied.triplaner.dto.NGeocodeWithErrDTO;
import com.tngtied.triplaner.dto.RouteRequestDTO;
import com.tngtied.triplaner.dto.SetTimeDTO;
import com.tngtied.triplaner.dto.TripThumbnailDTO;
import com.tngtied.triplaner.entity.DayPlan;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.entity.Place;
import com.tngtied.triplaner.entity.Plan;
import com.tngtied.triplaner.entity.TimePlan;
import com.tngtied.triplaner.repository.DayPlanRepository;
import com.tngtied.triplaner.repository.PlanRepository;
import com.tngtied.triplaner.repository.TimePlanRepository;
import com.tngtied.triplaner.repository.UserRepository;
import com.tngtied.triplaner.service.TripService;

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
	private final DayPlanRepository dayPlanRepository;

	@Autowired
	public TimePlanRepository timePlanRepository;

	@GetMapping("/home")
	public void getHome() {

	}

	@GetMapping("/list")
	public List<TripThumbnailDTO> getTrips(@RequestHeader("Authorization") String authorization) {
		System.out.println(">> ${base.path}" + "/trips accessed");
		Member user = userRepository.findByUsername(jwtTokenProvider.getUsername(authorization.substring(7))).get();
		//        return plan_repo.findByAuthor_Username(user.getUsername());
		return planRepository.findThumbnails(user.getUsername());
	}

	@PostMapping()
	public Plan initiateTrip(@RequestBody InitiateTripRequestDTO trip_dto) {
		Plan plan = new Plan();
		plan.title = trip_dto.title;
		plan.startDate = trip_dto.startDate;
		plan.endDate = trip_dto.endDate;

		planRepository.save(plan);
		return (plan);
	}

	@GetMapping("/{id}")
	public Plan getPlan(@PathVariable int id) {
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
		LocalDate pathDate = LocalDate.parse(date);
		if (pathDate == null) {
			return null;
		}
		//이거 프론트에 어케 전달할지몰르겟음
		System.out.println("Date search started with date: ");
		System.out.println(pathDate);
		DayPlan dayPlanFound = dayPlanRepository.findByParentPlanPlanIdAndPlanDate(id, pathDate);
		tripService.saveTimePlanToDayPlan(dayPlanFound, newTimePlan);
		return dayPlanFound;
	}

	@PostMapping("/{timePlanId}/time")
	public void setTimePlan(@PathVariable int timePlanId, @RequestBody SetTimeDTO setTimeDTO) {
		TimePlan timePlan = timePlanRepository.findById(timePlanId).get();
		timePlan.setTime(setTimeDTO.time);
	}

	@GetMapping("/route")
	public Object getRoute(@RequestBody HashMap<String, Object> hashMap) throws
		IOException,
		ParseException {
		URL url = new URL("https://apis.openapi.sk.com/transit/routes?appKey=" + URLEncoder.encode(tmapKey, "UTF-8"));
		HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("accept", "application/json");
		httpURLConnection.setRequestProperty("appKey", tmapKey);
		httpURLConnection.setRequestProperty("content-type", "application/json");
		httpURLConnection.setDoOutput(true);

		OutputStream outputStream = httpURLConnection.getOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO(hashMap.get("startX").toString(),
			hashMap.get("startY").toString(), hashMap.get("endX").toString(), hashMap.get("endY").toString(), 5, 0,
			"json",
			hashMap.get("date").toString(), hashMap.get("time").toString());

		ObjectMapper objectMapper = new ObjectMapper();
		String routeRequestString = objectMapper.writeValueAsString(routeRequestDTO);
		System.out.println(">> request json string: " + routeRequestString);
		outputStreamWriter.write(routeRequestString);
		outputStreamWriter.flush();
		outputStreamWriter.close();
		outputStream.close();

		httpURLConnection.connect();

		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "utf-8");
			JSONParser jsonParser = new JSONParser(inputStreamReader);
			return jsonParser.parse();
		} catch (IOException | ParseException exception) {
			inputStreamReader = new InputStreamReader(httpURLConnection.getErrorStream(), "utf-8");
			// String errorMessage = tripService.readFromReader(inputStreamReader);
			JSONParser jsonParser = new JSONParser(inputStreamReader);
			Object jsonObject = jsonParser.parse();
			return jsonObject;

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
			if (responseCode != 400 && responseCode != 500) {
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


