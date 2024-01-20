package com.example.demo.service;

import com.example.demo.exceptions.AgeIsNegativeException;
import com.example.demo.exceptions.CatAlreadyExistsException;
import com.example.demo.exceptions.CatNotFoundException;
import com.example.demo.exceptions.IncompleteDataException;
import com.example.demo.model.Cat;
import com.example.demo.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CatService {
     CatRepository catRepository;
    @Autowired
    public CatService(CatRepository catRepository){
        this.catRepository=catRepository;
        this.catRepository.save(new Cat("MILEK", 2, "czarny"));
    }
    public Cat findCatByName(String name){
        return this.catRepository.findByName(name);
    }
    public Optional<Cat> findById(Long id){
        Optional<Cat> cat = catRepository.findById(id);
        if(cat.isEmpty()) throw new CatNotFoundException();
        else return catRepository.findById(id);
    }
    public Cat saveCat(Cat cat){
        if(catRepository.findByName(cat.getName())!=null){
            throw new CatAlreadyExistsException();
        }
        if(cat.getAge()<0){
            throw new AgeIsNegativeException();
        }
        else if(cat.getName().trim().length()==0 || cat.getColour().trim().length()==0){
            throw new IncompleteDataException("Please provide data to edit! (name/age/colour)");
        }
        else{
            cat.setName(cat.getName().toUpperCase());
            System.out.println("Dodano podanego kotka: ");
            System.out.println(cat.makeNoise());
            return this.catRepository.save(cat);
        }
    }
    public Cat updateCat(Cat cat) {
        Cat catFromDb = catRepository.getCatById(cat.getId());
        System.out.println(cat.getName().trim().length());
        if(catFromDb==null){
            throw new CatNotFoundException();
        }
        else if(cat.getAge()==null || cat.getName().trim().length()==0 || cat.getColour().trim().length()==0){
            throw new IncompleteDataException("Please provide data to edit! (name/age/colour)");
        }
        else{
            if(catFromDb.getAge()!=null){
                if(cat.getAge()>=0) catFromDb.setAge(cat.getAge());
                else throw new AgeIsNegativeException();
            }
            System.out.println("Aktualizacja kota o id: "+cat.getId());
            System.out.println("Kotek przed aktualizacją: "+catFromDb.makeNoise());
            if(catFromDb.getName()!=null) catFromDb.setName(cat.getName().toUpperCase());
            System.out.println("Zaktualizowano kota");
            System.out.println("Kotek po aktualizacji: "+cat.makeNoise());
            return catRepository.save(catFromDb);
        }
    }
    public List<Cat> findAll(){
        return (List<Cat>) this.catRepository.findAll();
    }
    public long deleteById(Long id){
        if(findById(id).isPresent()){
            catRepository.deleteById(id);
            System.out.println("Usunięto koda o Id: "+id);
        }
        else {
            throw new CatNotFoundException();
        }
        return id;
    }
}
