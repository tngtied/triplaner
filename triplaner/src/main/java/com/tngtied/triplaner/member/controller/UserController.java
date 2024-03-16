package com.tngtied.triplaner.member.controller;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.catalina.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tngtied.triplaner.member.dto.UserLoginDTO;
import com.tngtied.triplaner.member.dto.UserSignupDTO;
import com.tngtied.triplaner.member.dto.UserValidationErrorDTO;
import com.tngtied.triplaner.member.dto.UserValidationFieldError;
import com.tngtied.triplaner.authentication.jwt.TokenInfo;
import com.tngtied.triplaner.member.exception.UserErrorCode;
import com.tngtied.triplaner.member.exception.UserException;
import com.tngtied.triplaner.member.service.MemberService;
import com.tngtied.triplaner.member.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base.path}" + "/user")
@Slf4j
public class UserController {

	private final UserDetailsServiceImpl userService;
	private final MemberService memberService;

	@GetMapping("/signup")
	public void signup() {
	}

	@PostMapping("/signup")
	public UserValidationErrorDTO signup(@RequestBody @Valid UserSignupDTO siteUser, BindingResult bindingResult) {
		UserValidationErrorDTO userValidationErrorDTO = new UserValidationErrorDTO();
		userValidationErrorDTO.objectErrorList = new ArrayList<String>();
		userValidationErrorDTO.fieldErrorList = new ArrayList<UserValidationFieldError>();

		if (bindingResult.hasErrors()) {
			log.error(">>BindingResult has errors");
			userValidationErrorDTO.setHasErr(true);

			for (ObjectError err : bindingResult.getAllErrors()) {
				if (err instanceof FieldError) {
					FieldError fieldError = (FieldError)err;
					if (fieldError.getField().equals("username")) {
						if (fieldError.getCode().equals("Size")) {
							throw new UserException(UserErrorCode.INCORRECT_USERNAME_SIZE);
						}
					} else if (fieldError.getField().equals("password")) {
						if (fieldError.getCode().equals("Length")) {
							throw new UserException(UserErrorCode.INCORRECT_PASSWORD_SIZE);
						}
					} else if (fieldError.getField().equals("email")) {
						throw new UserException(UserErrorCode.EMAIL_PATTERN_NOT_MATCH);
					}
					userValidationErrorDTO.fieldErrorList.add(
						new UserValidationFieldError(fieldError.getField(), fieldError.getCode()));
				} else {
					userValidationErrorDTO.objectErrorList.add(
						Pattern.compile("(\\w*)$").matcher(err.getClass().toString()).group(1));
				}
			}
		} else {
			try {
				log.debug(">>trying user creation with %s, %s, %s\n", siteUser.getUsername(),
					siteUser.getEmail(), siteUser.getPassword());
				memberService.create(siteUser.getUsername(), siteUser.getEmail(), siteUser.getPassword());
			} catch (Exception e) {
				log.error(">>caught exception");
				userValidationErrorDTO.setHasErr(true);
				log.error(">>error class: %s\n", e.getClass().toString());

				//regex pattern matching

				if (e.getClass().equals(DataIntegrityViolationException.class)) {
					log.error(">>DataIntegrityViolationException");
					log.error(">> error message: " + e.getMessage());
					if (e.getMessage().equals("USERNAME")) {
						throw new UserException(UserErrorCode.DUPLICATE_USERNAME);
					} else if (e.getMessage().equals("EMAIL")) {
						throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
					}
					userValidationErrorDTO.fieldErrorList.add(
						new UserValidationFieldError(e.getMessage(), "DUPLICATE"));
					return userValidationErrorDTO;
				}
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
	public ResponseEntity postLogin(@RequestBody UserLoginDTO userLoginDTO) {
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
