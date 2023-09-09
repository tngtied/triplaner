package com.tngtied.triplaner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.tngtied.triplaner.entity.SiteUser;
import com.tngtied.triplaner.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.JsonParser;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    private static final String base_mapping = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    SiteUser makeValidUser(){
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("test");
        siteUser.setPassword("password");
        siteUser.setEmail("Email@gmail.com");
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
        //JsonParser jsonParser = new JsonParser();
        //JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        signupMvc(jsonString);
    }

    @Test
    @DisplayName("Test: post signup invalid field [password field]")
    void signupTestInvalidPassword() throws Exception {
        String jsonString = "{\"username\": \"username\", \"password\": \"pass\", \"email\":\"email@gmail.com\"}";
        //JsonParser jsonParser = new JsonParser();
        //JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        signupMvc(jsonString);
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
        String jsonString = "{\"username\": \"username11\", \"password\": \"password1\", \"email\":\"email@gmail.com\"}";
        signupMvc(jsonString);
    }

    @Test
    void login() {
    }
}