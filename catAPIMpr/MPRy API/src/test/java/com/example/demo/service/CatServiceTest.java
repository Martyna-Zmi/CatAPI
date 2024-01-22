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
    @Captor
    ArgumentCaptor<Cat> argumentCaptor;

    @BeforeEach
    public void init(){
       openMocks = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void takeDown() throws Exception {
        openMocks.close();
    }
    @Test
    public void shouldFindByName(){
        //given
        Cat cat = new Cat("Kalinka", 2, "czarny");
        //when
        when(catRepository.findByName(cat.getName())).thenReturn(cat);
        Cat result = catService.findCatByName(cat.getName());
        //then
        assertEquals(cat, result);
    }
    @Test
    public void shouldSave(){
        //given
        Cat savedCat = new Cat("Kalinka", 2, "czarny");
        argumentCaptor = ArgumentCaptor.forClass(Cat.class);
        //when
        when(catRepository.save(argumentCaptor.capture())).thenReturn(savedCat);
        catService.saveCat(savedCat);
        //then
        Mockito.verify(catRepository,times(1)).save(savedCat);
        assertEquals(savedCat,argumentCaptor.getValue());
    }
    @Test
    public void shouldNotSaveThrowCatAlreadyExists(){
        //given
        Cat cat = new Cat("Kalinka", 2, "czarny");
        cat.setId(1L);
        //when
        when(catRepository.findById(1L)).thenReturn(Optional.of(cat));
        //then
        assertThrows(CatAlreadyExistsException.class, ()->{
           catService.saveCat(cat);
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
        //given
        Cat cat = new Cat("Kalinka", 2, "czarny");
        cat.setId(1L);
        //when
        when(catRepository.findById(cat.getId())).thenReturn(Optional.ofNullable(cat));
        Cat result = catService.findById(cat.getId());
        //then
        assertEquals(cat, result);
    }
    @Test
    public void shouldNotFindByIdThrowCatNotFound(){
        //given
        long id = 8L;
        //when
        when(catRepository.findById(id)).thenReturn(Optional.empty());
        //then
        assertThrows(CatNotFoundException.class, ()-> {
            catService.findById(id);
        });
    }
    @Test
    public void shouldFindAll(){
        //given
        Cat catA = new Cat("Kalinka", 2, "czarny");
        Cat catB = new Cat("MIALEK", 5, "rudy");
        List<Cat> catsInDb = Arrays.asList(catA, catB);
        //when
        when(catRepository.findAll()).thenReturn(catsInDb);
        var foundCats = catService.findAll();
        //then
        assertEquals(foundCats, catsInDb);
    }
    @Test
    public void shouldUpdateCat(){
        //given
        Cat catFromDb = new Cat("Kalinka", 2, "czarny");
        catFromDb.setId(1L);
        Cat updateData = new Cat("Buba", 3, "czarny");
        argumentCaptor = ArgumentCaptor.forClass(Cat.class);
        //when
        when(catRepository.findById(updateData.getId())).thenReturn(Optional.of(catFromDb));
        when(catRepository.save(argumentCaptor.capture())).thenReturn(catFromDb);
        catService.updateCat(updateData);
        //then
        assertEquals(catFromDb.getId(), argumentCaptor.getValue().getId());
    }
    @Test
    public void shouldDeleteCat(){
        //given
        Cat cat = new Cat("Kalinka", 2, "czarny");
        cat.setId(1L);
        //when
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        var result = catService.deleteById(cat.getId());
        //then
        verify(catRepository, times(1)).findById(cat.getId());
        assertEquals(cat.getId(), result);
    }
    @Test
    public void shouldNotDeleteCat(){
        //when
        when(catRepository.findById(3L)).thenReturn(Optional.empty());
        //then
        assertThrows(CatNotFoundException.class, ()->{
            catService.deleteById(3L);
        });
    }
}