package com.example.demo.controller;

import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CatController {
    private final CatService catService;
    @Autowired
    public CatController(CatService catService){
        this.catService = catService;
    }
    @GetMapping("cat/findByName/{name}")
    public Cat findCatByName(@PathVariable("name") String name){
        return catService.findCatByName(name);
    }
    @GetMapping("cat/find/{id}")
    public Cat findCatById(@PathVariable("id") long id){
        return catService.findById(id);
    }
    @GetMapping("cat/getAll")
    public List<Cat> getAll() {
        return catService.findAll();
    }
    @PostMapping("cat/add")
    public Cat add(@RequestBody Cat body) {
            return catService.saveCat(body);
    }
    @PutMapping("cat/update")
    public Cat update(@RequestBody Cat body) {
            return catService.updateCat(body);
    }
    @DeleteMapping("cat/deleteById/{id}")
    public void delete(@PathVariable("id") Long id) {
            catService.deleteById(id);
    }
}
