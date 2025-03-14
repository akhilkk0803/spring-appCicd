package com.example.ecom_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom_backend.model.Product;
import com.example.ecom_backend.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/")
    public String greet(){
        return "HELLO";
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK) ;
    }
    @GetMapping("/product/{id}")
    public ResponseEntity< Product> getOne(@PathVariable int id){
        Product prod= productService.getProductById(id); 
        return prod!=null?new ResponseEntity<>(prod,HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity<Product>create(@RequestPart Product product,@RequestPart MultipartFile imageFile){
        try {
            Product p=productService.createProduct(product,imageFile);
            return new ResponseEntity<>(p,HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("product/{id}/image")
    public ResponseEntity<?>getImageByProductId(@PathVariable int id){
        try {
            Product product=productService.getProductById(id);
            if(product == null)
                throw new Exception("Product not found");
            byte[]imageFile=product.getImageDate();
            return ResponseEntity.ok().body(imageFile);
        } catch (Exception e) {
           return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }
    @PutMapping("product/{id}")
    public ResponseEntity<Product> update(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
        try {
            Product prod=productService.updateProduct(id,product,imageFile);
            if(prod!=null){
                return new ResponseEntity<>(prod,HttpStatus.CREATED);
            }
            else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String>deleteProduct(@PathVariable int id){
        Product product=productService.getProductById(id);
        if(product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        productService.deleteProductById(id);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        return new ResponseEntity<>(productService.searchProductwithKeyword(keyword),HttpStatus.OK);
    }
}
