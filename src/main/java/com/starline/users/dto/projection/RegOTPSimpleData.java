package com.starline.users.dto.projection;

import java.time.LocalDateTime;

public interface RegOTPSimpleData {

    String getCode();

    String getMobilePhone();

    LocalDateTime getCreatedDate();
}
