package com.cts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.AuthorDTO;
import com.cts.dto.BookDTO;
import com.cts.entity.Author;
import com.cts.entity.Book;
import com.cts.repository.IAuthorRepository;
import com.cts.repository.IBookRepository;

@Service
public class AuthorServiceImpl implements IAuthorService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IAuthorRepository authorRepository;

	@Autowired
	private IBookRepository bookRepository; // ✅ Add this

	@Override
	public AuthorDTO addAuthor(AuthorDTO authorDTO) {
		Author author = modelMapper.map(authorDTO, Author.class);

		System.out.println("Mapped Author: " + author.getAuthName());

		Author savedAuthor = authorRepository.save(author);
		return modelMapper.map(savedAuthor, AuthorDTO.class);
	}

	@Override
	public AuthorDTO deleteAuthorByName(String authorName) {
		Author author = authorRepository.findByAuthName(authorName);
		if (author != null) {
			authorRepository.delete(author);
			return modelMapper.map(author, AuthorDTO.class);
		}
		return null;
	}

	@Override
	public List<BookDTO> getBooksByAuthorName(String authName) {
		List<Book> books = bookRepository.findBooksByAuthorName(authName); // ✅ Use bookRepository
		return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
	}
}
