package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.AuthorDto;
import com.cts.dto.BookDto;
import com.cts.service.IAuthorService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/authormanage")
public class AuthorController {
	
	@Autowired
	IAuthorService authorService;
	
	@PostMapping("/addnewauthor")
	public ResponseEntity<AuthorDto> addAuthor(@Valid @RequestBody AuthorDto authorDto){
		return new ResponseEntity<AuthorDto>(authorService.addAuthor(authorDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/viewallauthors") // Changed endpoint name for consistency
    public ResponseEntity<List<AuthorDto>> viewAllAuthors() {
        List<AuthorDto> authors = authorService.viewAllAuthors();
        if (!authors.isEmpty()) {
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }
        // Use NO_CONTENT if the list is empty, or OK with an empty list
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	@GetMapping("/viewallbooksbyauthid/{authId}")
    public ResponseEntity<List<BookDto>> getBooksByAuthId(@PathVariable Long authId) { // No need for @Valid on @PathVariable
        List<BookDto> books = authorService.getBooksByAuthId(authId);
        if (!books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No content if no books found for the author or author deleted
    }
	
	@GetMapping("/getauthorbyid/{authId}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long authId) { // No need for @Valid on @PathVariable
        AuthorDto authorDto = authorService.getAuthorById(authId);
        if (authorDto != null) {
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }
        // Return 404 Not Found if the author is not found or is marked as deleted
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
	
	@PutMapping("/updateauthor/{authId}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable Long authId, @Valid @RequestBody AuthorDto authorDto) {
        AuthorDto updatedAuthor = authorService.updateAuthorById(authId, authorDto);
        if (updatedAuthor != null) {
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        }
        // Return 404 Not Found if the author to update is not found or is marked as deleted
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
	
	@DeleteMapping("/deleteauthor/{authId}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Long authId) {
        authorService.deleteAuthorById(authId);
        // Use HttpStatus.OK for successful deletion (even soft delete)
        return new ResponseEntity<>("Author has been successfully deleted", HttpStatus.OK);
    } 	
	
}
