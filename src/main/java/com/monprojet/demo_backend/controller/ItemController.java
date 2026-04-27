package com.monprojet.demo_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import com.monprojet.demo_backend.model.*;
import com.monprojet.demo_backend.repository.ItemRepository;
import com.monprojet.demo_backend.repository.UserRepository;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    // Ma route pour les récupérers tous les objets (dans sa liste)
    @GetMapping("/me")
    public ResponseEntity<List<Item>> getMyAllItems(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Item> allItems = itemRepository.findByOwner(user); // ← direct, propre
        return ResponseEntity.ok(allItems);
    }

    // Ma route pour créer un objet 
    @PostMapping("/add")
    @Transactional
    public Item addItem(@RequestBody Item newItem, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        MyList list = user.getMyList();
        list.addItem(newItem);
        newItem.setOwner(user);
        
        return itemRepository.save(newItem);
    }

    // Ma route pour modifier un objet 
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item non trouvé"));

            item.setName(itemDetails.getName());
            item.setQuantity(itemDetails.getQuantity());

            return itemRepository.save(item);
    }

    // Mes routes pour attacher/détacher un objet d'une liste    
    @PutMapping("/{id}/detach")
    @Transactional
    public ResponseEntity<String> detachItem(@PathVariable Long id, Principal principal) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item non trouvé"));

        if (!item.getOwner().getUsername().equals(principal.getName())) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
    }

        MyList userList = item.getList();
        userList.removeItem(item);
        itemRepository.save(item);

        return ResponseEntity.ok("L'item a été retiré de votre liste.");
    }

    @PutMapping("/{id}/attach")
    @Transactional
    public ResponseEntity<String> attachItem(@PathVariable Long id, Principal principal) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item non trouvé"));

        User user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.getMyList().addItem(item);
        itemRepository.save(item);
        return ResponseEntity.ok("L'item a été ajouté à votre liste.");
    }

    // Mes routes pour changer la quantité d'un item

    @PutMapping("/{id}/plus")
    @Transactional
    public ResponseEntity<String> addOneItem(@PathVariable Long id, Principal principal) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item non trouvé"));

        if (!item.getOwner().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
    } 
    item.addQuantity();
    itemRepository.save(item);

    return ResponseEntity.ok("La quantité de l'item a été augmentée.");
    }

    @PutMapping("/{id}/minus")
    @Transactional
    public ResponseEntity<String> deleteOneItem(@PathVariable Long id, Principal principal) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item non trouvé"));

        if (!item.getOwner().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
    } 

    item.minusQuantity();
    itemRepository.save(item);

    return ResponseEntity.ok("La quantité de l'item a été diminuée.");
    }

    // Ma route pour supprimer 
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }

}