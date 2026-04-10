package com.myproject.smartinventory.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.Alert;
import com.myproject.smartinventory.entity.AlertStatus;
import com.myproject.smartinventory.entity.Product;

public interface AlertRepository extends JpaRepository<Alert, Integer>{
	List<Alert> findByStatus(AlertStatus status);
	Optional<Alert> findByProductAndStatus(Product product, AlertStatus status);
}
