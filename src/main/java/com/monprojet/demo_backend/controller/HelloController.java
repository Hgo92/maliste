package com.monprojet.demo_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController // Indique que cette classe répond à des requêtes Web
    public class HelloController {
    @GetMapping("/api/hello") // Définit l'URL
    public String sayHello() {
        return "Bonjour ! Votre backend Spring Boot fonctionne parfaitement.";
    }
    
}
