package com.starline.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class UserSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSvcApplication.class, args);
	}

}
