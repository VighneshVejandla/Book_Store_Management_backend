package com.cts.orderservice.config;

import com.cts.orderservice.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(url="http://localhost:9002", value="INVENTORY")
public interface InventoryFeignClient {
    @GetMapping("/inventory/stockByBookId/{bookId}")
    public Integer getStockByBookId(@PathVariable Long bookId);

    @PostMapping("/inventory/{bookId}/decrement")
    public Integer decrementStock(@PathVariable Long bookId,@RequestBody Map<String, Integer> requestBody);
}
