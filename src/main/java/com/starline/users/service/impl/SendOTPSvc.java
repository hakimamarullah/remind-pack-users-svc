package com.starline.users.service.impl;

import com.starline.users.dto.ApiResponse;
import com.starline.users.exceptions.TooManyOTPRequest;
import com.starline.users.feign.WhatsAppProxySvc;
import com.starline.users.models.RegistrationOTP;
import com.starline.users.repository.RegOTPRepository;
import com.starline.users.service.OTPService;
import com.starline.users.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendOTPSvc implements OTPService {

    private final RegOTPRepository otpRepository;

    private final WhatsAppProxySvc waProxySvc;

    private final MessageSource messageSource;

    @Value("${otp.max.time.millis:60000}")
    private Long otpMaxTimeMillis;


    @Transactional
    @Modifying
    @Override
    public ApiResponse<String> sendOTP(String mobilePhone) {
        mobilePhone = CommonUtils.normalizePhoneNumber(mobilePhone);
        Optional<RegistrationOTP> oldOtp = otpRepository.findByMobilePhone(mobilePhone);

        oldOtp.ifPresent(this::checkRequestInterval);
        oldOtp.ifPresent(it -> {
            otpRepository.deleteById(it.getCode());
            otpRepository.flush();
        });

        RegistrationOTP newOTP = new RegistrationOTP();
        newOTP.setCode(RandomStringUtils.secureStrong().nextNumeric(6));
        newOTP.setMobilePhone(mobilePhone);

        otpRepository.save(newOTP);

        long maxOTPTime = TimeUnit.MILLISECONDS.toMinutes(otpMaxTimeMillis);
        String message = messageSource.getMessage("otp.message", new Object[]{newOTP.getCode(), maxOTPTime}, LocaleContextHolder.getLocale());
        waProxySvc.sendMessage(mobilePhone, message);
        return ApiResponse.setResponse("Success Send OTP", 200);
    }

    private void checkRequestInterval(RegistrationOTP oldOTP) {
        long durationCreated = Duration.between(oldOTP.getCreatedDate(), LocalDateTime.now()).toMillis();
        if (durationCreated < 30_000) {
            throw new TooManyOTPRequest();
        }
    }
}
