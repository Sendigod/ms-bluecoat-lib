package com.bluecoat.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String url;
    private String clientType;
    private boolean proxyEnabled;
    private boolean sslEnabled;
    private String certPath;
    private String certPassword;

}
