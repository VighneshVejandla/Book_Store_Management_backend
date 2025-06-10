package com.cts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.entity.Category;
import com.cts.entity.Book;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

    Category findByCatName(String catName);

}
