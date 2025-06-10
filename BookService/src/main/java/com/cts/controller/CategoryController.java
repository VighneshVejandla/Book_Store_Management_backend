package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.CategoryDTO;
import com.cts.dto.BookDTO;
import com.cts.service.ICategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    @GetMapping
    public ResponseEntity<CategoryDTO> viewCategory() {
        return ResponseEntity.ok(categoryService.viewCategory());
    }

    @DeleteMapping("/{categoryName}")
    public ResponseEntity<CategoryDTO> deleteCategoryByName(@PathVariable String categoryName) {
        return ResponseEntity.ok(categoryService.deleteCategoryByName(categoryName));
    }

    @GetMapping("/{categoryName}/books")
    public ResponseEntity<List<BookDTO>> getBooksByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(categoryService.getBooksByCategoryName(categoryName));
    }
}
