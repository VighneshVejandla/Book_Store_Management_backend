package com.cts.config;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="inventory")
public interface InventoryFeignClient {

}
