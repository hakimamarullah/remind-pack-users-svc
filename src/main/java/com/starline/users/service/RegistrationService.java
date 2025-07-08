package com.starline.users.service;

import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.RegisterUserRequest;

public interface RegistrationService {

    ApiResponse<String> registerUser(RegisterUserRequest payload);
}
