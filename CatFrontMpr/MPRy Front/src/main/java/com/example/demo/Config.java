package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@ComponentScan
@Configuration
public class Config {
    @Bean
    RestClient getRestClient() {
        return RestClient.builder().build();
    }
}
