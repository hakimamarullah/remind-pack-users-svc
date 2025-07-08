package com.starline.users.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.UserInfo;
import com.starline.users.exceptions.DataNotFoundException;
import com.starline.users.models.User;
import com.starline.users.repository.UserRepository;
import com.starline.users.service.UserService;
import com.starline.users.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSvc implements UserService {

    private final UserRepository userRepository;

    private final ObjectMapper mapper;

    @Override
    public ApiResponse<UserInfo> getUserInfoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
        return ApiResponse.setSuccess(mapper.convertValue(user, UserInfo.class));
    }

    @Override
    public ApiResponse<UserInfo> getUserInfoByMobilePhone(String mobilePhone) {
        User user = userRepository.findByMobilePhone(CommonUtils.normalizePhoneNumber(mobilePhone))
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return ApiResponse.setSuccess(mapper.convertValue(user, UserInfo.class));
    }
}
