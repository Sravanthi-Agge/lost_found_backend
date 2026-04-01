package com.lostfound.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ItemRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotBlank(message = "Type is required")
    private String type; // LOST or FOUND
    
    public ItemRequest() {}
    
    public ItemRequest(String title, String description, String category, String location, String type) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.type = type;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
