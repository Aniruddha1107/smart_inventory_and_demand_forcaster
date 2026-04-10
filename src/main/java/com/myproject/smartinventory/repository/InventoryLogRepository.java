package com.myproject.smartinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.InventoryLog;
import com.myproject.smartinventory.entity.Product;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
	List<InventoryLog> findByProduct(Product product);
}
