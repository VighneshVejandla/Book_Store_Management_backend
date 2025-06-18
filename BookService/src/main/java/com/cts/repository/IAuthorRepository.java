package com.cts.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.entity.Author;
import com.cts.entity.Book;

public interface IAuthorRepository extends JpaRepository<Author, Long>{
	
	Author findByAuthName(String authName);
	List<Book> findByBooksBookId(long id);
	

}
