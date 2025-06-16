package com.cts.orderservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.orderservice.dto.BookDto;

@FeignClient(url="http://localhost:9001", value="BOOK")
public interface BookFeignClient {

	@GetMapping("/bookmanage/viewbookbyid/{Id}")
	public BookDto getBooks(@PathVariable Long Id); 
	
	@PutMapping("/bookmanage/updatebook/{Id}")
	public BookDto updatebook(@PathVariable Long Id, @RequestBody BookDto bookDTO);
}
