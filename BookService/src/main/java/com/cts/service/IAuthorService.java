package com.cts.service;

import java.util.List;

import com.cts.dto.AuthorDto;
import com.cts.dto.BookDto;

public interface IAuthorService {
	
	AuthorDto addAuthor(AuthorDto authorDto);
	List<AuthorDto> viewAllAuthors();
	AuthorDto getAuthorById(Long authId);
	
	AuthorDto updateAuthorById(Long authId, AuthorDto authorDto);
	void deleteAuthorById(Long authId);
	List<BookDto> getBooksByAuthId(Long authId);
}
