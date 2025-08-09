package com.bluecoat.proxy.service;

import com.bluecoat.proxy.config.ProxyConfig;
import com.bluecoat.proxy.config.RestTemplateConfig;
import com.bluecoat.proxy.feign.ExternalServiceFeignClient;
import com.bluecoat.proxy.utils.HttpClientUtils;
import com.bluecoat.signature.SignatureVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProxyService {

    private final HttpClientUtils httpClientUtils;

    private final SignatureVerifier signatureVerifier;

    private final ProxyConfig proxyConfig;

    private final RestTemplateConfig restTemplateConfig;

    public String callExternalService(String data, String expectedSignature) throws Exception {
        // 验证签名
        if (signatureVerifier.verifySignature(data, expectedSignature)) {
            // 动态选择客户端
            Object client = httpClientUtils.getHttpClient(proxyConfig.getClientType());

            // 这里需要根据选择的客户端类型进行不同的调用
            if (client instanceof RestTemplate) {
                // 使用 RestTemplate 发起请求
                RestTemplate restTemplate = (RestTemplate) client;
                return restTemplate.getForObject(proxyConfig.getExternalUrl(), String.class);
            } else {
                ExternalServiceFeignClient feignClient = (ExternalServiceFeignClient) client;
                return feignClient.callExternalService();
            }
        }
        throw new IllegalArgumentException("Invalid signature");
    }
}
