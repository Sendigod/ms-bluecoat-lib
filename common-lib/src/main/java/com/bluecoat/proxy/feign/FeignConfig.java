package com.bluecoat.proxy.feign;

import com.bluecoat.proxy.config.ProxyConfig;
import com.bluecoat.proxy.config.SSLContextConfigurer;
import feign.Client;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.aspectj.weaver.loadtime.Options;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final ProxyConfig proxyConfig;

    private final SSLContextConfigurer sslContextConfigurer;

    @Bean
    public Client feignClient() throws Exception {
        SSLContext sslContext = null;

        if (proxyConfig.isSslEnabled()) {
            sslContext = sslContextConfigurer.createSSLContext();
        }
        if (proxyConfig.isProxyEnabled()) {
            // 配置代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getProxyHost(), proxyConfig.getProxyPort()));
            return new Client.Proxied(sslContext.getSocketFactory(), new NoopHostnameVerifier(), proxy, proxyConfig.getProxyUsername(), proxyConfig.getProxyPassword());
        }
        return new Client.Default(sslContext != null ? sslContext.getSocketFactory() : null, new NoopHostnameVerifier());
    }

//    @Bean(name = "customExternalServiceFeignClient")
//    public ExternalServiceFeignClient externalServiceFeignClient(Client feignClient) {
//
//        // 设置重试机制
//        Retryer retryer = new Retryer.Default(1000, TimeUnit.SECONDS.toMillis(1), 3); // 重试3次，间隔1秒
//
//        // 使用 Feign 的 Options 来设置超时
//        Request.Options options = new Request.Options(5000, TimeUnit.MILLISECONDS, 5000, TimeUnit.MILLISECONDS, true); // 5秒超时，允许重定向
//
//        return Feign.builder()
//                .options(options)
//                .client(feignClient)
//                .retryer(retryer)
//                .target(ExternalServiceFeignClient.class, "https://example.com");
//    }
}
