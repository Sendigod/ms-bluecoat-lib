package com.bluecoat.proxy.controller;

import com.bluecoat.proxy.config.ProxyConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ProxyConfig test() {
        ProxyConfig proxyConfig = new ProxyConfig();
        proxyConfig.setPort(1234);
        return proxyConfig;
    }
}
