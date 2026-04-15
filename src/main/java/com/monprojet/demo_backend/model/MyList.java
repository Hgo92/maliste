package com.monprojet.demo_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "my_lists")
public class MyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne(mappedBy = "myList")
    @JsonBackReference // Empêche la liste de réafficher l'utilisateur
    private User user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Autorise la liste à afficher ses items
    private List<Item> items = new ArrayList<>();

    public MyList() {}
   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    
    
    public void addItem(Item item) {
        items.add(item);
        item.setList(this);
    }
}