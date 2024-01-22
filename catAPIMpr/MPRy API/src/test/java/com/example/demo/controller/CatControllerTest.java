package com.example.demo.controller;

import com.example.demo.exceptions.*;
import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CatControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CatService catService;
    @InjectMocks
    private CatController catController;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(new CatExceptionHandler(), catController).build();
    }
    @Test
    public void getByIdReturns200WhenCatIsPresent() throws Exception {
        //given
        Cat cat = new Cat("Milek", 2, "czarny");
        cat.setId(1L);
        //when
        when(catService.findById(1L)).thenReturn(cat);
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/find/1"))
                .andExpect(jsonPath("$.name").value("Milek"))
                .andExpect(jsonPath("$.age").value(2))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colour").value("czarny"))
                .andExpect(status().isOk());
    }
    @Test
    public void getByIdReturns404WhenCatNotFound() throws Exception {
        //given
        Cat cat = new Cat("Milek", 2, "czarny");
        cat.setId(1L);
        //when
        when(catService.findById(cat.getId())).thenThrow(new CatNotFoundException());
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/find/1")).andExpect(status().isNotFound());
    }
    @Test
    public void addReturns200WhenCatDoesntExistYet() throws Exception {
        //given
        Cat cat = new Cat("Milek", 2, "czarny");
        cat.setId(1L);
        //when
        when(catService.saveCat(any())).thenReturn(cat);
        //then
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": 2, \"colour\": \"czarny\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void addReturns400WhenCatAlreadyExists() throws Exception {
        //when
        when(catService.saveCat(any())).thenThrow(new CatAlreadyExistsException());
        //then
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": 2, \"colour\": \"czarny\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void addReturns400WhenAgeIsNegative() throws Exception {
        Cat impossibleCat = new Cat("Milek", -3, "czarny");
        when(catService.saveCat(any())).thenThrow(new AgeIsNegativeException());
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": -3,  \"colour\": \"czarny\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns200WhenCatIsUpdated() throws Exception {
        //given
        Cat catfromDb = new Cat("Miecio", 7, "bialy");
        catfromDb.setId(3L);
        //when
        when(catService.updateCat(any())).thenReturn(catfromDb);
        //then
        mockMvc.perform(put("/cat/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Miecio\", \"age\": 7, \"id\": 3,  \"colour\": \"czarny\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void updateReturns400WhenIncompleteData() throws Exception {
        //when
        when(catService.updateCat(any())).thenThrow(new IncompleteDataException("Please provide necessary data"));
        //then
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": 7, \"id\": 1,  \"colour\": \"czarny\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns400WhenAgeIsNegative() throws Exception {
        //when
        when(catService.updateCat(any())).thenThrow(new AgeIsNegativeException());
        //then
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": -7, \"id\": 1,  \"colour\": \"czarny\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns404WhenCatNotFound() throws Exception {
        //when
        when(catService.updateCat(any())).thenThrow(new CatNotFoundException());
        //then
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": -7, \"id\": 1,  \"colour\": \"czarny\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void findCatByNameReturns200WhenOK() throws Exception {
        //when
        when(catService.findCatByName("Mietek")).thenReturn(new Cat("Mietek", 2, "czarny"));
        //then
        mockMvc.perform(get("/cat/findByName/Mietek")).andExpect(status().isOk());
    }
    @Test
    public void findCatByNameReturns404WhenNotFound() throws Exception{
        //when
        when(catService.findCatByName("Mietek")).thenThrow(new CatNotFoundException());
        //then
        mockMvc.perform(get("/cat/findByName/Mietek")).andExpect(status().isNotFound());
    }
    @Test
    public void deleteReturns200WhenCatFound() throws Exception {
        mockMvc.perform(delete("/cat/deleteById/1")).andExpect(status().isOk());
    }
    @Test
    public void deleteReturns404WhenCatNotFound() throws Exception {
        //when
        when(catService.deleteById(4L)).thenThrow(new CatNotFoundException());
        //then
        mockMvc.perform(delete("/cat/deleteById/4")).andExpect(status().isNotFound());
    }
}
