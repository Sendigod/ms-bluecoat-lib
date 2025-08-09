package com.bluecoat.proxy.service;

import com.bluecoat.proxy.config.ProxyConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class ProxyServiceIntegrationTest {

    @Autowired
    private ProxyService proxyService; // 被测试的服务

    @Autowired
    private ProxyConfig proxyConfig;

    @Autowired
    private TestRestTemplate restTemplate; // 用于发起 HTTP 请求

    @Test
    public void testCallExternalService() throws Exception {
        // 模拟的请求数据
        String testData = "test data";
        String expectedSignature = "BbX0e3tpFDkACLPH7LFn8nn7BtHZU7KiS9S8P6lN9w4=";
        String method = "GET";  // 使用 GET 请求进行测试

        Object requestBody = null;  // 如果是 GET 请求，可以不传递请求体

        // 进行实际的业务逻辑调用
        ResponseEntity<ProxyConfig> result = proxyService.callExternalService(testData, expectedSignature, method, requestBody, ProxyConfig.class);

        // 断言返回的结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(result.getBody().getPort(),1234);



    }
}

