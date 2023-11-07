package com.hetongxue.cloud.config;

import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hy
 * @version 1.0
 */
@Configuration
public class OpenFeignConfig {
    @Bean
    Logger.Level level() {
        return Logger.Level.FULL;
    }
}
