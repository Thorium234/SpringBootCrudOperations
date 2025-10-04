package com.thorium234.CRUD.models;

import jakarta.validation.constraints.*;

public class UserDto {
    
    @NotEmpty
    @Size(min = 3, max = 20)
    private String username;
    
    @NotEmpty
    @Email
    private String email;
    
    @NotEmpty
    @Size(min = 6)
    private String password;
    
    @NotEmpty
    private String confirmPassword;
    
    private String fullName;
    private String phone;
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
