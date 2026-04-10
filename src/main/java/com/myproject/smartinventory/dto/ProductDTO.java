package com.myproject.smartinventory.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDTO {
	private String name;
	private String category;
	private BigDecimal price;
	private Integer quantity;
	private Integer safetyStock;
	private String sku;
}
