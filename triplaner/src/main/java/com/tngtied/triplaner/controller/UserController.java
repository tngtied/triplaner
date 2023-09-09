package com.tngtied.triplaner.controller;


import com.tngtied.triplaner.dto.UserValidationErrorDTO;
import com.tngtied.triplaner.dto.UserValidationFieldError;
import com.tngtied.triplaner.entity.SiteUser;
import com.tngtied.triplaner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

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
                    userValidationErrorDTO.objectErrorList.add(err.getDefaultMessage().toString());
                }
            }
        }
        else {
            System.out.println(">>BindingResult has no error");
            try {
                userService.create(siteUser.getUsername(), siteUser.getEmail(), siteUser.getPassword());
            }catch (Exception e){

                if (e.getClass().equals())
                userValidationErrorDTO.setHasErr(true);
                //e.printStackTrace();
                userValidationErrorDTO.objectErrorList.add(e.getLocalizedMessage());
                return userValidationErrorDTO;
            }
            userValidationErrorDTO.setHasErr(false);
        }

        //System.out.printf("[DTO object] hasErr %b\n", userValidationErrorDTO.HasErr);

        return userValidationErrorDTO;
    }

    @GetMapping("/login")
    public void login(){}

}
