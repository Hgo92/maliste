package com.monprojet.demo_backend.controller;

import com.monprojet.demo_backend.model.*;
import com.monprojet.demo_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/setup")
    public String setupTest() {
        User user = new User();
        user.setUsername("Hugo");

        MyList list = new MyList();
        list.setTitle("La liste d'Hugo");

        list.addItem(new Item("Café"));
        list.addItem(new Item("PQ"));

        user.setMyList(list);

        return "Utilisateur 'Hugo' a créé sa liste";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
