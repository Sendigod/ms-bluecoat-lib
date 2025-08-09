package com.bluecoat.proxy.utils;


import com.bluecoat.proxy.config.RestTemplateConfig;
import com.bluecoat.proxy.feign.ExternalServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpClientUtils {

    private final RestTemplateConfig restTemplateConfig;

    private final ExternalServiceFeignClient externalServiceFeignClient;

    public Object getHttpClient(String clientType) {
        if ("resttemplate".equalsIgnoreCase(clientType)) {
            return restTemplateConfig.createRestTemplate();
        } else if ("openfeign".equalsIgnoreCase(clientType)) {
            return externalServiceFeignClient;
        }
        throw new IllegalArgumentException("Invalid client type: " + clientType);
    }

}
