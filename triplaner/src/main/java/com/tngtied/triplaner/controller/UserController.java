package com.tngtied.triplaner.controller;


import com.tngtied.triplaner.dto.UserValidationErrorDTO;
import com.tngtied.triplaner.dto.UserValidationFieldError;
import com.tngtied.triplaner.entity.SiteUser;
import com.tngtied.triplaner.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public void signup(){}

    @ResponseBody
    @PostMapping("/signup")
    public UserValidationErrorDTO signup(@RequestBody @Valid SiteUser siteUser, BindingResult bindingResult){
        UserValidationErrorDTO userValidationErrorDTO = new UserValidationErrorDTO();
        userValidationErrorDTO.objectErrorList = new ArrayList<String>();
        userValidationErrorDTO.fieldErrorList = new ArrayList<UserValidationFieldError>();

        if (bindingResult.hasErrors()){
            System.out.println(">>BindingResult has errors");
            userValidationErrorDTO.setHasErr(true);

            for (ObjectError err : bindingResult.getAllErrors()) {
                if (err instanceof FieldError) {
                    FieldError fieldError = (FieldError) err;
                    userValidationErrorDTO.fieldErrorList.add(new UserValidationFieldError(fieldError.getField(), fieldError.getCode().toString()));
                }
                else{
                    userValidationErrorDTO.objectErrorList.add(Pattern.compile("(\\w*)$").matcher(err.getClass().toString()).group(1));
                }
            }
        }
        else {
            System.out.println(">>BindingResult has no error");
            try {
                userService.create(siteUser.getUsername(), siteUser.getEmail(), siteUser.getPassword());
            }catch (Exception e){
                System.out.println(">>caught exception");
                userValidationErrorDTO.setHasErr(true);
                if (e.getClass().equals(ConstraintViolationException.class)){
                    userValidationErrorDTO.fieldErrorList.add(new UserValidationFieldError("password", e.getMessage()));
                }else{
                    System.out.printf(">>error class: %s\n", e.getClass().toString());
                    System.out.printf("%s\n", e.getCause().toString());
                    //regex pattern matching

                    if (e.getClass().equals(DataIntegrityViolationException.class)){
                        Matcher matcher = Pattern.compile("(^.*SITE_USER\\()(\\w*)").matcher(e.getCause().toString());
                        if (matcher.find()){
                            userValidationErrorDTO.fieldErrorList.add(new UserValidationFieldError(matcher.group(2), "Integrity"));
                            System.out.printf(">>pattern found: %s\n", matcher.group(2));
                            return userValidationErrorDTO;
                        }
                    }
                    userValidationErrorDTO.objectErrorList.add(Pattern.compile("(\\w*)$").matcher(e.getClass().toString()).group(1));
                }

                return userValidationErrorDTO;
            }
            userValidationErrorDTO.setHasErr(false);
        }

        return userValidationErrorDTO;
    }

    @GetMapping("/login")
    public void login(){}

}
