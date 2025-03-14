package com.example.ecom_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom_backend.model.Product;
import com.example.ecom_backend.repo.ProductRepo;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    public Product getProductById(int id) {
        // TODO Auto-generated method stub
        return productRepo.findById(id).orElse(null);
    }
    public Product createProduct(Product product,MultipartFile imageFile) throws IOException{
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageDate(imageFile.getBytes());
        return productRepo.save(product);
    }
    public Product updateProduct(int id, Product product, MultipartFile imageFile) {
        // TODO Auto-generated method stub
        return productRepo.save(product);

    }
    public void deleteProductById(int id) {
        // TODO Auto-generated method stub
        productRepo.deleteById(id);
    }
    public List<Product>searchProductwithKeyword(String keyword){
        return productRepo.searchProducts(keyword);
    }
}
