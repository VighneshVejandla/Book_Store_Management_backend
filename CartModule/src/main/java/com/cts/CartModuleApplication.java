package com.cts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableFeignClients(basePackages = "com.cts.config")
public class CartModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartModuleApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
