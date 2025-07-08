package com.starline.users.service;

import com.starline.users.dto.ApiResponse;
import com.starline.users.dto.UserInfo;

public interface UserService {

    ApiResponse<UserInfo> getUserInfoById(Long id);

    ApiResponse<UserInfo> getUserInfoByMobilePhone(String mobilePhone);
}
