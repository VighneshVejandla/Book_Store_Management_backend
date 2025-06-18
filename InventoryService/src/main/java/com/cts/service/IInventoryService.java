package com.cts.service;

import java.util.List;
import com.cts.dto.InventoryDTO;

public interface IInventoryService {
	long getTotalOverallStock();
    int getStockByBookId(Long bookId);
    int incrementStock(Long bookId, int quantity);
    int decrementStock(Long bookId, int quantity);
    boolean isBookAvailable(Long bookId);
    List<Long> getLowStockBookIds(int threshold);
    List<Long> getOutOfStockBookIds();
	List<Long> getCriticalStockBooksIds(int lowStockThreshold);

}
