package com.example.demo.repository;

import com.example.demo.model.Cat;
import org.springframework.data.repository.CrudRepository;


public interface CatRepository extends CrudRepository<Cat, Long> {
    Cat findByName(String name);
}
