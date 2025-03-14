package com.example.ecom_backend.model;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private String category;
    private int stockQuantity;
    private boolean productAvailable;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy")
    private Date releaseDate;
    private String imageName;
    private String imageType;
    @Lob
    private byte[]imageDate;
}
