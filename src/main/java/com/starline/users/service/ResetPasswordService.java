package com.starline.users.service;

import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.ResetPasswordRequest;

public interface ResetPasswordService {

    ApiResponse<String> resetPassword(ResetPasswordRequest payload);
}
