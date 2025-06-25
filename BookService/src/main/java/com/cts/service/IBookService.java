package com.cts.service;

import java.util.List;

import com.cts.dto.BookDto;

public interface IBookService {

//	Book addBook(Book book);
	List<BookDto> viewAllBooks();

	BookDto getBookById(Long bookId);

	BookDto updateBookById(Long bookId, BookDto bookDto);

	void deleteBookById(Long bookId);

	BookDto addBook(BookDto bookDto);

	List<BookDto> findBooksByCategoryName(String catName);

	List<BookDto> findBooksByAuthorName(String authName);

	List<BookDto> findBooksByTitle(String title);

	List<BookDto> getBooksByAuthorId(Long authorId);

	List<BookDto> getBooksByCategoryId(Long categoryId);

	String purchaseBook(Long bookId, int quantity);

}