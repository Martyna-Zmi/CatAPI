package com.example.demo.controller;

import com.example.demo.exceptions.CatFrontExceptionHandler;
import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CatService catService;
    @InjectMocks
    private CatControllerThyme catController;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(new CatFrontExceptionHandler(), catController).build();
    }
    @Test
    public void viewAllTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        Cat cat2 = new Cat("Bartus", 3, "czarny");
        List<Cat> catsInDb = new ArrayList<>();
        catsInDb.add(cat);
        catsInDb.add(cat2);

        when(catService.findAll()).thenReturn(catsInDb);
        String viewName = catController.viewAll(model, redirectAttributes);
        verify(catService).findAll();
        assertEquals("viewAll", viewName);
        verify(model).addAttribute("cats", catsInDb);
    }
    @Test
    public void addViewTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        String viewName = catController.addView(model);
        assertEquals("addCat", viewName);
    }
    @Test
    public void addNewTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        String viewName = catController.addCat(cat, model);
        verify(catService).saveCat(cat);
        assertEquals("redirect:/viewAll", viewName);
    }
    @Test
    public void getDeleteCatTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        when(catService.findById(1L)).thenReturn(cat);
        String viewName = catController.getDeleteCat(model, 1L);
        assertEquals("deleteView", viewName);
        verify(model).addAttribute("cat", cat);
    }
    @Test
    public void deleteCatTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        String viewName = catController.deleteCat(model, 2);
        verify(catService).deleteById(2L);
        assertEquals("redirect:/viewAll", viewName);
    }
    @Test
    public void getUpdateCatTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        when(catService.findById(1L)).thenReturn(cat);
        String viewName = catController.getUpdateCat(model, 1L);
        assertEquals("editView", viewName);
        verify(model).addAttribute("cat", cat);
    }
    @Test
    public void editByIdTest(){
        Cat cat = new Cat("Milek", 2, "rudy");
        String viewName = catController.editById(cat, model);
        verify(catService).deleteById(2L);
        assertEquals("redirect:/viewAll", viewName);
    }
}
