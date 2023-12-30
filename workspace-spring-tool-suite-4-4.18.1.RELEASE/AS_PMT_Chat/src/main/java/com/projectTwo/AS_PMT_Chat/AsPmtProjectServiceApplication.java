package com.projectTwo.AS_PMT_Chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AsPmtProjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsPmtProjectServiceApplication.class, args);
	}

}
