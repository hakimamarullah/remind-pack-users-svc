package com.starline.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;


    @Column(name = "MOBILE_PHONE", length = 15, nullable = false, unique = true)
    @Comment(value = "Mobile phone number should be registered on whatsapp", on = "MOBILE_PHONE")
    private String mobilePhone;

    @Column(name = "ENABLED", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    @Comment(value = "Is user enabled", on = "ENABLED")
    private Boolean enabled = true;


    @Column(name = "LAST_LOGIN", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment(value = "Last login time", on = "LAST_LOGIN")
    private LocalDateTime lastLogin;


    @Column(name = "HASHED_PASSWORD", nullable = false)
    @Comment(value = "Hashed password", on = "HASHED_PASSWORD")
    private String hashedPassword;

}
