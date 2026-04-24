package com.myproject.smartinventory.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PurchaseOrderDTO {
    @NotBlank(message = "Supplier name is required.")
    private String supplier;

    @NotEmpty(message = "At least one purchase order item is required.")
    private List<@Valid PurchaseOrderItemDTO> items;

    @Data
    public static class PurchaseOrderItemDTO {
        @NotNull(message = "Product is required.")
        private Long productId;

        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity must be greater than 0.")
        private Integer quantityOrdered;

        @NotNull(message = "Unit cost is required.")
        @DecimalMin(value = "0.0", inclusive = true, message = "Unit cost cannot be negative.")
        private BigDecimal unitCost;
    }
}
