package com.example.demo.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class CatFrontExceptionHandler
extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(HttpClientErrorException clientErrorException, RedirectAttributes redirectAttributes){
        String message = clientErrorException.getMessage().replaceFirst("^.[^\"]*", "");
        redirectAttributes.addAttribute("error",message);
        return ("redirect:/viewAll");
    }
}
