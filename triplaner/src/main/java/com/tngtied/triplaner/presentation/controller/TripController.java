package com.tngtied.triplaner.presentation.controller;

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
import org.springframework.format.annotation.DateTimeFormat;
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
import com.tngtied.triplaner.presentation.authentication.jwt.JwtTokenProvider;
import com.tngtied.triplaner.repository.DayPlanRepository;
import com.tngtied.triplaner.repository.PlanRepository;
import com.tngtied.triplaner.repository.TimePlanRepository;
import com.tngtied.triplaner.repository.UserRepository;
import com.tngtied.triplaner.service.TripService;
import com.tngtied.triplaner.service.UserDetailsServiceImpl;

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

	private final JwtTokenProvider jwtTokenProvider;
	private final TripService tripService;
	private final UserDetailsServiceImpl userDetailsService;
	private final UserRepository userRepository;

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
		Member member = userDetailsService.getUserFromAuthorization(authorization);
		return tripService.createPlan(initiateTripRequestDTO.title, initiateTripRequestDTO.startDate,
			initiateTripRequestDTO.endDate, member);
	}

	@GetMapping("/{planId}")
	public Plan getPlan(@RequestHeader("Authorization") String authorization, @PathVariable int planId) {
		Member member = userDetailsService.getUserFromAuthorization(authorization);
		return tripService.loadValidatePlan(member, planId);
	}

	@PutMapping("/{planId}/{date}")
	public DayPlan putTimePlan(@RequestHeader("Authorization") String authorization, @PathVariable int planId,
		@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
		@RequestBody TimePlan timePlan) {

		Member member = userDetailsService.getUserFromAuthorization(authorization);
		Plan plan = tripService.loadValidatePlan(member, planId);
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


