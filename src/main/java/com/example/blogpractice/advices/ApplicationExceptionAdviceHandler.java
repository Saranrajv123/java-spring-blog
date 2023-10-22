package com.example.blogpractice.advices;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionAdviceHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AccessDeniedException.class, MethodArgumentNotValidException.class})
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public String InsufficientAuthenticationExceptionHandler(InsufficientAuthenticationException exception) {
        Map<String, String> errorMap = new HashMap<>();
        return errorMap.put("Authentication", exception.getMessage());
    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AccessDeniedException.class)
//    public String accessDeniedExceptionExceptionHandler(AccessDeniedException exception) {
//        Map<String, String> errorMap = new HashMap<>();
//        return errorMap.put("Authentication", exception.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public String genericExceptionHandler(Exception exception) {
//        Map<String, String> errorMap = new HashMap<>();
//        return errorMap.put("Authentication", exception.getMessage());
//    }
}
