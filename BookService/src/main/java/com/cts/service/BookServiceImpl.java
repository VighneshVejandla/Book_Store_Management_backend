package com.cts.service;

import com.cts.dto.BookDTO;
import com.cts.entity.Author;
import com.cts.entity.Book;
import com.cts.entity.Category;
import com.cts.repository.IAuthorRepository;
import com.cts.repository.IBookRepository;
import com.cts.repository.ICategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.exception.ResourceNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IAuthorRepository authorRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        // Fetch Author and Category by ID
        Author author = authorRepository.findById(bookDTO.getAuthId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + bookDTO.getAuthId()));

        Category category = categoryRepository.findById(bookDTO.getCatId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + bookDTO.getCatId()));

        // Convert DTO to Entity
        Book updatedBook = convertDTOToEntityForBook(bookDTO);

        // Set Author and Category
        updatedBook.setAuthor(author);
        updatedBook.setCategory(category);

        // Save and return DTO
        return convertEntityToDtoForBook(bookRepository.save(updatedBook));
    }


    @Override
    public List<BookDTO> viewAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        return convertBookListToDTOList(bookList);
    }

    @Override
    public BookDTO getBookById(int bookId) {
        Book book = bookRepository.findById((long) bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
        return convertEntityToDtoForBook(book);
    }

    @Override
    public BookDTO deleteBookById(int bookId) {
        Optional<Book> optionalBook = bookRepository.findById((long) bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }
        Book bookToDelete = optionalBook.get();
        bookRepository.delete(bookToDelete);
        return convertEntityToDtoForBook(bookToDelete);
    }

    @Override
    public BookDTO updateBookById(int bookId, BookDTO bookDTO) {
        Optional<Book> optionalBooks = bookRepository.findById((long) bookId);
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

    @Override
    public List<BookDTO> getBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return convertBookListToDTOList(books);
    }

    @Override
    public List<BookDTO> getBooksByAuthorName(String authName) {
        List<Book> books = bookRepository.findBooksByAuthorName(authName);
        return convertBookListToDTOList(books);
    }

    @Override
    public List<BookDTO> getBooksByCategoryName(String catName) {
        List<Book> books = bookRepository.findBooksByCategoryName(catName);
        return convertBookListToDTOList(books);
    }

    private List<BookDTO> convertBookListToDTOList(List<Book> books) {
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            bookDTOs.add(convertEntityToDtoForBook(book));
        }
        return bookDTOs;
    }

    private BookDTO convertEntityToDtoForBook(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertDTOToEntityForBook(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
