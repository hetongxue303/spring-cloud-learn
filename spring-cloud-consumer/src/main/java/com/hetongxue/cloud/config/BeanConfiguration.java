package com.hetongxue.cloud.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * bean 配置
 *
 * @author hy
 * @version 1.0
 */
@Configuration
public class BeanConfiguration {

    @Bean
    @LoadBalanced// 使用负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    @LoadBalanced// 使用负载均衡
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        builder.setConnectTimeout(Duration.ofSeconds(5));
//        builder.setReadTimeout(Duration.ofSeconds(1));
//        return builder.build();
//    }

}