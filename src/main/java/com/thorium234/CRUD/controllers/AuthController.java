package com.thorium234.CRUD.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.thorium234.CRUD.models.User;
import com.thorium234.CRUD.models.UserDto;
import com.thorium234.CRUD.services.UserRepository;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute UserDto userDto,
        BindingResult result
    ) {
        // Check if username already exists
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            result.addError(new FieldError("userDto", "username", "Username already exists"));
        }
        
        // Check if passwords match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.addError(new FieldError("userDto", "confirmPassword", "Passwords do not match"));
        }
        
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Create new user
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        
        userRepository.save(user);
        
        return "redirect:/login?registered";
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        
        return "auth/profile";
    }
    
    @GetMapping("/profile/edit")
    public String showEditProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username);
        
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        
        model.addAttribute("userDto", userDto);
        model.addAttribute("user", user);
        
        return "auth/editProfile";
    }
    
    @PostMapping("/profile/edit")
    public String updateProfile(
        @Valid @ModelAttribute UserDto userDto,
        BindingResult result,
        Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User user = userRepository.findByUsername(currentUsername);
        
        model.addAttribute("user", user);
        
        // Check if new username is taken by someone else
        if (!userDto.getUsername().equals(currentUsername)) {
            User existingUser = userRepository.findByUsername(userDto.getUsername());
            if (existingUser != null) {
                result.addError(new FieldError("userDto", "username", "Username already taken"));
            }
        }
        
        if (result.hasErrors()) {
            return "auth/editProfile";
        }
        
        // Update user details
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        
        // Update password if provided
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.addError(new FieldError("userDto", "confirmPassword", "Passwords do not match"));
                return "auth/editProfile";
            }
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        
        userRepository.save(user);
        
        return "redirect:/profile?updated";
    }
}
