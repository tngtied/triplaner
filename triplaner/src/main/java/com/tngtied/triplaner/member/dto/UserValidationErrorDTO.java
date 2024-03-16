package com.tngtied.triplaner.member.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserValidationErrorDTO {
    public boolean hasErr;

    public ArrayList<UserValidationFieldError> fieldErrorList;
    public ArrayList<String> objectErrorList;

}
