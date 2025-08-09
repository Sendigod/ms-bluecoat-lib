package com.bluecoat.proxy.utils;


import com.bluecoat.proxy.config.RestTemplateConfig;
import com.bluecoat.proxy.feign.FeignConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpClientUtils{

    private final RestTemplateConfig restTemplateConfig;

    private final FeignConfig feignConfig;

    @SneakyThrows
    public Object getHttpClient(String clientType, String url) {
        if ("resttemplate".equalsIgnoreCase(clientType)) {
            return restTemplateConfig.createRestTemplate();
        } else if ("openfeign".equalsIgnoreCase(clientType)) {
            return feignConfig.createFeignClient(url); // 创建一个自定义的 FeignClient 实例
        }
        throw new IllegalArgumentException("Invalid client type: " + clientType);
    }

}
