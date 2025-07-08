package com.starline.users.controllers;

import com.starline.users.annotations.LogRequestResponse;
import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.RegisterUserRequest;
import com.starline.users.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@LogRequestResponse
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid RegisterUserRequest payload) {
        return registrationService.registerUser(payload).toResponseEntity();
    }
}
