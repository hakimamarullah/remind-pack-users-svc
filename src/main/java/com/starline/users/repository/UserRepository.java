package com.starline.users.repository;

import com.starline.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobilePhone(String mobilePhone);

    boolean existsByMobilePhone(String mobilePhone);

}
