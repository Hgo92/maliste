package com.monprojet.demo_backend.repository;

import com.monprojet.demo_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Récupérer les items d'une liste/user spécifique
    List<Item> findByOwnerUsername(String username);
}
