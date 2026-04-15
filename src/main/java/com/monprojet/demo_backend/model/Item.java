package com.monprojet.demo_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference // Empêche l'item de ré-afficher la liste (évite la boucle)
    private MyList list;

    // Constructeurs
    public Item() {}

    public Item(String name) {
        this.name = name;
        this.quantity = 1;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public MyList getList() { return list; }
    public void setList(MyList list) { this.list = list; }

    public int getQuantity() {return this.quantity;}
    public void setQuantity(int num) {this.quantity = num;}
}