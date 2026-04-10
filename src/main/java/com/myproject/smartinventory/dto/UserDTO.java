package com.myproject.smartinventory.dto;
import lombok.Data;

@Data
public class UserDTO {
	private String username;
	private String password;
	private String email;
	private String role;
}
