package com.hetongxue.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

}