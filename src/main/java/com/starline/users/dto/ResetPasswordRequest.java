package com.starline.users.dto;

import com.starline.users.annotations.Censor;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Please Provide Valid Phone Number")
    @Length(message = "Phone Number maximum length is 15", max = 15)
    private String phoneNumber;


    @NotBlank(message = "Please Provide Valid OTP")
    private String otp;

    @Censor
    @NotBlank(message = "Please Provide Valid Password")
    @Length(max = 30, message = "Password maximum length is 30")
    private String newPassword;

    @Censor
    @NotBlank(message = "Please Provide Valid Confirm Password")
    private String confirmNewPassword;
}
