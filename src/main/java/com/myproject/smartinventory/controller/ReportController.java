package com.myproject.smartinventory.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myproject.smartinventory.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String reportHub(Model model) {
        // Default: current month
        LocalDate from = LocalDate.now().withDayOfMonth(1);
        LocalDate to = LocalDate.now();
        Map<String, Object> salesSummary = reportService.getSalesSummary(from, to);
        model.addAttribute("salesSummary", salesSummary);
        model.addAttribute("segments", reportService.getCustomerSegmentSummary());
        return "reports/hub";
    }

    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {
        if (from == null)
            from = LocalDate.now().withDayOfMonth(1);
        if (to == null)
            to = LocalDate.now();
        Map<String, Object> summary = reportService.getSalesSummary(from, to);
        model.addAttribute("summary", summary);
        return "reports/sales";
    }

    @GetMapping("/inventory")
    public String inventoryReport(Model model) {
        model.addAttribute("report", reportService.getInventoryValuationReport());
        return "reports/inventory";
    }

    // ─── CSV Exports ─────────────────────────────────────────

    @GetMapping("/export/sales")
    public void exportSalesCSV(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletResponse response) throws IOException {
        if (from == null)
            from = LocalDate.now().withDayOfMonth(1);
        if (to == null)
            to = LocalDate.now();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=sales_report_" + from + "_to_" + to + ".csv");

        Map<String, Object> summary = reportService.getSalesSummary(from, to);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) summary.get("productRows");

        PrintWriter writer = response.getWriter();
        writer.println("Product,Units Sold,Revenue (INR)");
        for (Map<String, Object> row : rows) {
            writer.printf("\"%s\",%s,%s%n", row.get("product"), row.get("units"), row.get("revenue"));
        }
        writer.println();
        writer.printf("TOTAL,,%s%n", summary.get("totalRevenue"));
        writer.flush();
    }

    @GetMapping("/export/inventory")
    public void exportInventoryCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=inventory_valuation.csv");

        Map<String, Object> report = reportService.getInventoryValuationReport();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) report.get("rows");

        PrintWriter writer = response.getWriter();
        writer.println("SKU,Name,Category,Price (INR),Quantity,Value (INR),Status");
        for (Map<String, Object> row : rows) {
            writer.printf("\"%s\",\"%s\",\"%s\",%s,%s,%s,\"%s\"%n",
                    row.get("sku"), row.get("name"), row.get("category"),
                    row.get("price"), row.get("quantity"), row.get("value"), row.get("status"));
        }
        writer.println();
        writer.printf("TOTAL PORTFOLIO VALUE,,,,,,%s%n", report.get("totalValue"));
        writer.flush();
    }
}
