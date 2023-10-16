package com.example.blogpractice.controllers;

import com.example.blogpractice.payloads.CommentRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/comment")
public class CommentController {

    public ResponseEntity<?> createPostController(@PathVariable String id, @RequestBody @Valid CommentRequest commentRequest) {


    }
}
