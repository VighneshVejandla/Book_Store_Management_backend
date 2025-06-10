package com.cts.service;

import java.util.List;

import com.cts.dto.BookDTO;
import com.cts.dto.CategoryDTO;

public interface ICategoryService {
	CategoryDTO addCategory(CategoryDTO categoryDTO);
	CategoryDTO viewCategory();
	List<BookDTO> getBooksByCategoryName(String catName);
	CategoryDTO deleteCategoryByName(String catName);
}
