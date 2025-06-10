package com.cts.userservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.cts.userservice.feignclient")
//@ComponentScan(basePackages = {"com.cts.dto"})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	  @Bean
	  @Primary
	    ModelMapper userServiceModelMapper()
		{
            return new ModelMapper();
		}

}
