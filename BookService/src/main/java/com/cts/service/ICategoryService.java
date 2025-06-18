package com.cts.service;
import java.util.List;

import com.cts.dto.BookDto;
import com.cts.dto.CategoryDto;

public interface ICategoryService {
	CategoryDto addCategory(CategoryDto categoryDto);
	List<CategoryDto> viewAllCategories();
	CategoryDto getCategoryById(Long catId);
	CategoryDto updateCategoryById(Long catId, CategoryDto categoryDto);
	void deleteCategoryById(Long catId);
	List<BookDto> getBooksByCatId(Long catId);
	CategoryDto getCategoryByCatName(String catName);
}