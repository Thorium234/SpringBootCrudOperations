package com.thorium234.CRUD.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thorium234.CRUD.models.product;

public interface ProductsRepository extends JpaRepository<product,Integer>{
  
}
