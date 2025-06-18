package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // --- Core Repository Methods ---

    // Spring Data JPA will infer this method to find an Inventory entity by its bookId.
    // It returns Optional<Inventory> to safely handle cases where the bookId doesn't exist.
    Optional<Inventory> findByBookId(Long bookId);

    // Custom query to sum the 'quantity' of all Inventory records.
    // This is a direct database aggregation query.
    @Query("SELECT SUM(i.quantity) FROM Inventory i")
    Long sumAllQuantities();

    // --- Methods for Stock Alerts (using @Query to select only bookId) ---

    // Finds bookIds where quantity is less than the given threshold.
    @Query("SELECT i.bookId FROM Inventory i WHERE i.quantity < :threshold")
    List<Long> findBookIdsWithQuantityLessThan(int threshold); // Return List<Long> to match entity's bookId type

    // Finds bookIds where quantity is exactly zero.
    @Query("SELECT i.bookId FROM Inventory i WHERE i.quantity = 0")
    List<Long> findBookIdsWithZeroQuantity(); // Return List<Long> to match entity's bookId type

    // Finds bookIds where quantity is less than or equal to the given threshold.
    @Query("SELECT i.bookId FROM Inventory i WHERE i.quantity <= :threshold")
    List<Long> findBookIdsWithQuantityLessThanOrEqualTo(int threshold); // Return List<Long> to match entity's bookId type

    // --- REMOVED METHODS ---
    // The following methods were removed because they contain business logic
    // and belong in the service layer (IInventoryService / InventoryServiceImpl):
    // - long getTotalOverallStock();
    // - int getStockByBookId(Long bookId);
    // - int incrementStock(Long bookId, int quantity);
    // - boolean isBookAvailable(Long bookId);
    // - List<String> getLowStockBookIds(int threshold);
    // - List<String> getOutOfStockBookIds();
    // - List<String> getCriticalStockBookIds(int lowStockThreshold);
}