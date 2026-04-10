package com.myproject.smartinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByCategory(String sku);
	List<Product> findByIsActiveTrue();
	List<Product> findByQuantityLessThanEqual(Integer qty);
}
