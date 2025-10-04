package com.thorium234.CRUD.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thorium234.CRUD.models.ProductDto;
import com.thorium234.CRUD.models.product;
import com.thorium234.CRUD.services.ProductsRepository;

import jakarta.validation.Valid;
import java.io.InputStream;
import java.nio.file.*;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;
	
	@GetMapping({"","/"})
	public String showProductList(Model model) {
		List<product> products = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
		model.addAttribute("products",products);
		return "products/index";
	}
	
	@GetMapping({"/create"})
	public String showCreatePage(Model model) {
		ProductDto productdto = new ProductDto();
		model.addAttribute("productDto",productdto);
		return "products/CreateProduct";
	}
	
	@PostMapping({"/create"})
	public String createProduct (
	    @Valid @ModelAttribute ProductDto productdto,
	    BindingResult result
	) {
	    
	    // Check if imageFile is null OR empty
	    if (productdto.getImageFile() == null || productdto.getImageFile().isEmpty()) {
	        result.addError(new FieldError("productDto","imageFile","The image file is required"));
	    }
	    
	    if (result.hasErrors()) {
	        return "products/CreateProduct";
	    }
	    
	    // Save the uploaded image file
	    MultipartFile image = productdto.getImageFile();
	    Date createdAt = new Date();//helps us generate unique file name for the image
	    String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
	    //storage file name is unique in search a way that we add time_image.png
	    try {
	        String uploadDir = "public/images/";//the image should be saved here
	        Path uploadPath = Paths.get(uploadDir);
	        
	        if (!Files.exists(uploadPath)) { //if path dont exist we will create it 
	            Files.createDirectories(uploadPath);
	        }
	        
	        try (InputStream inputStream = image.getInputStream()) {
	            Files.copy(inputStream, Paths.get(uploadDir + storageFileName), //where the image will be stored
	                StandardCopyOption.REPLACE_EXISTING);
	        }
	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	    }
	    
	    // Create and save the product in the database
	    product newProduct = new product();
	    newProduct.setName(productdto.getName());
	    newProduct.setBrand(productdto.getBrand());
	    newProduct.setCategory(productdto.getCategory());
	    newProduct.setPrice(productdto.getPrice());
	    newProduct.setDescription(productdto.getDescription());
	    newProduct.setCreatedAt(createdAt);
	    newProduct.setImageFileName(storageFileName);
	    
	    repo.save(newProduct);  // we are  saving the product 
	    
	    return "redirect:/products";
	}
	//we are creating a new image controllercthat allows us to display the page that allo the user to update the details
	// GET method - Show the edit form
	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {
	    try {
	        product existingProduct = repo.findById(id).get();
	        model.addAttribute("product", existingProduct);
	        
	        ProductDto productDto = new ProductDto();
	        productDto.setName(existingProduct.getName());
	        productDto.setBrand(existingProduct.getBrand());
	        productDto.setCategory(existingProduct.getCategory());
	        productDto.setPrice(existingProduct.getPrice());
	        productDto.setDescription(existingProduct.getDescription());
//	        productDto.setCreatedAt(existingProduct.getCreatedAt());
	        
	        model.addAttribute("productDto", productDto);
	        
	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	        return "redirect:/products";
	    }
	    
	    return "products/EditProduct";
	}

	// POST method - Process the update
	@PostMapping("/edit")
	public String updateProduct(
	    Model model,
	    @RequestParam int id,
	    @Valid @ModelAttribute ProductDto productDto,
	    BindingResult result  //if the product is valid
	) {
	    //reading the products from the database
	    try {
	        product existingProduct = repo.findById(id).get();
	        model.addAttribute("product", existingProduct);
	        
	        if (result.hasErrors()) {
	            return "products/EditProduct";
	        }
	        
	        // Check if new image was uploaded
	        if (!productDto.getImageFile().isEmpty()) {
	            // Delete old image
	            String uploadDir = "public/images/";
	            Path oldImagePath = Paths.get(uploadDir + existingProduct.getImageFileName());
	            
	            try {
	                Files.delete(oldImagePath);
	            } catch (Exception ex) {
	                System.out.println("Exception: " + ex.getMessage());
	            }
	            
	            // Save new image
	            MultipartFile image = productDto.getImageFile();
	            Date createdAt = new Date();
	            String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
	            
	            try (InputStream inputStream = image.getInputStream()) {
	                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
	                    StandardCopyOption.REPLACE_EXISTING);
	            }
	            
	            existingProduct.setImageFileName(storageFileName);
	        }
	        
	        // Update product details
	        existingProduct.setName(productDto.getName());
	        existingProduct.setBrand(productDto.getBrand());
	        existingProduct.setCategory(productDto.getCategory());
	        existingProduct.setPrice(productDto.getPrice());
	        existingProduct.setDescription(productDto.getDescription());
//	        existingProduct.setCreatedAt(productDto.getCreatedAt());
	        
	        repo.save(existingProduct);
	        
	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	    }
	    
	    return "redirect:/products";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {
	    try {
	        product existingProduct = repo.findById(id).get();
	        
	        // Delete the image file from storage
	        Path imagePath = Paths.get("public/images/" + existingProduct.getImageFileName());
	        
	        try {
	            Files.delete(imagePath);
	        } catch (Exception ex) {
	            System.out.println("Exception while deleting image: " + ex.getMessage());
	        }
	        
	        // Delete the product from database
	        repo.delete(existingProduct);
	        
	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	    }
	    
	    return "redirect:/products";
	}
}
