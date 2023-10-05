package com.tngtied.triplaner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    private static final String base_mapping = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    Member makeValidUser(){
        Member siteUser = new Member("test", "password","USER", "email@gmail.com");
        return siteUser;
    }

    String objectToJson(Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void signupMvc(String jsonContent) throws Exception {
        mockMvc.perform(post(base_mapping+"/signup")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test: post signup valid")
    void signupTestValid() throws Exception{
        String siteUser = objectToJson(makeValidUser()).toString();
        signupMvc(siteUser);
    }

    @Test
    @DisplayName("Test: post signup invalid field [username field]")
    void signupTestInvalidUsername() throws Exception {
        String jsonString = "{\"username\": \"a\", \"password\": \"password\", \"email\":\"email@gmail.com\"}";
        signupMvc(jsonString);

        //response body
        //{"hasErr":true,"fieldErrorList":[{"field":"username","error":"Size"}],"objectErrorList":[]}
    }

    @Test
    @DisplayName("Test: post signup invalid field [password field]")
    void signupTestInvalidPassword() throws Exception {
        String jsonString = "{\"username\": \"username\", \"password\": \"pass\", \"email\":\"email@gmail.com\"}";
        signupMvc(jsonString);

        //response body
        //{"hasErr":true,"fieldErrorList":[{"field":"password","error":"Size"}],"objectErrorList":[]}
    }

    @Test
    @DisplayName("Test: post signup duplicate field [username]")
    void signupTestDuplicateUsername() throws Exception {
        signupMvc(objectToJson(makeValidUser()).toString());
        String jsonString = "{\"username\": \"test\", \"password\": \"password1\", \"email\":\"emailmail@gmail.com\"}";
        signupMvc(jsonString);
    }

    @Test
    @DisplayName("Test: post signup duplicate field [email]")
    void signupTestDuplicateEmail() throws Exception {
        signupMvc(objectToJson(makeValidUser()).toString());
        String jsonString = "{\"username\": \"test1\", \"password\": \"password1\", \"email\":\"email@gmail.com\"}";
        signupMvc(jsonString);
    }

    /*
    @Test
    void loginTestSuccess() throws Exception {
        //진짜어떻겧하는지모르겟다 일단스킵
        MockMvcRequestBuilders requestBuilders =
        mockMvc.perform(p(base_mapping+"/login")
                        .param("username", "test")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andDo(print());
    }
     */

    @Test
    void SessionTestSuccess() throws Exception{

    }

}