package com.tngtied.triplaner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MapAPIKey {
    @Value("${NAVER-KEY}")
    private String apiKey;

    public String getApiKey(){
        return apiKey;
    }
    @Override
    public String toString(){
        return "ApiKey [apiKey="+apiKey+"]";
    }
}
