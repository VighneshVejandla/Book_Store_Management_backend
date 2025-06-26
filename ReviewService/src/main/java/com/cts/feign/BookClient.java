package com.cts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.BookDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "BOOK", url = "http://localhost:9001")
public interface BookClient {
    @CircuitBreaker(name = "Book", fallbackMethod = "getFallbackBook")
    @GetMapping("/bookmanage/viewbookbyid/{bookId}")
    ResponseEntity<BookDTO> viewBookById(@PathVariable Long bookId);
    
    default BookDTO getFallbackBook(Long id, Throwable ex) {
    	BookDTO fallbackBook = new BookDTO();
        fallbackBook.setBook_id(id);
        fallbackBook.setTitle("Book Information Unavailable");
        return fallbackBook;
    }
}