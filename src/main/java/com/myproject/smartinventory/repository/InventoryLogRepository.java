package com.myproject.smartinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.InventoryLog;
import com.myproject.smartinventory.entity.Product;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
	List<InventoryLog> findByProduct(Product product);

	List<InventoryLog> findAllByOrderByTimestampDesc();

	// Dashboard: fetch only top 5 at DB level — avoids loading all rows
	List<InventoryLog> findTop5ByOrderByTimestampDesc();
}

