package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="products")
@Data @NoArgsConstructor @AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long productId;
	
	@Column(nullable=false, length=200)
	private String name;
	
	@Column(length=100)
	private String category;
	
	@Column(nullable=false, precision=10, scale=2)
	private BigDecimal price;
	
	@Column(nullable=false)
	private Integer quantity=0;
	
	private Integer safetyStock=0;
	
	@Column(nullable=false, unique=true, length=50)
	private String sku;
	
	private Boolean isActive=true;
	
}
