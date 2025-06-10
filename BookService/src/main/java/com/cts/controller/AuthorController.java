package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.AuthorDTO;
import com.cts.dto.BookDTO;
import com.cts.service.IAuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;

    @PostMapping("/addAuthor")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.addAuthor(authorDTO));
        
    }

    @DeleteMapping("/{authorName}")
    public ResponseEntity<AuthorDTO> deleteAuthorByName(@PathVariable String authorName) {
        return ResponseEntity.ok(authorService.deleteAuthorByName(authorName));
    }

    @GetMapping("/{authorName}/books")
    public ResponseEntity<List<BookDTO>> getBooksByAuthorName(@PathVariable String authorName) {
        return ResponseEntity.ok(authorService.getBooksByAuthorName(authorName));
    }
}
