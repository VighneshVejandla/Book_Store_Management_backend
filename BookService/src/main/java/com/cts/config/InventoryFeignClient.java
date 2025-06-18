package com.cts.config;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Inventory", url = "http://localhost:9002")
public interface InventoryFeignClient {

	@PostMapping("/inventory/increaseStock")
	void increaseStock(@RequestBody Map<String, Object> request);

}
