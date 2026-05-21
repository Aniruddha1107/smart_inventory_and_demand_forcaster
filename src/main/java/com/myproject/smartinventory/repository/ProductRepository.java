package com.myproject.smartinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.myproject.smartinventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByCategory(String sku);
	List<Product> findByIsActiveTrue();
	List<Product> findByQuantityLessThanEqual(Integer qty);

	// DB-level low-stock filter: avoids loading all products in memory
	@Query("SELECT p FROM Product p WHERE p.isActive = true AND p.quantity <= p.safetyStock")
	List<Product> findLowStockProducts();
}

