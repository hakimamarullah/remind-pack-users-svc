package com.starline.users.service;

public interface OTPService {

    void sendOTPAsync(String mobilePhone);

    boolean validateAndClearOTP(String mobilePhone, String otp);
}
