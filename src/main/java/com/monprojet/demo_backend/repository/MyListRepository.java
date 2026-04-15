package com.monprojet.demo_backend.repository;

import com.monprojet.demo_backend.model.MyList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyListRepository extends JpaRepository<MyList, Long> {
    
}
