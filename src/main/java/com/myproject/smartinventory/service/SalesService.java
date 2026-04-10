package com.myproject.smartinventory.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.SalesDTO;
import com.myproject.smartinventory.entity.ChangeType;
import com.myproject.smartinventory.entity.Customer;
import com.myproject.smartinventory.entity.InventoryLog;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.entity.Sales;
import com.myproject.smartinventory.entity.User;
import com.myproject.smartinventory.repository.CustomerRepository;
import com.myproject.smartinventory.repository.InventoryLogRepository;
import com.myproject.smartinventory.repository.ProductRepository;
import com.myproject.smartinventory.repository.SalesRepository;
import jakarta.transaction.Transactional;

@Service
public class SalesService {

	@Autowired
	private SalesRepository salesRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private InventoryLogRepository logRepository;
	
	@Autowired
	private AlertService alertService;
	
	@Transactional
	public Sales recordSale(SalesDTO dto, User currentUser) {
		Product product=productRepository.findById(dto.getProductId())
				.orElseThrow(()-> new RuntimeException("Product not found"));
		if(product.getQuantity() < dto.getQuantitySold()) {
			throw new RuntimeException("Insufficient stock! Avilable: "+product.getQuantity());
		}
		
		product.setQuantity(product.getQuantity() - dto.getQuantitySold());
		productRepository.save(product);
		
		Sales sale =new Sales();
		sale.setProduct(product);
		sale.setSaleDate(LocalDate.now());
		sale.setQuantitySold(dto.getQuantitySold());
		sale.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantitySold())));
		
		if(dto.getCustomerId()!=null) {
			Customer customer=customerRepository.findById(dto.getCustomerId()).orElse(null);
			sale.setCustomer(customer);
		}
		salesRepository.save(sale);
		
		InventoryLog log= new InventoryLog();
		log.setProduct(product);
		log.setChangeType(ChangeType.SALE);
		log.setQtyChange(-dto.getQuantitySold());
		log.setUser(currentUser);
		logRepository.save(log);
		
		alertService.checkAndCreateAlert(product);
		
		return sale;
		
	}
	
	public List<Sales> getSalesByProduct(Product product){
		return salesRepository.findByProduct(product);
	}
	
	public List<Sales> getAllSales(){
		return salesRepository.findAll();
	}
}
