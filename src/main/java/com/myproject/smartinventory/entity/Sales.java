package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="sales")
@Data @NoArgsConstructor @AllArgsConstructor
public class Sales {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long saleId;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@Column(nullable=false)
	private LocalDate saleDate;
	
	@Column(nullable=false)
	private Integer quantitySold;
	
	@Column(precision=12, scale=2)
	private BigDecimal totalAmount;
	
}
