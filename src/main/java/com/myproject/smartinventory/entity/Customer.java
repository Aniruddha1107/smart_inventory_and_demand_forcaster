package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="customers")
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long customerId;
	
	@Column(nullable=false, length=200)
	private String name;
	
	@Column(nullable=false, length=200)
	private String email;
	
	private Integer recency=0;
	private Integer frequency=0;
	
	@Column(precision=12, scale=2)
	private BigDecimal monetaryValue = BigDecimal.ZERO;
	
	@Column(length=50)
	private String rfmSegment;
}
