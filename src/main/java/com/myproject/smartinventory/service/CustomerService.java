package com.myproject.smartinventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.CustomerDTO;
import com.myproject.smartinventory.entity.Customer;
import com.myproject.smartinventory.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(CustomerDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email is required.");
        }
        if (customerRepository.findByEmail(dto.getEmail().trim().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("A customer with this email already exists.");
        }
        Customer customer = new Customer();
        customer.setName(dto.getName().trim());
        customer.setEmail(dto.getEmail().trim().toLowerCase());
        return customerRepository.save(customer);
    }
}

