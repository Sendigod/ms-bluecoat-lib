package com.bluecoat.signature;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignatureVerifier {

    private final SignatureGenerator signatureGenerator;

    public boolean verifySignature(String data, String expectedSignature) throws Exception {
        String actualSignature = signatureGenerator.generateSignature(data);
        return actualSignature.equals(expectedSignature);
    }
}
