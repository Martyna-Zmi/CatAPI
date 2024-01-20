package com.example.demo.exceptions;

public class CatNotFoundException extends RuntimeException{
    public CatNotFoundException(){
        super("This cat doesn't exist");
    }
}
