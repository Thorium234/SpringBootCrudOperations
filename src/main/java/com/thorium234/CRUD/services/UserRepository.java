package com.thorium234.CRUD.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thorium234.CRUD.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
