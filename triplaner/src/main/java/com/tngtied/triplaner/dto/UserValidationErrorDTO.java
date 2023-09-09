package com.tngtied.triplaner.dto;

import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserValidationErrorDTO {
    public boolean hasErr;

    public ArrayList<UserValidationFieldError> fieldErrorList;
    public ArrayList<String> objectErrorList;

}
