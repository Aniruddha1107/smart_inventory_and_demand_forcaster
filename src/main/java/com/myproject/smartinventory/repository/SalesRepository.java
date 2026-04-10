package com.myproject.smartinventory.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long>{
	List<Sales> findByProductAndSaleDateBetween(Product product, LocalDate start, LocalDate end);
	List<Sales> findByProduct(Product product);
	List<Sales> findByCustomer_CustomerId(Long customerId);
}
