package com.cts.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import com.cts.dto.BookDto;
import com.cts.service.IBookService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/bookmanage")
public class BookController {

	@Autowired
	IBookService bookService;

	@PostMapping("/addbook")
	public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto){
		return new ResponseEntity<BookDto>(bookService.addBook(bookDto), HttpStatus.OK);
	}

	@GetMapping("/viewallbooks")
	public ResponseEntity<List<BookDto>> viewAllBooks(){
		return new ResponseEntity<List<BookDto>>(bookService.viewAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/viewbookbyid/{bookId}")
	public ResponseEntity<BookDto> viewBookById(@PathVariable Long bookId)
	{
		return new ResponseEntity<BookDto>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@PutMapping("/updatebook/{bookId}")
	public ResponseEntity<BookDto> updateBookById(@PathVariable Long bookId, @RequestBody BookDto book)
	{
		return new ResponseEntity<BookDto>(bookService.updateBookById(bookId, book), HttpStatus.OK);
	}

	@DeleteMapping("/deletebook/{bookId}")
	public ResponseEntity<String> deleteBookById(@PathVariable Long bookId)
	{
		  bookService.deleteBookById(bookId);

		  return new ResponseEntity<String>("deleted successfully", HttpStatus.OK);
	}

	@GetMapping("/viewbycategory/{catName}")
    public ResponseEntity<List<BookDto>> viewBooksByCategory(@PathVariable String catName) {
        List<BookDto> books = bookService.findBooksByCategoryName(catName);
        if (!books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Or HttpStatus.NOT_FOUND if you prefer
    }

    @GetMapping("/viewbyauthor/{authName}")
    public ResponseEntity<List<BookDto>> viewBooksByAuthor(@PathVariable String authName) {
        List<BookDto> books = bookService.findBooksByAuthorName(authName);
        if (!books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/viewbytitle/{title}")
    public ResponseEntity<List<BookDto>> viewBooksByTitle(@PathVariable String title) {
        List<BookDto> books = bookService.findBooksByTitle(title);
        if (!books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/books/author/{authorId}")
    public ResponseEntity<List<BookDto>> getBooksByAuthorId(@PathVariable Long authorId) {
        List<BookDto> books = bookService.getBooksByAuthorId(authorId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/category/{categoryId}")
    public ResponseEntity<List<BookDto>> getBooksByCategoryId(@PathVariable Long categoryId) {
        List<BookDto> books = bookService.getBooksByCategoryId(categoryId);
        return ResponseEntity.ok(books);

    }

    @GetMapping("/books/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        BookDto bookDto = bookService.findBookByIsbn(isbn);
        return ResponseEntity.ok(bookDto);
    }


    @GetMapping("/price")
    public ResponseEntity<List<BookDto>> getBooksByPriceRange(
            @RequestParam double min,
            @RequestParam double max) {
        return ResponseEntity.ok(bookService.findBooksByPriceRange(min, max));
    }





}
