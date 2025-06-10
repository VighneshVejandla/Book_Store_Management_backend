package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.BookDTO;
import com.cts.service.IBookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookstore")
public class BookController {

    @Autowired
    private IBookService bookService;

    @PostMapping("/books")
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookDTO bookDTO) {
        BookDTO savedBook = bookService.addBook(bookDTO);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }


    @GetMapping("/book")
    public ResponseEntity<List<BookDTO>> viewAllBooks() {
        return new ResponseEntity<>(bookService.viewAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable int id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.updateBookById(id, bookDTO), HttpStatus.OK);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<BookDTO> deleteBookById(@PathVariable int id) {
        return new ResponseEntity<>(bookService.deleteBookById(id), HttpStatus.OK);
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthorName(@PathVariable String authorName) {
        return new ResponseEntity<>(bookService.getBooksByAuthorName(authorName), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<BookDTO>> getBookByCategoryName(@PathVariable String categoryName) {
        return new ResponseEntity<>(bookService.getBooksByCategoryName(categoryName), HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookDTO>> getBookByTitle(@PathVariable String title) {
        return new ResponseEntity<>(bookService.getBooksByTitle(title), HttpStatus.OK);
    }
}
