package com.lostfound.service;

import com.lostfound.model.Item;
import com.lostfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item approveItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setStatus(Item.ItemStatus.PENDING);
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
        
        itemRepository.delete(item);
    }

    public Item updateItemStatus(Long id, Item.ItemStatus status) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setStatus(status);
        return itemRepository.save(item);
    }
}
