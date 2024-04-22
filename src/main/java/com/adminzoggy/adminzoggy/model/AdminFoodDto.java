package com.adminzoggy.adminzoggy.model;

import org.springframework.web.multipart.MultipartFile;

public class AdminFoodDto {

    private String name;
    private String type;
    private String category;
    private double price;
    private String description;
    private MultipartFile imageFilename;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(MultipartFile imageFilename) {
        this.imageFilename = imageFilename;
    }
}
