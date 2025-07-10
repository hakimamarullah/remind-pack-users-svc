package com.starline.users.repository;

import com.starline.users.dto.projection.RegOTPSimpleData;
import com.starline.users.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegOTPRepository extends JpaRepository<OTP, String> {

    @Query("SELECT ro.code as code, ro.mobilePhone as mobilePhone, ro.createdDate as createdDate FROM OTP ro WHERE ro.code = :code")
    Optional<RegOTPSimpleData> findByCode(String code);

    Optional<OTP> findByMobilePhone(String mobilePhone);

}
