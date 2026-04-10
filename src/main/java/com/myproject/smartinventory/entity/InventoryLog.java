package com.myproject.smartinventory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="inventory_log")
@Data @NoArgsConstructor @AllArgsConstructor
public class InventoryLog {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long logId;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ChangeType changeType;
	
	private Integer qtyChange;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private LocalDateTime timestamp =LocalDateTime.now();
}
