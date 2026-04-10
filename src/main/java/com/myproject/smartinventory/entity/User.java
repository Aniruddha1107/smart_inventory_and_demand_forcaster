package com.myproject.smartinventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private long userID;
	
	@Column(nullable = false, unique =true, length = 50)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Role role;
	
	@Column(nullable=false)
	private Boolean isActive=true;
	
	private LocalDateTime createdAt=LocalDateTime.now();
	
	
	
}
