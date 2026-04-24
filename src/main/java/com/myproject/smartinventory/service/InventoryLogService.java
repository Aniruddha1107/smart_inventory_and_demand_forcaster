package com.myproject.smartinventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.entity.InventoryLog;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.repository.InventoryLogRepository;

@Service
public class InventoryLogService {

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    public List<InventoryLog> getAllLogs() {
        return inventoryLogRepository.findAllByOrderByTimestampDesc();
    }

    public List<InventoryLog> getLogsByProduct(Product product) {
        return inventoryLogRepository.findByProduct(product);
    }
}
