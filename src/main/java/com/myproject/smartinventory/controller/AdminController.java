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
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "admin/users/add";
    }

    @PostMapping("/add")
    public String addUserSubmit(@ModelAttribute UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/dashboard?userAdded=true";
    }
}
