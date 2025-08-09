package com.bluecoat.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.bluecoat")
@EnableFeignClients
public class BluecoatProxySdkApplication {
    public static void main(String[] args) {
        SpringApplication.run(BluecoatProxySdkApplication.class, args);
    }
}
