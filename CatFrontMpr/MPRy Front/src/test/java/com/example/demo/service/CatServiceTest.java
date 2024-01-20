package com.example.demo.service;

import com.example.demo.model.Cat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@RestClientTest
@ExtendWith(MockitoExtension.class)
public class CatServiceTest {
    private final String URL = "http://localhost:8080/cat/";
    private final MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
    private final RestClient.Builder builder = RestClient.builder();
    private CatService service;

    @BeforeEach
    public void init(){
        customizer.customize(builder);
        service = new CatService(builder.build());
    }
    @Test
    public void shouldFindById(){
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"find/1"))
                .andRespond(MockRestResponseCreators.withSuccess(""" 
                        {"id":"1", "name": "MIALEK", "age": 5, "colour": "bialy"} 
                    """, MediaType.APPLICATION_JSON));
        Cat found = service.findById(1L);
        assertEquals(found.getId(), 1);
    }
    @Test
    public void shouldNotFindById(){
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"find/1"))
                .andRespond(MockRestResponseCreators.withBadRequest());
        assertThrows(HttpClientErrorException.class, ()-> {
            service.findById(1L);
        });
    }
    @Test
    public void shouldFindAll(){
        Cat cat = new Cat("MIALEK", 5, "bialy");
        cat.setId(1L);
        Cat cat2 = new Cat("BUBA", 2, "czarny");
        cat2.setId(2L);
        List<Cat> catsInDb = new ArrayList<>();
        catsInDb.add(cat);
        catsInDb.add(cat2);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"getAll"))
                .andRespond(MockRestResponseCreators.withSuccess(""" 
                        [{"id":"1", "name": "MIALEK", "age": 5, "colour": "bialy"},
                        {"id":"2", "name": "BUBA", "age": 2, "colour": "czarny"}]
                    """, MediaType.APPLICATION_JSON));
        List<Cat> catsFound = service.findAll();
        assertEquals(catsFound.get(0).getId(),catsInDb.get(0).getId());
        assertEquals(catsFound.get(0).getName(),catsInDb.get(0).getName());
        assertEquals(catsFound.get(0).getAge(),catsInDb.get(0).getAge());
        assertEquals(catsFound.get(1).getId(),catsInDb.get(1).getId());
        assertEquals(catsFound.get(1).getName(),catsInDb.get(1).getName());
        assertEquals(catsFound.get(1).getAge(),catsInDb.get(1).getAge());
    }
    @Test
    public void shouldSave(){
        Cat cat = new Cat("MIALEK", 5, "bialy");
        cat.setId(1L);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"add"))
                .andRespond(MockRestResponseCreators.withSuccess());
        service.saveCat(cat);
    }
    @Test
    public void shouldNotSave(){
        Cat cat = new Cat("MIALEK", 5, "bialy");
        cat.setId(1L);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"add"))
                .andRespond(MockRestResponseCreators.withBadRequest());
        assertThrows(HttpClientErrorException.class, ()-> {
            service.saveCat(cat);
        });
    }
    @Test
    public void shouldUpdate(){
        Cat cat = new Cat("MIALEK", 5, "bialy");
        cat.setId(1L);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"update"))
                .andRespond(MockRestResponseCreators.withSuccess());
        service.updateCat(cat);
    }
    @Test
    public void shouldNotUpdate(){
        Cat cat = new Cat("MIALEK", 5, "bialy");
        cat.setId(1L);
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"update"))
                .andRespond(MockRestResponseCreators.withBadRequest());
        assertThrows(HttpClientErrorException.class, ()-> {
            service.updateCat(cat);
        });
    }
    @Test
    public void shouldDelete(){
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"deleteById/1"))
                .andRespond(MockRestResponseCreators.withSuccess());
        service.deleteById(1L);
    }
    @Test
    public void shouldNotDelete(){
        customizer.getServer().expect(MockRestRequestMatchers.requestTo(URL+"deleteById/1"))
                .andRespond(MockRestResponseCreators.withBadRequest());
        assertThrows(HttpClientErrorException.class, ()-> {
            service.deleteById(1L);
        });
    }
}