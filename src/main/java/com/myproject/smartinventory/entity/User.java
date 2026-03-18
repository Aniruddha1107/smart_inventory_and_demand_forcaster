package com.myproject.smartinventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
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
	@Column(nulllable=false)
	private String status="ACTIVE";
	
}
