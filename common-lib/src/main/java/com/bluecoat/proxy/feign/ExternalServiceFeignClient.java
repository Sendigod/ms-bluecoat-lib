package com.bluecoat.proxy.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "externalService", url = "${proxy.external.url}")
public interface ExternalServiceFeignClient {

    @GetMapping("/endpoint")
    String callExternalService();
}
