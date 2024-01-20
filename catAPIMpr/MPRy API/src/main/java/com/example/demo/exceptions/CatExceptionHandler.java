package com.example.demo.exceptions;

import com.example.demo.model.Cat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CatExceptionHandler
extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AgeIsNegativeException.class)
    protected ResponseEntity<Cat> handleAge(AgeIsNegativeException ex, WebRequest request){
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CatNotFoundException.class)
    protected ResponseEntity<Cat> handleNotFound(CatNotFoundException ex, WebRequest request){
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IncompleteDataException.class)
    protected ResponseEntity<Cat> handleNotFound(IncompleteDataException ex, WebRequest request){
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CatAlreadyExistsException.class)
    protected ResponseEntity<Cat> handleAlreadyExists(CatAlreadyExistsException ex, WebRequest request){
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
