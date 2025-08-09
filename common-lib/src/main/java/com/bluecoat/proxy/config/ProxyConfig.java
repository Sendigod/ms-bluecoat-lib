package com.bluecoat.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {

    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String externalUrl;
    private String clientType;
    private boolean proxyEnabled;
    private boolean sslEnabled;
    private String certPath;
    private String certPassword;

}
