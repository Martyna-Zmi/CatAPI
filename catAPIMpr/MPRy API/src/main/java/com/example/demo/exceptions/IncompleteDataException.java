package com.example.demo.exceptions;

public class IncompleteDataException extends RuntimeException{
    public IncompleteDataException(String message){
        super(message);
    }
}
