package com.starline.users.exceptions;

public class InvalidMobilePhoneFormatException extends ApiException {

    public InvalidMobilePhoneFormatException() {
        super("Invalid mobile phone format");
        this.httpCode = 400;
    }
}
