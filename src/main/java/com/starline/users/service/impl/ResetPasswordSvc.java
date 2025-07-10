package com.starline.users.service.impl;

import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.ResetPasswordRequest;
import com.starline.users.exceptions.DataNotFoundException;
import com.starline.users.exceptions.InvalidOTP;
import com.starline.users.exceptions.NewPasswordMismatchException;
import com.starline.users.models.User;
import com.starline.users.repository.UserRepository;
import com.starline.users.service.OTPService;
import com.starline.users.service.ResetPasswordService;
import com.starline.users.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResetPasswordSvc implements ResetPasswordService {

    private final OTPService otpService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    @Modifying
    @Override
    public ApiResponse<String> resetPassword(ResetPasswordRequest payload) {
        if (!Objects.equals(payload.getNewPassword(), payload.getConfirmNewPassword())) {
            throw new NewPasswordMismatchException();
        }

        boolean isValidOTP = otpService.validateAndClearOTP(payload.getPhoneNumber(), payload.getOtp());
        if (Boolean.FALSE.equals(isValidOTP)) {
            throw new InvalidOTP();
        }

        User user = userRepository.findByMobilePhone(CommonUtils.normalizePhoneNumber(payload.getPhoneNumber()))
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        user.setHashedPassword(passwordEncoder.encode(payload.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.setResponse(null, "Success Reset Password", 200);
    }
}
