package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.smartinventory.dto.CustomerDTO;
import com.myproject.smartinventory.service.AlertService;
import com.myproject.smartinventory.service.CustomerService;
import com.myproject.smartinventory.service.InventoryLogService;
import com.myproject.smartinventory.service.ProductService;
import com.myproject.smartinventory.service.RFMService;
import com.myproject.smartinventory.service.SalesService;

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

	@Autowired
	private InventoryLogService inventoryLogService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("totalProducts", productService.getAllActiveProducts().size());
		model.addAttribute("lowStockProducts", productService.getLowStockProducts());
		model.addAttribute("activeAlerts", alertService.getActiveAlerts());
		model.addAttribute("sales", salesService.getAllSales());
		model.addAttribute("recentLogs", inventoryLogService.getRecentLogs());
		return "dashboard/index";
	}

	@GetMapping("/alerts")
	public String alerts(Model model) {
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

	@GetMapping("/inventory-log")
	public String inventoryLog(Model model) {
		model.addAttribute("logs", inventoryLogService.getAllLogs());
		return "inventory/log";
	}

	@GetMapping("/customers/add")
	public String addCustomerForm(Model model) {
		model.addAttribute("customerDTO", new CustomerDTO());
		return "customers/add";
	}

	@PostMapping("/customers/add")
	public String addCustomer(@ModelAttribute CustomerDTO customerDTO,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			customerService.addCustomer(customerDTO);
			redirectAttributes.addFlashAttribute("successMessage", "Customer '" + customerDTO.getName() + "' added successfully.");
			return "redirect:/customers";
		} catch (IllegalArgumentException ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			model.addAttribute("customerDTO", customerDTO);
			return "customers/add";
		}
	}
}
