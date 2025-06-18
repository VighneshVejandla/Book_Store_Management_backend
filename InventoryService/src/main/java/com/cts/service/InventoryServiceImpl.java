package com.cts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.InventoryDTO; // Not used in this specific class, but kept if needed elsewhere
import com.cts.entity.Inventory;
import com.cts.repository.InventoryRepository;


@Service
public class InventoryServiceImpl implements IInventoryService {

	@Autowired
	InventoryRepository inventoryRepository;
	
	
	@Override
	public long getTotalOverallStock() {
		// Retrieves the sum of quantities from the repository and handles null if no records exist.
		Long totalStock = inventoryRepository.sumAllQuantities();
		return totalStock != null ? totalStock : 0L;
	}

	@Override
	public int getStockByBookId(Long bookId) {
		// Fetches the inventory record by book ID.
		Optional<Inventory> inventoryOptional = inventoryRepository.findByBookId(bookId);
		
		// Returns the quantity if found, otherwise returns 0 (book not in inventory or out of stock).
		if(inventoryOptional.isPresent()) {
			return inventoryOptional.get().getQuantity();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public int incrementStock(Long bookId, int quantity) {
		// Validates that the quantity to increment is positive.
		if(quantity <= 0)
		{
			throw new IllegalArgumentException("Quantity to increment must be positive.");
		}
		
		// Attempts to find an existing inventory record for the given bookId.
		Optional<Inventory> inventoryOptional = inventoryRepository.findByBookId(bookId);
		Inventory inventory;

		// If a record exists, update its quantity.
		if(inventoryOptional.isPresent())
		{
			inventory = inventoryOptional.get();
			inventory.setQuantity(inventory.getQuantity() + quantity);
		}
		else // If no record exists, create a new one with the given bookId and quantity.
		{
			inventory = new Inventory(bookId, quantity);
		}
		
		// Saves the (updated or newly created) inventory record to the database.
		Inventory savedInventory = inventoryRepository.save(inventory);
		
		// Returns the updated quantity.
		return savedInventory.getQuantity();
	}
	
	@Override
	public int decrementStock(Long bookId, int quantity) {
	    // Validate that the quantity to decrement is positive.
	    if (quantity <= 0) {
	        throw new IllegalArgumentException("Quantity to decrement must be positive.");
	    }

	    // Attempt to find an existing inventory record for the given bookId.
	    Optional<Inventory> inventoryOptional = inventoryRepository.findByBookId(bookId);

	    if (inventoryOptional.isEmpty()) {
	        throw new IllegalStateException("Inventory record not found for bookId: " + bookId);
	    }

	    Inventory inventory = inventoryOptional.get();

	    // Check if there is enough stock to decrement.
	    if (inventory.getQuantity() < quantity) {
	        throw new IllegalStateException("Insufficient stock for bookId: " + bookId + 
	            ". Available: " + inventory.getQuantity() + ", Requested: " + quantity);
	    }

	    // Decrement the stock.
	    inventory.setQuantity(inventory.getQuantity() - quantity);

	    // Save the updated inventory record.
	    Inventory savedInventory = inventoryRepository.save(inventory);

	    // Return the updated quantity.
	    return savedInventory.getQuantity();
	}


	@Override
	public boolean isBookAvailable(Long bookId) {
		// Checks if a book is available by looking up its inventory and checking if quantity is > 0.
		Optional<Inventory> inventoryOptional = inventoryRepository.findByBookId(bookId);

		return inventoryOptional.isPresent() && inventoryOptional.get().getQuantity() > 0;
	}

	@Override
	public List<Long> getLowStockBookIds(int threshold)
	{
		if(threshold < 0)
		{
			throw new IllegalArgumentException("Low stock threshold cannot be negative.");
		}
		return inventoryRepository.findBookIdsWithQuantityLessThan(threshold);
	}
	
	
	@Override
	public List<Long> getOutOfStockBookIds()
	{
		return inventoryRepository.findBookIdsWithZeroQuantity();
	}
	
	

	@Override
	public List<Long> getCriticalStockBooksIds(int lowStockThreshold)
	{
		if(lowStockThreshold<0)
		{
			throw new IllegalArgumentException("Critical stock threshold cannot be negative.");
		}
		return inventoryRepository.findBookIdsWithQuantityLessThanOrEqualTo(lowStockThreshold);
	}



}