package com.myproject.smartinventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myproject.smartinventory.dto.UserDTO;
import com.myproject.smartinventory.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "auth/register";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute UserDTO userDTO) {
		userService.registerUser(userDTO);
		return "redirect:/auth/login?registered=true";
	}
}
