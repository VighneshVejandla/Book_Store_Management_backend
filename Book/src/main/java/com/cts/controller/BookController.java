package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.BookDTO;
import com.cts.service.IBookService;

@RestController
@RequestMapping("/bookstore")
public class BookController {
	
	@Autowired
	IBookService bookService;
	
	
	@PostMapping("/book")
	ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
		return new ResponseEntity<BookDTO>(bookService.addBook(bookDTO), HttpStatus.OK);
	}
	
	@GetMapping("/book")
	ResponseEntity<List<BookDTO>> viewAllBooks(){
		return new ResponseEntity<List<BookDTO>>(bookService.viewAllBooks(),HttpStatus.OK);
	}
	
	@GetMapping("/book/{id}")
	ResponseEntity<BookDTO> getBookById(@PathVariable int id){
		return new ResponseEntity<BookDTO>(bookService.getBookById(id),HttpStatus.OK);
	}
	
	@PutMapping("/book/{id}")
	ResponseEntity<BookDTO> updateBookById(@PathVariable int id,@RequestBody BookDTO bookDTO){
		return new ResponseEntity<BookDTO>(bookService.updateBookById(id, bookDTO),HttpStatus.OK);
	}
	
	@DeleteMapping("/book/{id}")
	ResponseEntity<BookDTO> deleteBookById(@PathVariable int id){
		return new ResponseEntity<BookDTO>(bookService.deleteBookById(id),HttpStatus.OK);
	}
}
