package com.myproject.smartinventory.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByEmail(String email);
}
