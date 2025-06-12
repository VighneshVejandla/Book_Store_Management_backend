package com.cts.Config;

import com.cts.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url="http://localhost:9001", value="BOOK")
public interface BookFeignClient {

// Get a book by its ID
@GetMapping("/books/{id}")
BookDto getBookById(@PathVariable("id") Long id);


}