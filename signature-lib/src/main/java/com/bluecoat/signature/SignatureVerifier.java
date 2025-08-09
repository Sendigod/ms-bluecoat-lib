package com.bluecoat.signature;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SignatureVerifier {

    private final SignatureGenerator signatureGenerator;

    public boolean verifySignature(String data, String expectedSignature) throws Exception {
        String actualSignature = signatureGenerator.generateSignature(data);
        return actualSignature.equals(expectedSignature);
    }
}
