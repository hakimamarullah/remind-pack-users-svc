package com.starline.users.controllers;

import com.starline.users.annotations.LogRequestResponse;
import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.UserInfo;
import com.starline.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@LogRequestResponse
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/info/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserInfo>> getUserInfoById(@PathVariable Long id) {
        return userService.getUserInfoById(id).toResponseEntity();
    }

    @GetMapping(value = "/info/phone/{mobilePhone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserInfo>> getUserInfoByMobilePhone(@PathVariable String mobilePhone) {
        return userService.getUserInfoByMobilePhone(mobilePhone).toResponseEntity();
    }
}
