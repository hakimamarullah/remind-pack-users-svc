package com.starline.users.service;

import com.starline.users.dto.ApiResponse;

public interface OTPService {

    ApiResponse<String> sendOTP(String mobilePhone);
}
