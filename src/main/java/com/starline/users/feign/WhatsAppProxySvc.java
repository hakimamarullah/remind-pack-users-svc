package com.starline.users.feign;

import com.starline.users.feign.config.FeignLogBasicConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "whatsapp-proxy",
        url = "${whatsapp.proxy.url:http://localhost:4000}",
        configuration = {FeignLogBasicConfig.class}
)
public interface WhatsAppProxySvc {

    @PostMapping(value = "/wa/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void sendMessage(@RequestPart("to") String to, @RequestPart("message") String message);
}
