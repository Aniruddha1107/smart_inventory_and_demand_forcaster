package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.myproject.smartinventory.service.AlertService;
import com.myproject.smartinventory.service.ProductService;
import com.myproject.smartinventory.service.RFMService;
import com.myproject.smartinventory.service.SalesService;
import com.myproject.smartinventory.service.CustomerService;

@Controller
public class DashboardController {
	
	
	@Autowired
	private ProductService productService;
	
	@Autowired 
	private AlertService alertService;
	
	@Autowired
	private SalesService salesService;
	
	@Autowired
	private RFMService rfmService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("totalProducts", productService.getAllActiveProducts().size());
		model.addAttribute("lowStockProducts", productService.getLowStockProducts());
		model.addAttribute("activeAlerts", alertService.getActiveAlerts());
		model.addAttribute("sales", salesService.getAllSales());
		return "dashboard/index";
	}
	
	@GetMapping("/alerts")
	public String alters(Model model) {
		model.addAttribute("alerts", alertService.getActiveAlerts());
		return "alerts/list";
	}
	
	@GetMapping("/customers")
	public String customers(Model model) {
		model.addAttribute("customers", customerService.getAllCustomers());
		return "customers/list";
	}
	
	@GetMapping("/customers/calculate-rfm")
	public String calculateRFM() {
		rfmService.calculateRFMDForAllCustomers();
		return "redirect:/customers";
	}
}
