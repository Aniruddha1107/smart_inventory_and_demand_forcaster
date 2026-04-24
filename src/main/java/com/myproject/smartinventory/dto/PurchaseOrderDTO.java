package com.myproject.smartinventory.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseOrderDTO {
    private String supplier;
    private List<PurchaseOrderItemDTO> items;

    @Data
    public static class PurchaseOrderItemDTO {
        private Long productId;
        private Integer quantityOrdered;
        private BigDecimal unitCost;
    }
}
