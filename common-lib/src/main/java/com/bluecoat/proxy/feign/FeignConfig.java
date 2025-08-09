package com.bluecoat.proxy.feign;

import com.bluecoat.proxy.config.ProxyConfig;
import com.bluecoat.proxy.config.SSLContextConfigurer;
import feign.Client;
import feign.Contract;
import feign.Feign;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final ProxyConfig proxyConfig;

    private final SSLContextConfigurer sslContextConfigurer;

    private final GenericDecoder genericDecoder;

    @Bean
    public Contract feignContract() {
        // 使用 SpringMvcContract 来支持 Spring 的 HTTP 方法注解
        return new SpringMvcContract();
    }

    @Bean
    public Client feignClient() throws Exception {
        SSLContext sslContext = null;

        if (proxyConfig.isSslEnabled()) {
            sslContext = sslContextConfigurer.createSSLContext();
        }
        if (proxyConfig.isProxyEnabled()) {
            // 配置代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort()));
            return new Client.Proxied(sslContext.getSocketFactory(), new NoopHostnameVerifier(), proxy, proxyConfig.getUsername(), proxyConfig.getPassword());
        }
        return new Client.Default(sslContext != null ? sslContext.getSocketFactory() : null, new NoopHostnameVerifier());
    }

    public ExternalServiceFeignClient createFeignClient(String url) throws Exception {
        return Feign.builder()
                .client(feignClient())
                .contract(feignContract())
                .decoder(genericDecoder)
                .target(ExternalServiceFeignClient.class, url);
    }

}
