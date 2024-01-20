package com.example.demo.service;

import com.example.demo.exceptions.AgeIsNegativeException;
import com.example.demo.exceptions.CatAlreadyExistsException;
import com.example.demo.exceptions.CatNotFoundException;
import com.example.demo.model.Cat;
import com.example.demo.repository.CatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {
    @Mock
    private CatRepository catRepository;
    @InjectMocks
    private CatService catService;
    private AutoCloseable openMocks;
    private Cat cat = new Cat("Kalinka", 2, "black");
    private Cat cat2 = new Cat("MIALEK", 5, "orange");
    private Cat cat3 = new Cat("MILKA", 4, "gray");
    private List<Cat> catsInDb = Arrays.asList(cat, cat2, cat3);
    @Captor
    ArgumentCaptor<Cat> argumentCaptor;

    @BeforeEach
    public void init(){
       openMocks = MockitoAnnotations.openMocks(this);
       cat.setId(1L);
    }
    @AfterEach
    public void takeDown() throws Exception {
        openMocks.close();
    }
    @Test
    public void shouldFindByName(){
        when(catRepository.findByName(cat.getName())).thenReturn(cat);
        Cat result = catService.findCatByName(cat.getName());
        assertEquals(cat, result);
    }
    @Test
    public void shouldSave(){
        Cat savedCat = cat;
        argumentCaptor = ArgumentCaptor.forClass(Cat.class);
        when(catRepository.save(argumentCaptor.capture())).thenReturn(savedCat);
        catService.saveCat(savedCat);
        Mockito.verify(catRepository,times(1)).save(savedCat);
        assertEquals(savedCat,argumentCaptor.getValue());
    }
    @Test
    public void shouldNotSaveThrowCatAlreadyExists(){
        String tempName = this.cat.getName();
        when(catRepository.findByName(tempName)).thenReturn(this.cat);
        assertThrows(CatAlreadyExistsException.class, ()->{
           catService.saveCat(this.cat);
        });
    }
    @Test
    public void  shouldNotSaveThrowAgeIsNegative(){
        Cat impossibleCat = new Cat("Milek", -4, "black");
        assertThrows(AgeIsNegativeException.class, ()->{
            catService.saveCat(impossibleCat);
        });
    }
    @Test
    public void shouldFindById(){
        Optional<Cat> cat = Optional.ofNullable(this.cat);
        when(catRepository.findById(this.cat.getId())).thenReturn(Optional.ofNullable(this.cat));
        Optional<Cat> result = catService.findById(this.cat.getId());
        assertEquals(cat, result);
    }
    @Test
    public void shouldNotFindByIdThrowCatNotFound(){
        long tempId = 8L;
        when(catRepository.findById(tempId)).thenReturn(Optional.empty());
        assertThrows(CatNotFoundException.class, ()-> {
            catService.findById(tempId);
        });
    }
    @Test
    public void shouldFindAll(){
        when(catRepository.findAll()).thenReturn(catsInDb);
        assertEquals(catService.findAll(), catsInDb);
    }
    @Test
    public void shouldUpdateCat(){
        Cat catfromDb = cat;
        String newName = "Bambik";
        int newAge = 7;
        Cat updateInfoCat = new Cat(newName, newAge, "white");
        updateInfoCat.setId(catfromDb.getId());
        argumentCaptor = ArgumentCaptor.forClass(Cat.class);
        when(catRepository.getCatById(updateInfoCat.getId())).thenReturn(catfromDb);
        when(catRepository.save(argumentCaptor.capture())).thenReturn(catfromDb);
        catService.updateCat(updateInfoCat);
        assertEquals(catfromDb.getId(), argumentCaptor.getValue().getId());
    }
    @Test
    public void shouldDeleteCat(){
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        var result = catService.deleteById(cat.getId());
        verify(catRepository, times(2)).findById(cat.getId());
        assertEquals(cat.getId(), result);
    }
    @Test
    public void shouldNotDeleteCat(){
        when(catRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(CatNotFoundException.class, ()->{
            catService.deleteById(3L);
        });
    }
    @Test
    public void shouldFilterByName(){
        when(catRepository.findAll()).thenReturn(catsInDb);
        List<Cat> correctList = Arrays.asList(cat2, cat3);
        assertEquals(catService.filterByName("MI"), correctList);
    }
}