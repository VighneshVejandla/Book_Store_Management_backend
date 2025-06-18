package com.cts;

import org.modelmapper.ModelMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; 


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class BookApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}
	
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}