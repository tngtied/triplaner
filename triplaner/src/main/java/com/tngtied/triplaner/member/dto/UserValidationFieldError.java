package com.tngtied.triplaner.member.dto;

import lombok.Data;

@Data
public class UserValidationFieldError {
    String Field;
    String Error;

    public UserValidationFieldError(String field, String string) {
        this.Field = field;
        this.Error = string;
    }
}
