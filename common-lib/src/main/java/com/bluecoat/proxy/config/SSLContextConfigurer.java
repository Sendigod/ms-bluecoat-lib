package com.bluecoat.proxy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class SSLContextConfigurer {

    private final ProxyConfig proxyConfig;

    // 加载证书并配置 SSLContext
    public SSLContext createSSLContext() throws Exception {
        String certPath = proxyConfig.getCertPath();
        String certPassword = proxyConfig.getCertPassword();

        if (certPath == null || certPassword == null) {
            throw new IllegalArgumentException("Certificate path and password must be provided in ProxyConfig");
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (InputStream certStream = new FileInputStream(certPath)) {
            keyStore.load(certStream, certPassword.toCharArray());
        }

        // 创建密钥管理器和信任管理器
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, certPassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // 创建 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return sslContext;
    }
}
