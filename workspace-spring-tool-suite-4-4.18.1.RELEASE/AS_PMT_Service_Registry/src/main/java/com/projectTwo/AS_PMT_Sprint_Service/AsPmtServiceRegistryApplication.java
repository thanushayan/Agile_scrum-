package com.projectTwo.AS_PMT_Sprint_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AsPmtServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsPmtServiceRegistryApplication.class, args);
	}

}
