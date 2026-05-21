package com.myproject.smartinventory.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.ProductDTO;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.repository.ProductRepository;
import com.myproject.smartinventory.repository.SalesRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private EOQCalculator eoqCalculator;

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

	public List<Product> getAllActiveProducts() {
		return productRepository.findByIsActiveTrue();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
	}

	public Map<String, Object> getEOQResult(Product product) {
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(365);
		var salesHistory = salesRepository.findByProductAndSaleDateBetween(product, startDate, endDate);

		double totalUnits = salesHistory.stream().mapToInt(s -> s.getQuantitySold()).sum();
		double avgDaily = totalUnits / 365.0;
		double variance = salesHistory.stream()
				.mapToDouble(s -> Math.pow(s.getQuantitySold() - avgDaily, 2))
				.average().orElse(0.0);
		double demandStdDev = Math.sqrt(variance);

		double leadTimeDays = 7.0;
		double orderingCost = 500.0;
		double holdingCost = product.getPrice().doubleValue() * 0.20;

		double eoq = eoqCalculator.calculateEOQ(totalUnits, orderingCost, holdingCost);
		double safetyStock = eoqCalculator.calculateSafetyStock(demandStdDev, leadTimeDays);
		double reorderPoint = (avgDaily * leadTimeDays) + safetyStock;

		Map<String, Object> result = new HashMap<>();
		result.put("eoq", (long) Math.round(eoq));
		result.put("safetyStock", (long) Math.round(safetyStock));
		result.put("reorderPoint", (long) Math.round(reorderPoint));
		result.put("annualDemand", (long) Math.round(totalUnits));
		result.put("avgDailyDemand", Math.round(avgDaily * 10.0) / 10.0);
		return result;
	}

	public Product updateProduct(Long id, ProductDTO dto) {
		Product product = getProductById(id);
		product.setName(dto.getName());
		product.setCategory(dto.getCategory());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setSafetyStock(dto.getSafetyStock());
		return productRepository.save(product);
	}

	public void softDeleteProduct(Long id) {
		Product product = getProductById(id);
		product.setIsActive(false);
		productRepository.save(product);
	}

	public List<Product> getByCategory(String category) {
		return productRepository.findByCategory(category);
	}

	public List<Product> getLowStockProducts() {
		return productRepository.findLowStockProducts();
	}
}
