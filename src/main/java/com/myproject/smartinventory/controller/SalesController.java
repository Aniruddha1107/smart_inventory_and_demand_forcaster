package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myproject.smartinventory.dto.SalesDTO;
import com.myproject.smartinventory.security.CustomUserDetails;
import com.myproject.smartinventory.service.CustomerService;
import com.myproject.smartinventory.service.ProductService;
import com.myproject.smartinventory.service.SalesService;

@Controller
@RequestMapping("/sales")
public class SalesController {
	
	@Autowired
	private SalesService salesService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public String listSales(Model model) {
		model.addAttribute("sales", salesService.getAllSales());
		return "sales/list";
	}
	
	@GetMapping("/record")
	public String recordForm(Model model) {
		model.addAttribute("salesDTO", new SalesDTO());
		model.addAttribute("products", productService.getAllActiveProducts());
		return "sales/record";
	}
	
	@PostMapping("/record")
	public String recordSale(@ModelAttribute SalesDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
		salesService.recordSale(dto, userDetails.getUser());
		return "redirect:/sales";
	}
}
