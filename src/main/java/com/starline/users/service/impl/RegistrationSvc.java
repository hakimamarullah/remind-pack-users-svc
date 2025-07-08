package com.starline.users.service.impl;

import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.RegisterUserRequest;
import com.starline.users.dto.projection.RegOTPSimpleData;
import com.starline.users.exceptions.DuplicateDataException;
import com.starline.users.exceptions.InvalidOTP;
import com.starline.users.exceptions.NewPasswordMismatchException;
import com.starline.users.models.User;
import com.starline.users.repository.RegOTPRepository;
import com.starline.users.repository.UserRepository;
import com.starline.users.service.RegistrationService;
import com.starline.users.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegistrationSvc implements RegistrationService {

    private final UserRepository userRepository;

    private final RegOTPRepository otpRepository;

    private final PasswordEncoder passwordEncoder;


    @Value("${otp.max.time.millis:60000}")
    private Long otpMaxTimeMillis;

    @Transactional
    @Modifying
    @Override
    public ApiResponse<String> registerUser(RegisterUserRequest payload) {
        if (!Objects.equals(payload.getPassword(), payload.getConfirmPassword())) {
            throw new NewPasswordMismatchException();
        }

        if (userRepository.existsByMobilePhone(CommonUtils.normalizePhoneNumber(payload.getPhoneNumber()))) {
            throw new DuplicateDataException("User with this phone number already exists");
        }

        RegOTPSimpleData otp = otpRepository.findByCode(payload.getOtp()).orElseThrow(InvalidOTP::new);
        if (Boolean.FALSE.equals(isValidOTP(otp, payload))) {
            throw new InvalidOTP();
        }
        User newUser = new User()
                .setEnabled(true)
                .setHashedPassword(passwordEncoder.encode(payload.getPassword()))
                .setLastLogin(LocalDateTime.now())
                .setMobilePhone(CommonUtils.normalizePhoneNumber(payload.getPhoneNumber()));
        userRepository.save(newUser);
        otpRepository.deleteById(otp.getCode());
        return ApiResponse.setResponse(null, "Success Registration", 201);
    }

    private boolean isValidOTP(RegOTPSimpleData storedOTP, RegisterUserRequest payload) {
        if (!Objects.equals(storedOTP.getCode(), payload.getOtp())) {
            return false;
        }

        long durationCreated = Duration.between(storedOTP.getCreatedDate(), LocalDateTime.now()).toMillis();
        return durationCreated < otpMaxTimeMillis;
    }
}
