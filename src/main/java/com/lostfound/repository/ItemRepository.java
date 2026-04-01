package com.lostfound.repository;

import com.lostfound.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    List<Item> findByUserId(Long userId);
    
    List<Item> findByType(Item.ItemType type);
    
    List<Item> findByStatus(Item.ItemStatus status);
    
    List<Item> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    
    @Query("SELECT i FROM Item i WHERE " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "i.type != :type")
    List<Item> findPotentialMatches(@Param("keyword") String keyword, @Param("type") Item.ItemType type);
    
    @Query("SELECT i FROM Item i WHERE " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%')) AND " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :category, '%')) AND " +
           "i.type != :type")
    List<Item> findMatchesByLocationAndCategory(@Param("location") String location, 
                                               @Param("category") String category, 
                                               @Param("type") Item.ItemType type);
}
