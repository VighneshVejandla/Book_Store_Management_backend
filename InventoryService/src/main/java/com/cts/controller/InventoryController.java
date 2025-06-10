package com.cts.controller;

import com.cts.dto.InventoryDTO;
import com.cts.service.IInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @PostMapping("/addInventory")
    public ResponseEntity<InventoryDTO> addInventory(@RequestBody InventoryDTO inventoryDTO){
    @GetMapping("/viewallinventory")
    public ResponseEntity<List<InventoryDTO>> viewAllInventory() {
        return ResponseEntity.ok(inventoryService.viewAllInventory());
    }

    @GetMapping("/inventorybyid/{id}")
    public ResponseEntity<InventoryDTO> viewInventoryById(@PathVariable int id) {
        return ResponseEntity.ok(inventoryService.viewInventoryById(id));
    }

    @PutMapping("/inventorybyid/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable int id, @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateInventoryById(id, inventoryDTO));
    }

    @DeleteMapping("/deleteinventorybyid/{id}")
    public ResponseEntity<InventoryDTO> deleteInventory(@PathVariable int id) {
        return ResponseEntity.ok(inventoryService.deleteInventoryById(id));
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAllInventory() {
        inventoryService.deleteAllInventory();
        return ResponseEntity.ok("All inventory records deleted.");
    }

    @GetMapping("/totalstock")
    public ResponseEntity<Integer> viewTotalStock() {
        return ResponseEntity.ok(inventoryService.viewStock());
    }

    @GetMapping("/viewstockbyid/{bookId}")
    public ResponseEntity<Integer> viewStockByBookId(@PathVariable int bookId) {
        return ResponseEntity.ok(inventoryService.book_stock_by_id(bookId));
    }

    @GetMapping("/bookcount") 
    public ResponseEntity<Integer> getBookCount() {
        return ResponseEntity.ok(inventoryService.book_count());
    }
}
