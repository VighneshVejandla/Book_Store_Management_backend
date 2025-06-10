package com.cts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.InventoryDTO;
import com.cts.entity.Inventory;
import com.cts.repository.IInventoryRepository;

@Service
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IInventoryRepository inventoryRepository;

    @Override
    public InventoryDTO addInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = convertToEntity(inventoryDTO);
        Inventory saved = inventoryRepository.save(inventory);
        return convertToDTO(saved);
    }

    @Override
    public List<InventoryDTO> viewAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
 List<InventoryDTO> dtoList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            dtoList.add(convertToDTO(inventory));
        }
        return dtoList;
    }

    @Override
    public InventoryDTO viewInventoryById(int inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        return inventory.map(this::convertToDTO).orElse(null);
    }

    @Override
    public InventoryDTO updateInventoryById(int inventoryId, InventoryDTO inventoryDTO) {
        Optional<Inventory> existing = inventoryRepository.findById(inventoryId);
        if (existing.isPresent()) {
            Inventory updated = existing.get();
            updated.setBookId(inventoryDTO.getBookId());
            updated.setQuantity(inventoryDTO.getQuantity());
            return convertToDTO(inventoryRepository.save(updated));
        }
        return null;
    }

    @Override
    public InventoryDTO deleteInventoryById(int inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        inventory.ifPresent(inventoryRepository::delete);
        return inventory.map(this::convertToDTO).orElse(null);
    }

    @Override
    public InventoryDTO deleteAllInventory() {
        inventoryRepository.deleteAll();
        return null;
    }

    @Override
    public int viewStock() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        int total = 0;
        for (Inventory inventory : inventoryList) {
            total += inventory.getQuantity();
        }
        return total;
    }

    @Override
    public int book_stock_by_id(int book_id) {
        Optional<Inventory> optionalInventory = inventoryRepository.findByBookId(book_id);
        return optionalInventory.map(Inventory::getQuantity).orElse(0);
    }

    @Override
    public int book_count() {
        return (int) inventoryRepository.count();
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    private Inventory convertToEntity(InventoryDTO inventoryDTO) {
        return modelMapper.map(inventoryDTO, Inventory.class);
    }
}
