package com.lostfound.controller;

import com.lostfound.model.Item;
import com.lostfound.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = adminService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Item> approveItem(@PathVariable Long id) {
        try {
            Item item = adminService.approveItem(id);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            adminService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Item> updateItemStatus(@PathVariable Long id, @RequestParam Item.ItemStatus status) {
        try {
            Item item = adminService.updateItemStatus(id, status);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
