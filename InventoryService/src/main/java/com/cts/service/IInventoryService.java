package com.cts.service;

import java.util.List;
import com.cts.dto.InventoryDTO;

public interface IInventoryService {
    InventoryDTO addInventory(InventoryDTO inventoryDTO);
    List<InventoryDTO> viewAllInventory();
    InventoryDTO viewInventoryById(int inventoryId);
    InventoryDTO updateInventoryById(int inventoryId, InventoryDTO inventoryDTO);
    InventoryDTO deleteInventoryById(int inventoryId);
    InventoryDTO deleteAllInventory();
    int viewStock();
    int book_stock_by_id(int book_id);
    int book_count();
}
