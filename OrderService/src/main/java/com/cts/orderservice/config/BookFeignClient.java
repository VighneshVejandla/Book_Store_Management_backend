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

//	@GetMapping("/bookstore/book/{Id}")
//	public BookDto getBooks(@PathVariable Long Id);
@GetMapping("/bookmanage/viewbookbyid/{bookId}") // <--- Updated to match BookController's path
public BookDto getBooks(@PathVariable("bookId") Long bookId);
	
//	@PutMapping("/bookstore/book/{Id}")
//	public BookDto updatebook(@PathVariable Long Id, @RequestBody BookDto bookDTO);

	@PutMapping("/bookmanage/updatebook/{bookId}") // <--- Updated to match BookController's path
	public BookDto updatebook(@PathVariable("bookId") Long bookId, @RequestBody BookDto bookDTO);
}
