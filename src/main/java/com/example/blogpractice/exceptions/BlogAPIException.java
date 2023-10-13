package com.example.blogpractice.exceptions;


import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public BlogAPIException(HttpStatus status, String message) {
        super();
        this.httpStatus = status;
        this.message = message;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
