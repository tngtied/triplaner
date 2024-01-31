package com.tngtied.triplaner.dto;

import lombok.Data;

@Data
public class RouteRequestDTO {
    String startX;
    String startY;
    String endX;
    String endY;
    int count;
    int lang;
    String format;

    public RouteRequestDTO(String startX, String startY, String endX, String endY, int count, int lang, String format){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.count = count;
        this.lang = lang;
        this.format = format;

    }
    
}
