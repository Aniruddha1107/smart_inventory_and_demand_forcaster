package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="forecasts")
@Data @NoArgsConstructor @AllArgsConstructor
public class Forecast {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long forecastId;

	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	private LocalDate forecastDate;
	private Double predictedDemand;
	private Double mapeScore;
	
	@Column(length=50)
	private String modelUsed;
	
	private LocalDateTime generatedAt=LocalDateTime.now();
}
