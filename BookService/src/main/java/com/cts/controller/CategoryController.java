package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.BookDto;
import com.cts.dto.CategoryDto;
import com.cts.service.ICategoryService;

import jakarta.validation.Valid;

@CrossOrigin
@Validated
@RestController
@RequestMapping("/bookmanage")
public class CategoryController {
	
	 @Autowired
	 ICategoryService categoryService;
	 
	 @PostMapping("/addnewcategory")
	    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
	        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.CREATED);
	    }

	    @GetMapping("/viewallcategories")
	    public ResponseEntity<List<CategoryDto>> viewAllCategories() {
	        return new ResponseEntity<>(categoryService.viewAllCategories(), HttpStatus.OK);
	    }
	    
	    @GetMapping("/viewallbooksbycatid/{catId}")
		public ResponseEntity<List<BookDto>> getBooksByCatId(@Valid @PathVariable Long catId){
			return new ResponseEntity<List<BookDto>>(categoryService.getBooksByCatId(catId), HttpStatus.OK);
		}

	    @GetMapping("/getcategorybyid/{catId}")
	    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
	        CategoryDto categoryDto = categoryService.getCategoryById(catId);
	        if (categoryDto != null) {
	            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @PutMapping("/updatecategory/{catId}")
	    public ResponseEntity<CategoryDto> updateCategoryById(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
	        CategoryDto updatedCategory = categoryService.updateCategoryById(catId, categoryDto);
	        if (updatedCategory != null) {
	            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("/deletecategory/{catId}")
	    public ResponseEntity<String> deleteCategoryById(@PathVariable Long catId) {
	        categoryService.deleteCategoryById(catId);
	        return new ResponseEntity<>("Category has been successfully deleted", HttpStatus.OK);
	    }
	    
	    
}
