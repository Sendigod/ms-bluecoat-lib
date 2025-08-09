package com.bluecoat.signature;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SignatureConfig {

    @Value("${signature.algorithm:HMAC-SHA256}")
    private String algorithm;

    @Value("${signature.key:}")
    private String signatureKey;
}
