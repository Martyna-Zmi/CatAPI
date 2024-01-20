package com.example.demo.exceptions;

public class AgeIsNegativeException extends RuntimeException{
    public AgeIsNegativeException(){
        super("Age cannot be less than 0");
    }
}
