package com.bluecoat.proxy.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "externalService", url = "${proxy.url}")
public interface ExternalServiceFeignClient {


    @GetMapping("/test")
    <R> ResponseEntity<R> callExternalService();

    @PostMapping("/test2")
    <R> ResponseEntity<R> callExternalServiceWithBody(@RequestBody Object requestBody);
}
