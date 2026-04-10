package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myproject.smartinventory.dto.ProductDTO;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.service.ForecastService;
import com.myproject.smartinventory.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ForecastService forecastService;
	
	@GetMapping
	public String listProducts(Model model) {
		model.addAttribute("products", productService.getAllActiveProducts());
		return "products/list";
	}
	
	@GetMapping("/{id}")
	public String productDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		model.addAttribute("forecasts", forecastService.getForecastsForProduct(product));
		return "products/detail";
	}
	
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("product", new Product());
		return "products/form";
	}
	
	
	@PostMapping("/add")
	public String addProduct(@ModelAttribute ProductDTO dto) {
		productService.addProduct(dto);
		return "redirect:/products";
	}
	
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "products/form";
	}
	
	@PostMapping("/edit/{id}")
	public String updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO dto) {
		productService.updateProduct(id, dto);
		return "redirect:/products";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.softDeleteProduct(id);
		return "redirect:/products";
	}
}
