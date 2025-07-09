package com.starline.users.controllers;

import com.starline.users.annotations.LogRequestResponse;
import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.SendOTPRequest;
import com.starline.users.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
@LogRequestResponse
public class OTPController {

    private final OTPService otpService;


    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> sendOTP(@RequestBody @Valid SendOTPRequest payload) {
        otpService.sendOTPAsync(payload.getMobilePhone());
        return ApiResponse.setSuccess("OTP Will Be Sent").toResponseEntity();
    }
}
