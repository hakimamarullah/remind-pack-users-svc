package com.starline.users.exceptions;

public class InvalidOTP extends ApiException {

    public InvalidOTP() {
        super("OTP Invalid or mismatched");
        this.httpCode = 403;
    }
}
