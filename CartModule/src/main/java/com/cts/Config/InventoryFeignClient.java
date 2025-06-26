package com.cts.Config;

import com.cts.dto.BookSummaryDto;
import com.cts.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:9002",value="INVENTORY")
public interface InventoryFeignClient {

    @GetMapping("/inventory/stockByBookId/{bookId}")
    int getStockByBookId(@PathVariable long bookId);
}
