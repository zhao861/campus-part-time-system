package com.rgzn.zcy.jobmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * AI 审核专用 RestClient：显式设置 connect/read 超时（默认工厂不保证读超时）。
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient aiAuditRestClient(AiAuditProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()));
        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
