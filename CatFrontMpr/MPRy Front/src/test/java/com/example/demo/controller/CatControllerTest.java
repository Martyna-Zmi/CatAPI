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
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        Cat cat2 = new Cat("Bartus", 3, "czarny");
        List<Cat> catsInDb = new ArrayList<>();
        catsInDb.add(cat);
        catsInDb.add(cat2);
        //when
        when(catService.findAll()).thenReturn(catsInDb);
        String viewName = catController.viewAll(model, redirectAttributes);
        //then
        verify(catService).findAll();
        assertEquals("viewAll", viewName);
        verify(model).addAttribute("cats", catsInDb);
    }
    @Test
    public void addViewTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        String viewName = catController.addView(model);
        //then
        assertEquals("addCat", viewName);
    }
    @Test
    public void addNewTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        String viewName = catController.addCat(cat, model);
        //then
        verify(catService).saveCat(cat);
        assertEquals("redirect:/viewAll", viewName);
    }
    @Test
    public void getDeleteCatTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        when(catService.findById(1L)).thenReturn(cat);
        String viewName = catController.getDeleteCat(model, 1L);
        //then
        assertEquals("deleteView", viewName);
        verify(model).addAttribute("cat", cat);
    }
    @Test
    public void deleteCatTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        String viewName = catController.deleteCat(model, 2);
        //then
        verify(catService).deleteById(2L);
        assertEquals("redirect:/viewAll", viewName);
    }
    @Test
    public void getUpdateCatTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        when(catService.findById(1L)).thenReturn(cat);
        String viewName = catController.getUpdateCat(model, 1L);
        //then
        assertEquals("editView", viewName);
        verify(model).addAttribute("cat", cat);
    }
    @Test
    public void editByIdTest(){
        //given
        Cat cat = new Cat("Milek", 2, "rudy");
        //when
        String viewName = catController.editById(cat, model);
        //then
        verify(catService).deleteById(2L);
        assertEquals("redirect:/viewAll", viewName);
    }
}
