package com.myproject.smartinventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.ProductDTO;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product addProduct(ProductDTO dto) {
		Product product = new Product();
		product.setName(dto.getName());
		product.setCategory(dto.getCategory());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setSafetyStock(dto.getSafetyStock());
		product.setSku(dto.getSku());
		return productRepository.save(product);
	}
	
	public List<Product> getAllActiveProducts(){
		return productRepository.findByIsActiveTrue();
	}
	
	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Prodct not found with ID: "+ id));
	}
	
	public Product updateProduct(Long id, ProductDTO dto) {
		Product product=getProductById(id);
		product.setName(dto.getName());
		product.setCategory(dto.getCategory());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setSafetyStock(dto.getSafetyStock());
		return productRepository.save(product);
	}
	
	public void softDeleteProduct(Long id) {
		Product product=getProductById(id);
		product.setIsActive(false);
		productRepository.save(product);
	}
	
	public List<Product> getByCategory(String category){
		return productRepository.findByCategory(category);
	}
	
	public List<Product> getLowStockProducts(){
		return productRepository.findByIsActiveTrue().stream()
				.filter(p->p.getQuantity()<=p.getSafetyStock())
				.toList();
	}
}
