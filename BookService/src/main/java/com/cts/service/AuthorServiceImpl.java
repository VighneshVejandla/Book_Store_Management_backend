package com.cts.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.AuthorDto;
import com.cts.dto.BookDto;
import com.cts.entity.Author;
import com.cts.entity.Book;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.IAuthorRepository;

@Service
public class AuthorServiceImpl implements IAuthorService {

	@Autowired
	IAuthorRepository authorRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public AuthorDto addAuthor(AuthorDto authorDto) {
		Author newAuthor = modelMapper.map(authorDto, Author.class);

		newAuthor.setAuthorCreatedDate(LocalDateTime.now());
		newAuthor.setAuthDeleted(false);

//		newAuthor.getBooks().forEach(b -> b.setAuthor(newAuthor));

		Author savedAuthor = authorRepo.save(newAuthor);
		return modelMapper.map(savedAuthor, AuthorDto.class);
	}

	@Override
    public List<AuthorDto> viewAllAuthors() {
        // Filter out authors marked as deleted before mapping to DTOs
        List<Author> authors = authorRepo.findAll();
        return authors.stream()
                .filter(author -> !author.isAuthDeleted())
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }

//	@Override
//	public AuthorDto getAuthorById(Long authId) {
//			Author author = authorRepo.findById(authId).get();
//			if(!author.isAuthDeleted()) {
//				return modelMapper.map(author, AuthorDto.class);
//			}
//		return null;
//	}

	@Override
	public AuthorDto getAuthorById(Long authId) {
		Author author = authorRepo.findById(authId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authId)); // Use an
																											// actual
																											// exception
		if (!author.isAuthDeleted()) {
			return modelMapper.map(author, AuthorDto.class);
		}
		return null; // Return null if author is found but marked as deleted
	}

	@Override
	public List<BookDto> getBooksByAuthId(Long authId) {
		Author author = authorRepo.findById(authId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authId)); // Use an
																											// actual
																											// exception
		if (!author.isAuthDeleted()) {
			List<BookDto> bookDtos = new ArrayList<>();
			// Assuming Author entity has a getBooks() method
			if (author.getBooks() != null) {
				for (Book book : author.getBooks()) {
					if (!book.isBookDeleted()) { // Ensure only non-deleted books are returned
						bookDtos.add(modelMapper.map(book, BookDto.class));
					}
				}
			}
			return bookDtos;
		}
		return new ArrayList<>(); // Return empty list if author is deleted or not found
	}

	@Override
	public AuthorDto updateAuthorById(Long authId, AuthorDto authorDto) {
		Author updateAuth = authorRepo.findById(authId).get();
		if (!updateAuth.isAuthDeleted()) {
			updateAuth.setAuthName(authorDto.getAuthName());

			Author saveAuth = authorRepo.save(updateAuth);
			return modelMapper.map(saveAuth, AuthorDto.class);
		}
		return null;
	}

//	@Override
//	public void deleteAuthorById(Long authId) {
//		Author author = authorRepo.findById(authId)
//				.orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authId)); // Use an
//																											// actual
//																											// exception
//		author.setAuthDeleted(true); // Soft delete
//		authorRepo.save(author);
//	}

	@Override
	public void deleteAuthorById(Long authId) {
		Author author = authorRepo.findById(authId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authId)); // Use an
																											// actual
		if(author.getBooks() != null && !author.getBooks().isEmpty())
		{
			throw new IllegalStateException("Cannot delete author. Books are still associated with this author.");
		}
		author.setAuthDeleted(true); // Soft delete
		authorRepo.save(author);
	}



}
