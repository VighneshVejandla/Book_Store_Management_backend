package com.cts.service;

import com.cts.dto.BookDTO;
import com.cts.entity.Book;
import com.cts.repository.IBookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IBookRepository bookRepository;

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book updatedBook = convertDTOToEntityForBook(bookDTO);
        return convertEntityToDtoForBook(bookRepository.save(updatedBook));
    }

    @Override
    public List<BookDTO> viewAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDTO> bookDtos = new ArrayList<>();

        for (Book book : bookList) {
            bookDtos.add(convertEntityToDtoForBook(book));
        }

        return bookDtos;
    }

    @Override
    public BookDTO getBookById(int bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
        return convertEntityToDtoForBook(book);
    }

    @Override
    public BookDTO deleteBookById(int bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        Book bookToDelete = optionalBook.get();
        bookRepository.delete(bookToDelete);

        return convertEntityToDtoForBook(bookToDelete);
    }

    @Override
    public BookDTO updateBookById(int bookId, BookDTO bookDTO) {
        Optional<Book> optionalBooks = bookRepository.findById(bookId);

        if (optionalBooks.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        
        Book updatedBook = optionalBooks.get();
        updatedBook.setTitle(bookDTO.getTitle());
        updatedBook.setPrice(bookDTO.getPrice());
        updatedBook.setStock_quantity(bookDTO.getStock_quantity());

        Book savedBook = bookRepository.save(updatedBook);
        return convertEntityToDtoForBook(savedBook);
    }

    private BookDTO convertEntityToDtoForBook(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertDTOToEntityForBook(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
