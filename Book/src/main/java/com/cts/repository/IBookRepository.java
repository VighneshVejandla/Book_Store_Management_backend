package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Book;
public interface IBookRepository extends JpaRepository<Book, Integer>{

}
