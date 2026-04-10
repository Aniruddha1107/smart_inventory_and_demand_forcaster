package com.myproject.smartinventory.dto;

import lombok.Data;

@Data
public class SalesDTO {
	private Long productId;
	private Long customerId;
	private Integer quantitySold;
}
