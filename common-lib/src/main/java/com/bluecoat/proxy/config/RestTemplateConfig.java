package com.bluecoat.proxy.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final ProxyConfig proxyConfig;
    private final SSLContextConfigurer sslContextConfigurer;

    @SneakyThrows
    @Bean
    public RestTemplate createRestTemplate() {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        // 配置 SSL，如果启用 SSL
        if (proxyConfig.isProxyEnabled()) {
            SSLContext sslContext = sslContextConfigurer.createSSLContext();
            // 配置 SSL 支持
            connectionManager = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                            .build()
            );
        }

        connectionManager.setMaxTotal(100); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(10); // 每个路由的最大连接数

        // 配置请求超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TimeValue.ofMilliseconds(5000).toTimeout())
                .setResponseTimeout(TimeValue.ofMilliseconds(5000).toTimeout())
                .build();

        // 配置 HttpClient
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager);

        // 配置代理，如果启用代理
        if (proxyConfig.isProxyEnabled()) {
            HttpHost proxy = new HttpHost(proxyConfig.getProxyHost(), proxyConfig.getProxyPort());
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(proxy.getHostName(), proxy.getPort()),
                    new UsernamePasswordCredentials(proxyConfig.getProxyUsername(), proxyConfig.getProxyPassword().toCharArray())
            );
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setProxy(proxy);
        }

        // 返回带有 HttpClient 配置的 RestTemplate
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build()));
    }
}
