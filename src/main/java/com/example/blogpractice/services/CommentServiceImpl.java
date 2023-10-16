package com.example.blogpractice.services;

import com.example.blogpractice.respository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl {

    @Autowired
    private CommentRepository commentRepository;

}
