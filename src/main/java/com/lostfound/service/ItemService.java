package com.lostfound.service;

import com.lostfound.dto.ItemRequest;
import com.lostfound.model.Item;
import com.lostfound.model.User;
import com.lostfound.repository.ItemRepository;
import com.lostfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public Item addItem(ItemRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setType(Item.ItemType.valueOf(request.getType().toUpperCase()));
        item.setUser(user);

        item = itemRepository.save(item);

        checkForMatches(item);

        return item;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public void deleteItem(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUser().getId().equals(user.getId()) && !user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You can only delete your own items");
        }

        itemRepository.delete(item);
    }

    public List<Item> getItemsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return itemRepository.findByUserId(user.getId());
    }

    private void checkForMatches(Item newItem) {
        List<Item> potentialMatches;
        
        if (newItem.getType() == Item.ItemType.LOST) {
            potentialMatches = itemRepository.findMatchesByLocationAndCategory(
                newItem.getLocation(), newItem.getCategory(), Item.ItemType.FOUND);
        } else {
            potentialMatches = itemRepository.findMatchesByLocationAndCategory(
                newItem.getLocation(), newItem.getCategory(), Item.ItemType.LOST);
        }

        for (Item match : potentialMatches) {
            if (isStrongMatch(newItem, match)) {
                newItem.setStatus(Item.ItemStatus.MATCHED);
                match.setStatus(Item.ItemStatus.MATCHED);
                itemRepository.save(match);
                break;
            }
        }
        
        itemRepository.save(newItem);
    }

    private boolean isStrongMatch(Item item1, Item item2) {
        boolean locationMatch = item1.getLocation().equalsIgnoreCase(item2.getLocation());
        boolean categoryMatch = item1.getCategory().equalsIgnoreCase(item2.getCategory());
        
        String title1 = item1.getTitle().toLowerCase();
        String title2 = item2.getTitle().toLowerCase();
        boolean titleSimilarity = calculateSimilarity(title1, title2) > 0.6;
        
        return locationMatch && categoryMatch && titleSimilarity;
    }

    private double calculateSimilarity(String s1, String s2) {
        String longer = s1.length() > s2.length() ? s1 : s2;
        String shorter = s1.length() > s2.length() ? s2 : s1;
        
        if (longer.length() == 0) return 1.0;
        
        return (longer.length() - editDistance(longer, shorter)) / (double) longer.length();
    }

    private int editDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }

    public Item updateItemStatus(Long id, Item.ItemStatus status) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setStatus(status);
        return itemRepository.save(item);
    }

    public List<Item> searchItems(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return itemRepository.findAll();
        }
        
        String searchKeyword = keyword.trim().toLowerCase();
        return itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchKeyword, searchKeyword);
    }
}
