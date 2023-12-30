package com.projectTwo.AS_PMT_GateWay.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template(){
       return new RestTemplate();
    }
    
    
}