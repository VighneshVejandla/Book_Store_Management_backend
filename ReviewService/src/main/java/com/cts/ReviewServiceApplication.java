package com.cts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient  
@SpringBootApplication
@EnableFeignClients(basePackages = "com")
public class ReviewServiceApplication {
	public static void main(String[] args) {

		SpringApplication.run(ReviewServiceApplication.class, args);
	}
	
	
	@Bean
	ModelMapper createModelMapperBean() {
		return new ModelMapper();
	}
}
