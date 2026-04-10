package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.service.ForecastService;
import com.myproject.smartinventory.service.ProductService;

@Controller
@RequestMapping("/forecast")
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private ProductService productService;

    @PostMapping("/generate/{id}")
    public String generateForecast(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        forecastService.generateForecast(product);
        return "redirect:/products/" + id;
    }
}
