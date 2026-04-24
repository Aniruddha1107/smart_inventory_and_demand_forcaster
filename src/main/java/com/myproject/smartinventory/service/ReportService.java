package com.myproject.smartinventory.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.entity.Customer;
import com.myproject.smartinventory.entity.Product;
import com.myproject.smartinventory.entity.Sales;
import com.myproject.smartinventory.repository.CustomerRepository;
import com.myproject.smartinventory.repository.ProductRepository;
import com.myproject.smartinventory.repository.SalesRepository;

@Service
public class ReportService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ─── Sales Summary ───────────────────────────────────────
    public Map<String, Object> getSalesSummary(LocalDate from, LocalDate to) {
        List<Sales> allSales = salesRepository.findAll();
        List<Sales> filtered = allSales.stream()
                .filter(s -> !s.getSaleDate().isBefore(from) && !s.getSaleDate().isAfter(to))
                .collect(Collectors.toList());

        BigDecimal totalRevenue = filtered.stream()
                .map(Sales::getTotalAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalUnits = filtered.stream().mapToInt(Sales::getQuantitySold).sum();

        // Revenue per product
        Map<String, BigDecimal> revenueByProduct = new LinkedHashMap<>();
        Map<String, Integer> unitsByProduct = new LinkedHashMap<>();
        for (Sales s : filtered) {
            String name = s.getProduct().getName();
            revenueByProduct.merge(name, s.getTotalAmount() != null ? s.getTotalAmount() : BigDecimal.ZERO,
                    BigDecimal::add);
            unitsByProduct.merge(name, s.getQuantitySold(), Integer::sum);
        }

        // Sort by revenue desc
        List<Map<String, Object>> productRows = new ArrayList<>();
        revenueByProduct.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(e -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("product", e.getKey());
                    row.put("units", unitsByProduct.getOrDefault(e.getKey(), 0));
                    row.put("revenue", e.getValue());
                    productRows.add(row);
                });

        BigDecimal avgOrderValue = filtered.isEmpty() ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(filtered.size()), 2, java.math.RoundingMode.HALF_UP);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalUnits", totalUnits);
        summary.put("totalTransactions", filtered.size());
        summary.put("avgOrderValue", avgOrderValue);
        summary.put("productRows", productRows);
        summary.put("from", from);
        summary.put("to", to);
        return summary;
    }

    // ─── Inventory Valuation ──────────────────────────────────
    public Map<String, Object> getInventoryValuationReport() {
        List<Product> products = productRepository.findByIsActiveTrue();

        List<Map<String, Object>> rows = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product p : products) {
            BigDecimal value = p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity()));
            totalValue = totalValue.add(value);

            String status;
            if (p.getQuantity() <= 0)
                status = "Out of Stock";
            else if (p.getQuantity() <= p.getSafetyStock())
                status = "Low Stock";
            else
                status = "In Stock";

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("sku", p.getSku());
            row.put("name", p.getName());
            row.put("category", p.getCategory());
            row.put("price", p.getPrice());
            row.put("quantity", p.getQuantity());
            row.put("value", value);
            row.put("status", status);
            rows.add(row);
        }

        // Sort by value desc
        rows.sort(Comparator.comparing(r -> ((BigDecimal) r.get("value"))));
        java.util.Collections.reverse(rows);

        Map<String, Object> report = new HashMap<>();
        report.put("rows", rows);
        report.put("totalValue", totalValue);
        report.put("totalSKUs", products.size());
        return report;
    }

    // ─── Customer Segment Summary ─────────────────────────────
    public Map<String, Long> getCustomerSegmentSummary() {
        List<Customer> customers = customerRepository.findAll();
        Map<String, Long> segments = new LinkedHashMap<>();
        segments.put("Champions", 0L);
        segments.put("Loyal", 0L);
        segments.put("At-Risk", 0L);
        segments.put("Lost", 0L);
        segments.put("Unsegmented", 0L);
        for (Customer c : customers) {
            String seg = c.getRfmSegment();
            if (seg == null || seg.isBlank())
                seg = "Unsegmented";
            segments.merge(seg, 1L, Long::sum);
        }
        return segments;
    }
}
