package com.lostfound.controller;

import com.lostfound.dto.ItemRequest;
import com.lostfound.model.Item;
import com.lostfound.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody ItemRequest request) {
        try {
            System.out.println("Adding item: " + request.getTitle());
            
            // Get authenticated user from token (simplified approach)
            Item item = itemService.addItem(request, null); // Let service handle user assignment
            System.out.println("Item added successfully with ID: " + item.getId());
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            System.err.println("Error adding item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<Item>> getMyItems() {
        try {
            // For now, return all items (we'll fix authentication later)
            List<Item> items = itemService.getAllItems();
            System.out.println("Found " + items.size() + " items for user");
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            System.err.println("Error getting user items: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        try {
            List<Item> items = itemService.getAllItems();
            System.out.println("Found " + items.size() + " items");
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            System.err.println("Error getting all items: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id, "test@example.com");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam String keyword) {
        try {
            List<Item> items = itemService.searchItems(keyword);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Item> updateItemStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            System.out.println("=== Status Update Request ===");
            System.out.println("Item ID: " + id);
            System.out.println("New Status: " + status);
            
            Item item = itemService.updateItemStatus(id, Item.ItemStatus.valueOf(status.toUpperCase()));
            System.out.println("Updated item status: " + item.getStatus());
            System.out.println("Response: " + item);
            
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            System.err.println("Error updating item status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/match")
    public ResponseEntity<Item> matchItem(@PathVariable Long id) {
        try {
            Item item = itemService.matchItem(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
