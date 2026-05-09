package com.monprojet.demo_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;

    // Relations
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;
    
    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference 
    private MyList list;

    @JsonProperty("isArchived")
    public boolean isArchived() {
        return this.list == null;
    }

    // Constructeurs
    public Item() {}

    public Item(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
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
    public void addQuantity() {this.quantity += 1;}
    public void minusQuantity() {
        if (this.quantity > 1) {
            this.quantity -= 1;
        } else {
            this.setQuantity(0);
        }
    }

    public User getOwner() {return this.owner;}
    public void setOwner(User owner) {this.owner = owner;}
}