package com.starline.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "OTP")
public class OTP extends BaseEntity {


    @Id
    @Column(name = "CODE", length = 6)
    @Comment(value = "Unique One Time Password for general purposes. Delete after used.", on = "CODE")
    private String code;

    @Column(name = "MOBILE_PHONE", nullable = false, unique = true)
    @Comment(value = "Associated mobile phone number", on = "MOBILE_PHONE")
    private String mobilePhone;
}
