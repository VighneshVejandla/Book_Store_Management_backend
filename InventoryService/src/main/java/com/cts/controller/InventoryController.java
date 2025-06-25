package com.cts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.InventoryDTO;
import com.cts.service.IInventoryService;
import com.cts.service.InventoryServiceImpl;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    
    @GetMapping("/total-stock")
    public ResponseEntity<Long> getTotalOverallStock() {
        long totalStock = inventoryService.getTotalOverallStock();
        return ResponseEntity.ok(totalStock);
    }

    @GetMapping("/stockByBookId/{bookId}")
    public ResponseEntity<Integer> getStockByBookId(@PathVariable Long bookId) {
        int stock = inventoryService.getStockByBookId(bookId);
        return ResponseEntity.ok(stock);
    }

    
    @PostMapping("/{bookId}/increment")
    public ResponseEntity<?> incrementStock(@PathVariable Long bookId,
                                            @RequestBody Map<String, Integer> requestBody) {
        try {
            // Extract quantity from the request body Map
            Integer quantity = requestBody.get("quantity");
            if (quantity == null) {
                return ResponseEntity.badRequest().body("Request body must contain 'quantity'.");
            }

            int updatedStock = inventoryService.incrementStock(bookId, quantity);
            return ResponseEntity.ok(updatedStock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{bookId}/decrement")
    public ResponseEntity<?> decrementStock(@PathVariable Long bookId,@RequestBody Map<String, Integer> requestBody) {
    	try {
    		// Extract quantity from the request body Map
    		Integer quantity = requestBody.get("quantity");
    		if (quantity == null) {
    			return ResponseEntity.badRequest().body("Request body must contain 'quantity'.");
    		}
    		
    		int updatedStock = inventoryService.decrementStock(bookId, quantity);
    		return ResponseEntity.ok(updatedStock);
    	} catch (IllegalArgumentException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }

    
    
    @GetMapping("/{bookId}/available")
    public ResponseEntity<Boolean> isBookAvailable(@PathVariable Long bookId) {
        boolean available = inventoryService.isBookAvailable(bookId);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockBookIds(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer threshold = requestBody.get("threshold");
            if (threshold == null) {
                return ResponseEntity.badRequest().body("Request body must contain 'threshold'.");
            }
            List<Long> lowStockBookIds = inventoryService.getLowStockBookIds(threshold);
            return ResponseEntity.ok(lowStockBookIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Long>> getOutOfStockBookIds() {
        List<Long> outOfStockBookIds = inventoryService.getOutOfStockBookIds();
        return ResponseEntity.ok(outOfStockBookIds);
    }

    @GetMapping("/critical-stock")
    public ResponseEntity<?> getCriticalStockBookIds(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer lowStockThreshold = requestBody.get("lowStockThreshold");
            if (lowStockThreshold == null) {
                return ResponseEntity.badRequest().body("Request body must contain 'lowStockThreshold'.");
            }
            List<Long> criticalStockBookIds = inventoryService.getCriticalStockBooksIds(lowStockThreshold);
            return ResponseEntity.ok(criticalStockBookIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
