package com.cts.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.config.InventoryFeignClient;
import com.cts.dto.*;
import com.cts.entity.Book;
import com.cts.repository.IAuthorRepository;
import com.cts.repository.IBookRepository;
import com.cts.repository.ICategoryRepository;

@Service
public class BookServiceImpl implements IBookService{
	
	@Autowired
	IBookRepository bookRepository;
	
	@Autowired
	IAuthorRepository authRepo;
	
	@Autowired
	ICategoryRepository catRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	InventoryFeignClient inventoryFeignClient;
	
	@Override
	public BookDto addBook(BookDto bookDto) {
	    Book newBook = modelMapper.map(bookDto, Book.class);
	    newBook.setBookCreatedDate(LocalDateTime.now());
	    newBook.setBookDeleted(false);


		if(!catRepo.existsById(bookDto.getCategory().getCatId())){
			throw new IllegalArgumentException("Cat Doesn't exist");
		}


	    Book savedBook = bookRepository.save(newBook);
	    BookDto mappedDto = modelMapper.map(savedBook, BookDto.class);
	    
	    Map<String, Integer> requestBody = new HashMap<>();
	    requestBody.put("quantity", savedBook.getStockQuantity());

	    inventoryFeignClient.incrementStock(savedBook.getBookId(), requestBody);

	    return mappedDto;
	}


	@Override
	public List<BookDto> viewAllBooks() {
//		return bookRepository.findAll();
		
		List<Book> books = bookRepository.findAll();
		List<BookDto> bookDtos = new ArrayList<>();
		for(Book book : books) {
			if(!book.isBookDeleted()) {
				bookDtos.add(modelMapper.map(book, BookDto.class));
			}
		}
		return bookDtos;
	}

	@Override
	public BookDto getBookById(Long bookId) {
	    Book book = bookRepository.findById(bookId).get();
	    	if(!book.isBookDeleted()) {
	    		return modelMapper.map(book, BookDto.class);
	    	}
	    	return null;
	}

	@Override
	public BookDto updateBookById(Long bookId, BookDto bookDto) {
	    Book updateBook = bookRepository.findById(bookId).get();
	    if(!updateBook.isBookDeleted()) {
	    	updateBook.setTitle(bookDto.getTitle());
	    	updateBook.setPrice(bookDto.getPrice());
	    	updateBook.setStockQuantity(bookDto.getStockQuantity());
	    
	    	Book savedBook = bookRepository.save(updateBook);
	    	return modelMapper.map(savedBook, BookDto.class);
	    }
	    return null;
	}

	@Override
	public void deleteBookById(Long bookId) {
	    Book book = bookRepository.findById(bookId).get();
//	    		orElseThrow(() -> new ResourceNotFoundException("Book not found"));
	    book.setBookDeleted(true);
	    bookRepository.save(book);
	}
	
	
	@Override
    public List<BookDto> findBooksByCategoryName(String catName) {
        List<Book> books = bookRepository.findBooksByCategoryName(catName);
        return books.stream()
                .filter(book -> !book.isBookDeleted())
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findBooksByAuthorName(String authName) {
        List<Book> books = bookRepository.findBooksByAuthorName(authName);
        return books.stream()
                .filter(book -> !book.isBookDeleted())
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .filter(book -> !book.isBookDeleted())
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookDto> getBooksByAuthorId(Long authorId) {
        List<Book> books = bookRepository.findByAuthor_AuthId(authorId);
        return books.stream()
                    .filter(book -> !book.isBookDeleted())
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksByCategoryId(Long categoryId) {
        List<Book> books = bookRepository.findByCategory_CatId(categoryId);
        return books.stream()
                    .filter(book -> !book.isBookDeleted())
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toList());
    }
    
    @Override
    public String purchaseBook(Long bookId, int quantity) {

        Boolean available = inventoryFeignClient.isBookAvailable(bookId);
        if (available == null || !available) {
            throw new RuntimeException("Insufficient stock for book ID: " + bookId);
        }

        // Prepare request body as a Map
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("quantity", quantity);

        // Call the updated Feign client method
        inventoryFeignClient.decrementStock(bookId, requestBody);

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        return "Purchase successful for book: " + book.getTitle();
    }
	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findByIsbn(isbn)
				.filter(b -> !b.isBookDeleted())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));

		return modelMapper.map(book, BookDto.class);
	}



	@Override
	public List<BookDto> findBooksByPriceRange(double min, double max) {
		List<Book> books = bookRepository.findByPriceBetween(min, max);
		return books.stream()
				.filter(book -> !book.isBookDeleted())
				.map(book -> modelMapper.map(book, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookDto> getRandomBooks(Long count) {
		List<Book> books = bookRepository.findAll();
		List<BookDto> bookDtos=  books.stream()
				.map(book -> modelMapper.map(book, BookDto.class))
				.collect(Collectors.toList());
		Collections.shuffle(bookDtos);
		return bookDtos.stream()
				.limit(count)
				.collect(Collectors.toList());
	}
}
