package com.bluecoat.proxy.service;

import com.bluecoat.proxy.config.ProxyConfig;
import com.bluecoat.proxy.feign.ExternalServiceFeignClient;
import com.bluecoat.proxy.utils.HttpClientUtils;
import com.bluecoat.signature.SignatureVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProxyService{

    private final HttpClientUtils httpClientUtils;

    private final SignatureVerifier signatureVerifier;

    private final ProxyConfig proxyConfig;

    private final ObjectMapper objectMapper;

    public <T, R> ResponseEntity<R> callExternalService(String data, String expectedSignature, String method, T requestBody, Class<R> responseType) throws Exception {
        // 验证签名
        if (signatureVerifier.verifySignature(data, expectedSignature)) {
            // 动态选择客户端
            Object client = httpClientUtils.getHttpClient(proxyConfig.getClientType(), proxyConfig.getUrl());

            if (client instanceof RestTemplate restTemplate) {
                // 使用 RestTemplate 发起请求

                // 构造 HTTP 请求
                HttpEntity<T> entity = new HttpEntity<>(requestBody);

                if(Arrays.stream(HttpMethod.values()).anyMatch(x->x.matches(method))){
                    return restTemplate.exchange(proxyConfig.getUrl(), HttpMethod.valueOf(method), entity, responseType);
                }else {
                    throw new UnsupportedOperationException(STR."Unsupported HTTP method: \{method}");
                }
            } else if (client instanceof ExternalServiceFeignClient feignClient) {
                // 使用 FeignClient 发起请求
                ResponseEntity<?> responseEntity;
                if ("GET".equalsIgnoreCase(method)) {
                    responseEntity =  feignClient.callExternalService();
                } else if ("POST".equalsIgnoreCase(method)) {
                    responseEntity =  feignClient.callExternalServiceWithBody(requestBody);
                } else {
                    throw new UnsupportedOperationException(STR."Unsupported HTTP method: \{method}");
                }
                return handleResponse(responseEntity, responseType);
            } else {
                throw new IllegalArgumentException(STR."Invalid client type: \{client.getClass().getName()}");
            }
        }
        throw new IllegalArgumentException("Invalid signature");
    }

    @SneakyThrows
    private <R> ResponseEntity<R> handleResponse(ResponseEntity<?> responseEntity, Class<R> responseType){
        // 获取响应体内容
        Object responseBody = responseEntity.getBody();

        if (responseBody instanceof String responseStr) {
            R response = objectMapper.readValue(responseStr, responseType);
            return new ResponseEntity<>(response, responseEntity.getStatusCode());
        }

        R response = objectMapper.readValue(objectMapper.writeValueAsString(responseBody), responseType);
        return new ResponseEntity<>(response, responseEntity.getStatusCode());
    }
}
