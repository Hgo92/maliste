package com.monprojet.demo_backend.controller;

import com.monprojet.demo_backend.model.*;
import com.monprojet.demo_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Ma fonction pour enregister un utilisateur
    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Je prends le mdp de l'utilisateur, je le hash et je le renvoie

        MyList list = new MyList();
        list.setTitle("La liste de course de " + user.getUsername()); // Je set le titre de la liste à partir de l'username 
        user.setMyList(list); // Je lie la liste à l'user

        return userRepository.save(user);
    }

    // Ma fonction pour modifier un utilisateur
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id) // Je cherche l'user par id 
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé")); // erreur si je le trouve pas

        user.setUsername(userDetails.getUsername()); // Je modifie username et password
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        return userRepository.save(user); // Je sauvegarde le "nouvel" user
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
