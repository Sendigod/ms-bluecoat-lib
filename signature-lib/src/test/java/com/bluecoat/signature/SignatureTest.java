package com.bluecoat.signature;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import java.util.Arrays;

public class SignatureTest {

    @Test
    public void testSignatureGenerationAndVerification() throws Exception {
        // 测试数据
        String data = "test data";  // 用来生成签名的数据
        String expectedSignature = "BbX0e3tpFDkACLPH7LFn8nn7BtHZU7KiS9S8P6lN9w4=";  // 期望的 Base64 编码签名

        // 初始化 SignatureConfig 和 SignatureGenerator
        SignatureConfig signatureConfig = new SignatureConfig();
        signatureConfig.setAlgorithm("HmacSHA256");
        signatureConfig.setSignatureKey("123456");

        SignatureGenerator signatureGenerator = new SignatureGenerator(signatureConfig);

        // 使用 SignatureGenerator 生成签名
        String actualSignature = signatureGenerator.generateSignature(data);

        // 使用 Base64 解码期望签名
        byte[] expectedSignatureBytes = Base64.getDecoder().decode(expectedSignature);

        // 使用 Base64 解码实际生成的签名
        byte[] actualSignatureBytes = Base64.getDecoder().decode(actualSignature);

        // 验证签名是否匹配
        boolean isSignatureValid = Arrays.equals(expectedSignatureBytes, actualSignatureBytes);

        // 断言验证
        Assertions.assertTrue(isSignatureValid, "Signature does not match.");
    }
}
