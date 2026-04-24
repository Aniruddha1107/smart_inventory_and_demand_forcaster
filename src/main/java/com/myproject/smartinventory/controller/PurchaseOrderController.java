package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.myproject.smartinventory.dto.PurchaseOrderDTO;
import com.myproject.smartinventory.security.CustomUserDetails;
import com.myproject.smartinventory.service.ProductService;
import com.myproject.smartinventory.service.PurchaseOrderService;

@Controller
@RequestMapping("/restock")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listPOs(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.getAllPOs());
        return "restock/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("purchaseOrderDTO", new PurchaseOrderDTO());
        model.addAttribute("products", productService.getAllActiveProducts());
        return "restock/create";
    }

    @PostMapping("/create")
    public String createPO(@ModelAttribute PurchaseOrderDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        purchaseOrderService.createPO(dto, userDetails.getUser());
        return "redirect:/restock";
    }

    @PostMapping("/receive/{id}")
    public String receivePO(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        purchaseOrderService.receivePO(id, userDetails.getUser());
        return "redirect:/restock";
    }

    @PostMapping("/cancel/{id}")
    public String cancelPO(@PathVariable Long id) {
        purchaseOrderService.cancelPO(id);
        return "redirect:/restock";
    }
}
