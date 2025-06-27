package com.cts.config;

import com.cts.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                // User Service Routes
//                .route("userservice", r -> r.path("/user/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:8009"))
//
//                .route("profileservice", r -> r.path("/profile/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:8009"))
//
//                // Order Service Routes
//                .route("orderservice", r -> r.path("/bookstore/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:9082"))
//
//                // Review Service Routes
//                .route("reviewservice", r -> r.path("/reviews/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:8007"))
//
//                // Cart Service Routes
//                .route("cartservice", r -> r.path("/api/v1/cart/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:9926"))
//
//                // Book Service Routes (Author Management)
//                .route("bookservice-authors", r -> r.path("/authormanage/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:9001"))
//
//                // Book Service Routes (Book Management)
//                .route("bookservice-books", r -> r.path("/bookmanage/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:9001"))
//
//                // Inventory Service Routes
//                .route("inventoryservice", r -> r.path("/inventory/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:9002"))
//
//                // Payment Service Routes
//                .route("paymentservice", r -> r.path("/payments/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri("http://localhost:8008")) // Adjust port as needed
//
//                .build();
//    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("userservice", r -> r.path("/user/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8006"))

                .route("bookservice", r -> r.path("/bookmanage/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8001"))
                .route("authorservice", r -> r.path("/authormanage/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8001"))

                .route("cartservice", r -> r.path("/api/v1/cart/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8003"))

                .route("orderservice", r -> r.path("/bookstore/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8008"))

                .route("reviewservice", r -> r.path("/reviews/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8007"))

                .route("inventoryservice", r -> r.path("/inventory/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8002"))

                .route("paymentservice", r -> r.path("/payments/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8004"))

                .build();
    }
}