package com.starline.users.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLogBasicConfig {

    @Bean
    Logger.Level loggerInfo() {
        return Logger.Level.BASIC;
    }
}
