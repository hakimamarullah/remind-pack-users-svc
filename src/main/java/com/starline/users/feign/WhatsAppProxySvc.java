package com.starline.users.feign;

import com.starline.users.feign.config.FeignBasicConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "${srv.feign.names.whatsapp:whatsapp}",
        configuration = {FeignBasicConfig.class}
)
public interface WhatsAppProxySvc {

    @PostMapping(value = "/wa/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void sendMessage(@RequestPart("to") String to, @RequestPart("message") String message);
}
