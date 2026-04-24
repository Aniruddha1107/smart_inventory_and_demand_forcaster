package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.myproject.smartinventory.dto.PurchaseOrderDTO;
import com.myproject.smartinventory.security.CustomUserDetails;
import com.myproject.smartinventory.service.ProductService;
import com.myproject.smartinventory.service.PurchaseOrderService;

import jakarta.validation.Valid;
import java.util.Objects;

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
    public String createPO(@Valid @ModelAttribute("purchaseOrderDTO") PurchaseOrderDTO dto,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (hasDuplicateProducts(dto)) {
            bindingResult.reject("purchaseOrder.items.duplicate",
                    "Duplicate products are not allowed in the same purchase order.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.getAllActiveProducts());
            return "restock/create";
        }
        try {
            purchaseOrderService.createPO(dto, userDetails.getUser());
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("purchaseOrder.invalid", ex.getMessage());
            model.addAttribute("products", productService.getAllActiveProducts());
            return "restock/create";
        }
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

    private boolean hasDuplicateProducts(PurchaseOrderDTO dto) {
        if (dto == null || dto.getItems() == null) {
            return false;
        }
        long distinctProducts = dto.getItems().stream()
                .map(PurchaseOrderDTO.PurchaseOrderItemDTO::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return distinctProducts != dto.getItems().size();
    }
}
