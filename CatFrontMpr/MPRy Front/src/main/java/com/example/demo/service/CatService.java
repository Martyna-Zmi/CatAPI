package com.example.demo.service;

import com.example.demo.model.Cat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CatService {
    @Autowired
    RestClient restClient;

    public CatService(RestClient restClient) {
        this.restClient = restClient;
    }
    private final String URL = "http://localhost:8080/cat/";
    public CatService(){
        this.restClient = RestClient.create();
    }
    public Cat findById(Long id){
        Cat cat = restClient.get()
                .uri(URL+"find/"+id)
                .retrieve()
                .body(Cat.class);
        return cat;
    }
    public void saveCat(Cat cat){
        ResponseEntity<Void> response = restClient.post()
                .uri(URL+"add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cat)
                .retrieve()
                .toBodilessEntity();
    }
    public List<Cat> findAll(){
        List<Cat> cats = restClient.get().
                uri(URL+"getAll")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return cats;

    }
    public void deleteById(Long id){
        restClient.delete().uri(URL+"deleteById/"+id)
                .retrieve()
                .toBodilessEntity();
    }
    public void updateCat(Cat cat){
        restClient.put().uri(URL+"update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cat)
                .retrieve()
                .toBodilessEntity();
    }
}
