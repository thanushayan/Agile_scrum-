package com.projectTwo.AS_PMT_GateWay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@EnableWebFlux
@SpringBootApplication
public class AsPmtGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsPmtGatewayApplication.class, args);
    }

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            if (CorsUtils.isCorsRequest(ctx.getRequest())) {
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                if (ctx.getRequest().getMethod() == HttpMethod.OPTIONS) {
                    ctx.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
                    ctx.getResponse().setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }
}
