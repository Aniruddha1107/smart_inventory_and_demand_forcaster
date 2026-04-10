package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="alerts")
@Data @NoArgsConstructor @AllArgsConstructor
public class Alert {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer alertId;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@Enumerated(EnumType.STRING)
	private AlertType alertType;
	
	@Enumerated(EnumType.STRING)
	private AlertStatus status= AlertStatus.ACTIVE;
	
	private LocalDateTime triggeredAt = LocalDateTime.now();
}
