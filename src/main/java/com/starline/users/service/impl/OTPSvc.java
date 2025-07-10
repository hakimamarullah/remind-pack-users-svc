package com.starline.users.service.impl;

import com.starline.users.exceptions.DataNotFoundException;
import com.starline.users.exceptions.TooManyOTPRequest;
import com.starline.users.feign.WhatsAppProxySvc;
import com.starline.users.models.OTP;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPSvc implements OTPService {

    private final RegOTPRepository otpRepository;

    private final WhatsAppProxySvc waProxySvc;

    private final MessageSource messageSource;

    @Value("${otp.max.time.millis:60000}")
    private Long otpMaxTimeMillis;


    @Async
    @Transactional
    @Modifying
    @Override
    public void sendOTPAsync(String mobilePhone) {
        mobilePhone = CommonUtils.normalizePhoneNumber(mobilePhone);
        Optional<OTP> oldOtp = otpRepository.findByMobilePhone(mobilePhone);

        oldOtp.ifPresent(this::checkRequestInterval);
        oldOtp.ifPresent(it -> {
            otpRepository.deleteById(it.getCode());
            otpRepository.flush();
        });

        OTP newOTP = new OTP();
        newOTP.setCode(RandomStringUtils.secureStrong().nextNumeric(6));
        newOTP.setMobilePhone(mobilePhone);

        otpRepository.save(newOTP);

        long maxOTPTime = TimeUnit.MILLISECONDS.toMinutes(otpMaxTimeMillis);
        String message = messageSource.getMessage("otp.message", new Object[]{newOTP.getCode(), maxOTPTime}, LocaleContextHolder.getLocale());
        waProxySvc.sendMessage(mobilePhone, message);
        log.info("OTP sent to {}", mobilePhone);
    }

    @Transactional
    @Modifying
    @Override
    public boolean validateAndClearOTP(String mobilePhone, String otp) {
        try {
            OTP otpData = otpRepository.findByMobilePhone(CommonUtils.normalizePhoneNumber(mobilePhone))
                    .orElseThrow(() -> new DataNotFoundException("OTP not found"));

            if (Objects.equals(otpData.getCode(), otp)) {
                long durationCreated = Duration.between(otpData.getCreatedDate(), LocalDateTime.now()).toMillis();
                otpRepository.deleteById(otpData.getCode());

                return durationCreated < otpMaxTimeMillis;
            }
        } catch (Exception e) {
            log.warn("Failed to validate OTP -> {}", e.getMessage());
        }
        return false;
    }

    private void checkRequestInterval(OTP oldOTP) {
        long durationCreated = Duration.between(oldOTP.getCreatedDate(), LocalDateTime.now()).toMillis();
        if (durationCreated < 30_000) {
            log.info("Too many OTP request for {}", oldOTP.getMobilePhone());
            throw new TooManyOTPRequest();
        }
    }
}
