package com.tngtied.triplaner.presentation.controller;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tngtied.triplaner.dto.UserLoginDTO;
import com.tngtied.triplaner.dto.UserSignupDTO;
import com.tngtied.triplaner.dto.UserValidationErrorDTO;
import com.tngtied.triplaner.dto.UserValidationFieldError;
import com.tngtied.triplaner.presentation.authentication.TokenInfo;
import com.tngtied.triplaner.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("${base.path}" + "/user")
public class UserController {

	private final UserDetailsServiceImpl userService;

	@GetMapping("/signup")
	public void signup() {
	}

	@ResponseBody
	@PostMapping("/signup")
	public UserValidationErrorDTO signup(@RequestBody @Valid UserSignupDTO siteUser, BindingResult bindingResult) {
		UserValidationErrorDTO userValidationErrorDTO = new UserValidationErrorDTO();
		userValidationErrorDTO.objectErrorList = new ArrayList<String>();
		userValidationErrorDTO.fieldErrorList = new ArrayList<UserValidationFieldError>();

		if (bindingResult.hasErrors()) {
			System.out.println(">>BindingResult has errors");
			userValidationErrorDTO.setHasErr(true);

			for (ObjectError err : bindingResult.getAllErrors()) {
				if (err instanceof FieldError) {
					FieldError fieldError = (FieldError)err;
					userValidationErrorDTO.fieldErrorList.add(
						new UserValidationFieldError(fieldError.getField(), fieldError.getCode().toString()));
				} else {
					userValidationErrorDTO.objectErrorList.add(
						Pattern.compile("(\\w*)$").matcher(err.getClass().toString()).group(1));
				}
			}
		} else {
			System.out.println(">>BindingResult has no error");
			try {

				System.out.printf(">>trying user creation with %s, %s, %s\n", siteUser.getUsername(),
					siteUser.getEmail(), siteUser.getPassword());
				userService.create(siteUser.getUsername(), siteUser.getEmail(), siteUser.getPassword());
			} catch (Exception e) {
				System.out.println(">>caught exception");
				userValidationErrorDTO.setHasErr(true);
				System.out.printf(">>error class: %s\n", e.getClass().toString());

				//regex pattern matching

				if (e.getClass().equals(DataIntegrityViolationException.class)) {
					System.out.println(">>DataIntegrityViolationException");
					//                    Matcher matcher = Pattern.compile("(^.*MEMBER\\()(\\w*)").matcher(e.getCause().toString());
					//                    if (matcher.find()){
					//                        userValidationErrorDTO.fieldErrorList.add(new UserValidationFieldError(matcher.group(2), "DUPLICATE"));
					//                        System.out.printf(">>pattern found: %s\n", matcher.group(2));
					//                        return userValidationErrorDTO;
					//                    }
					userValidationErrorDTO.fieldErrorList.add(
						new UserValidationFieldError(e.getMessage(), "DUPLICATE"));
					return userValidationErrorDTO;
				}
				System.out.println(">>objectErrorList");
				userValidationErrorDTO.objectErrorList.add(
					Pattern.compile("(\\w*)$").matcher(e.getClass().toString()).group(1));
				return userValidationErrorDTO;
			}
			userValidationErrorDTO.setHasErr(false);
		}
		return userValidationErrorDTO;
	}

	@GetMapping("/login")
	public void getLogin() {
		return;
	}

	@PostMapping("/login")
	public ResponseEntity PostLogin(@RequestBody UserLoginDTO userLoginDTO) {
		try {
			TokenInfo tokenInfo = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());

			return ResponseEntity.ok()
				.header(
					HttpHeaders.AUTHORIZATION,
					String.valueOf(tokenInfo)
				)
				.body("");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

}
