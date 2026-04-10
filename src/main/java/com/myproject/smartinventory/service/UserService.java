package com.myproject.smartinventory.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myproject.smartinventory.dto.UserDTO;
import com.myproject.smartinventory.entity.Role;
import com.myproject.smartinventory.entity.User;
import com.myproject.smartinventory.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User registerUser(UserDTO dto) {
		com.myproject.smartinventory.entity.User user=new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(Role.valueOf(dto.getRole()));
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
}
