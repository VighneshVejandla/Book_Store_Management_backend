package com.cts.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.userservice.entity.User;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	Optional<User> findByName(String name);

	@Query("SELECT u FROM User u WHERE u.isDeleted = true")
	List<User> findByIsDeletedTrue();

}
