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
        Cat cat = new Cat("Milek", 2, "black");
        cat.setId(1L);
        when(catService.findById(1L)).thenReturn(Optional.of(cat));
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/find/1"))
                .andExpect(jsonPath("$.name").value("Milek"))
                .andExpect(jsonPath("$.age").value(2))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colour").value("black"))
                .andExpect(status().isOk());
    }
    @Test
    public void getByIdReturns404WhenCatNotFound() throws Exception {
        when(catService.findById(4L)).thenThrow(new CatNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/find/4")).andExpect(status().isNotFound());
    }
    @Test
    public void addReturns200WhenCatDoesntExistYet() throws Exception {
        Cat cat = new Cat("Milek", 2, "black");
        cat.setId(1L);
        when(catService.saveCat(any())).thenReturn(cat);
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": 2, \"colour\": \"black\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void addReturns400WhenCatAlreadyExists() throws Exception {
        when(catService.saveCat(any())).thenThrow(new CatAlreadyExistsException());
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": 2, \"colour\": \"black\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void addReturns400WhenAgeIsNegative() throws Exception {
        Cat impossibleCat = new Cat("Milek", -3, "black");
        when(catService.saveCat(any())).thenThrow(new AgeIsNegativeException());
        mockMvc.perform(post("/cat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Milek\", \"age\": -3,  \"colour\": \"black\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns200WhenCatIsUpdated() throws Exception {
        Cat catfromDb = new Cat("Miecio", 7, "white");
        catfromDb.setId(3L);
        when(catService.updateCat(any())).thenReturn(catfromDb);
        mockMvc.perform(put("/cat/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Miecio\", \"age\": 7, \"id\": 1,  \"colour\": \"black\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void updateReturns400WhenIncompleteData() throws Exception {
        when(catService.updateCat(any())).thenThrow(new IncompleteDataException("Please provide necessary data"));
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": 7, \"id\": 1,  \"colour\": \"black\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns400WhenAgeIsNegative() throws Exception {
        when(catService.updateCat(any())).thenThrow(new AgeIsNegativeException());
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": -7, \"id\": 1,  \"colour\": \"black\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updateReturns404WhenCatNotFound() throws Exception {
        when(catService.updateCat(any())).thenThrow(new CatNotFoundException());
        mockMvc.perform(put("/cat/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Miecio\", \"age\": -7, \"id\": 1,  \"colour\": \"black\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void findCatByNameReturns200WhenOK() throws Exception {
        when(catService.findCatByName("Mietek")).thenReturn(new Cat("Mietek", 2, "black"));
        mockMvc.perform(get("/cat/findByName/Mietek")).andExpect(status().isOk());
    }
    @Test
    public void findCatByNameReturns404WhenNotFound() throws Exception{
        when(catService.findCatByName("Mietek")).thenThrow(new CatNotFoundException());
        mockMvc.perform(get("/cat/findByName/Mietek")).andExpect(status().isNotFound());
    }
    @Test
    public void deleteReturns200WhenCatFound() throws Exception {
        mockMvc.perform(delete("/cat/deleteById/1")).andExpect(status().isOk());
    }
    @Test
    public void deleteReturns404WhenCatNotFound() throws Exception {
        when(catService.deleteById(4L)).thenThrow(new CatNotFoundException());
        mockMvc.perform(delete("/cat/deleteById/4")).andExpect(status().isNotFound());
    }
}
