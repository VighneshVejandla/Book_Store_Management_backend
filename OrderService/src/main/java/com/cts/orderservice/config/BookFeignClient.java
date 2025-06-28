package com.cts.orderservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.orderservice.dto.BookDto;

@FeignClient(url="http://localhost:8001", value="BOOK")
public interface BookFeignClient {

	@GetMapping("/bookmanage/viewbookbyid/{bookId}") // <--- Updated to match BookController's path
	public BookDto getBooks(@PathVariable("bookId") Long bookId);

	@PutMapping("/bookmanage/updatebook/{bookId}") // <--- Updated to match BookController's path
	public BookDto updatebook(@PathVariable("bookId") Long bookId, @RequestBody BookDto bookDTO);
}
