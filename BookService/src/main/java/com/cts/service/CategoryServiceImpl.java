package com.cts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.BookDTO;
import com.cts.dto.CategoryDTO;
import com.cts.entity.Book;
import com.cts.entity.Category;
import com.cts.repository.IBookRepository;
import com.cts.repository.ICategoryRepository;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICategoryRepository categoryRepository;
    
    @Autowired
    private IBookRepository bookRepository;

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = convertDTOToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertEntityToDto(savedCategory);
    }

    @Override
    public CategoryDTO viewCategory() {
        Category category = categoryRepository.findAll().stream().findFirst().orElse(null);
        return category != null ? convertEntityToDto(category) : null;
    }

    @Override
    public CategoryDTO deleteCategoryByName(String catName) {
        Category category = categoryRepository.findByCatName(catName);
        if (category != null) {
            categoryRepository.delete(category);
            return convertEntityToDto(category);
        }
        return null;
    }

    @Override
    public List<BookDTO> getBooksByCategoryName(String catName) {
        List<Book> books = bookRepository.findBooksByCategoryName(catName);
        return books.stream()
                    .map(this::convertEntityToDtoForBook)
                    .collect(Collectors.toList());
    }

    private CategoryDTO convertEntityToDto(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category convertDTOToEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    private BookDTO convertEntityToDtoForBook(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertDTOToEntityForBook(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
