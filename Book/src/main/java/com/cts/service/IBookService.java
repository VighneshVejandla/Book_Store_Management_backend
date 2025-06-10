package com.cts.service;

import java.util.List;

import com.cts.dto.BookDTO;

public interface IBookService {
	BookDTO addBook(BookDTO bookDTO);
	List<BookDTO> viewAllBooks();
	BookDTO getBookById(int bookId);
	BookDTO deleteBookById(int bookId);
	BookDTO updateBookById(int bookId, BookDTO bookDTO);
}


