package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.entity.Book;

public interface IBookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.category.catName = :catName")
    List<Book> findBooksByCategoryName(@Param("catName") String catName);

    @Query("SELECT b FROM Book b WHERE b.author.authName = :authName")
    List<Book> findBooksByAuthorName(@Param("authName") String authName);

    List<Book> findByTitle(String title);
}
