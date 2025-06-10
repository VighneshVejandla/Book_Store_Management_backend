package com.cts.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.entity.Inventory;

public interface IInventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByBookId(int bookId);
}
