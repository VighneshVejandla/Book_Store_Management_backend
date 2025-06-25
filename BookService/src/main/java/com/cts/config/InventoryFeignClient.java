package com.cts.config;

import com.cts.dto.InventoryDTO;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Inventory", url="http://localhost:9002/inventory") // Adjust port if needed
public interface InventoryFeignClient {

	

	@PostMapping("/{bookId}/increment")
    public ResponseEntity<?> incrementStock(@PathVariable Long bookId,
                                            @RequestBody Map<String, Integer> requestBody);

	@PostMapping("/{bookId}/decrement")
    public ResponseEntity<?> decrementStock(@PathVariable Long bookId,@RequestBody Map<String, Integer> requestBody);

	@GetMapping("/{bookId}/available")
	Boolean isBookAvailable(@PathVariable Long bookId);


}
