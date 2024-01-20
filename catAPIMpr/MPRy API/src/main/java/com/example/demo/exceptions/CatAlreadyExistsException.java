package com.example.demo.exceptions;

public class CatAlreadyExistsException extends RuntimeException{
    public CatAlreadyExistsException(){
        super("Cat like this or similar to this one already exists");
    }
}
