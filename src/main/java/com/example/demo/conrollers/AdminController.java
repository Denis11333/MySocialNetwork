package com.example.demo.conrollers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/Admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> allUsers = userRepository.findAll();

        model.addAttribute("users", allUsers);

        return "AdminPage";
    }

    @PostMapping
    public String change(Model model, @ModelAttribute("User") User user) {
        User updatedUser = userRepository.getById(user.getId());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setFacultyNumber(user.getFacultyNumber());
        updatedUser.setGroupNumber(user.getGroupNumber());

        userRepository.save(updatedUser);

        List<User> allUsers = userRepository.findAll();

        model.addAttribute("users", allUsers);

        return "AdminPage";
    }
}
