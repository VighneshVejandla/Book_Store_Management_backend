package com.cts.service;

import java.util.List;
import com.cts.dto.BookDTO;


import com.cts.dto.AuthorDTO;

public interface IAuthorService {
	AuthorDTO addAuthor(AuthorDTO authorDTO);
	AuthorDTO deleteAuthorByName(String authorName);
	List<BookDTO> getBooksByAuthorName(String authorName);
}
