package com.cts.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.BookDto;
import com.cts.dto.CategoryDto;
import com.cts.entity.Book;
import com.cts.entity.Category;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.ICategoryRepository;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private ICategoryRepository categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto addCategory(CategoryDto categoryDto) {
		Category category = modelMapper.map(categoryDto, Category.class);

		category.setCatCreatedDate(LocalDateTime.now());
		category.setCatDeleted(false);

		Category savedCategory = categoryRepo.save(category);
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> viewAllCategories() {
		// Filter out categories marked as deleted before mapping to DTOs
		List<Category> categories = categoryRepo.findAll();
		return categories.stream().filter(category -> !category.isCatDeleted())
				.map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
	}

	@Override
	public CategoryDto getCategoryById(Long catId) {
		Category category = categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + catId)); // Use an
																											// actual
																											// exception
		if (!category.isCatDeleted()) {
			return modelMapper.map(category, CategoryDto.class);
		}
		return null; // Return null if category is found but marked as deleted
	}

	@Override
	public List<BookDto> getBooksByCatId(Long catId) {
		Category category = categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + catId)); // Use an
																											// actual
																											// exception
		if (!category.isCatDeleted()) {
			List<BookDto> bookDtos = new ArrayList<>();
			// Assuming Category entity has a getBooks() method
			if (category.getBooks() != null) {
				for (Book book : category.getBooks()) {
					if (!book.isBookDeleted()) { // Ensure only non-deleted books are returned
						bookDtos.add(modelMapper.map(book, BookDto.class));
					}
				}
			}
			return bookDtos;
		}
		return new ArrayList<>(); // Return empty list if category is deleted or not found
	}

	@Override
	public CategoryDto updateCategoryById(Long catId, CategoryDto categoryDto) {
		Category updatedCat = categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + catId)); // Use an
																											// actual
																											// exception
		if (!updatedCat.isCatDeleted()) {
			updatedCat.setCatName(categoryDto.getCatName());

			Category saveCat = categoryRepo.save(updatedCat);
			return modelMapper.map(saveCat, CategoryDto.class);
		}
		return null; // Return null if category is found but marked as deleted
	}

//	@Override
//	public void deleteCategoryById(Long catId) {
//		Category category = categoryRepo.findById(catId)
//				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + catId)); // Use an
//																											// actual
//																											// exception
//		category.setCatDeleted(true); // Soft delete
//		categoryRepo.save(category);
//	}

	@Override
	public void deleteCategoryById(Long catId) {
		Category category = categoryRepo.findById(catId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + catId)); // Use an
		// actual
		if(category.getBooks() != null && !category.getBooks().isEmpty())
		{
			throw new IllegalStateException("Cannot delete category. Books are still associated with this category.");
		}
		category.setCatDeleted(true); // Soft delete
		categoryRepo.save(category);
	}


	@Override
	public CategoryDto getCategoryByCatName(String catName) {
		Category category = categoryRepo.findByCatName(catName);
		if (category != null && !category.isCatDeleted()) {
			return modelMapper.map(category, CategoryDto.class);
		}
		return null; // Return null if category is not found or marked as deleted
	}

}
