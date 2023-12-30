package com.projectTwo.AS_PMT_Project_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.projectTwo.AS_PMT_Project_Service.Entities") // Adjust the package name accordingly
public class AsPmtProjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsPmtProjectServiceApplication.class, args);
    }
}
