package com.tngtied.triplaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TriplanerApplication {

//	public final static String BASE_URL = "https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=";
//	public final MapAPIKey Api_Key_instance;
//
//	public TriplanerApplication(MapAPIKey apiKey){
//		this.Api_Key_instance = apiKey;
//	}
	public static void main(String[] args) {
		SpringApplication.run(TriplanerApplication.class, args);
	}

}
