package com.monprojet.demo_backend.repository;

import com.monprojet.demo_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
}
