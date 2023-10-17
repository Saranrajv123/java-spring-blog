package com.example.blogpractice.controllers;

import com.example.blogpractice.modals.Comment;
import com.example.blogpractice.payloads.CommentRequest;
import com.example.blogpractice.payloads.response.PagedResponse;
import com.example.blogpractice.respository.CommentRepository;
import com.example.blogpractice.services.CommentServiceImpl;
import com.example.blogpractice.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping("/public/{postId}/create-comment")
    public ResponseEntity<?> createCommentController(@PathVariable String postId, @RequestBody @Valid CommentRequest commentRequest) {
        return new ResponseEntity<>(
                commentService.createComment(postId, commentRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/public/{postId}/update-comment/{commentId}")
    public ResponseEntity<Comment> updateCommentController(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        return new ResponseEntity<>(
                commentService.updateComment(postId, commentId, commentRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/public/{postId}/delete-comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String postId, @PathVariable String commentId) {
        return new ResponseEntity<>(
                commentService.deleteComment(postId, commentId),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/{postId}/comments")
    public ResponseEntity<PagedResponse<Comment>> getAllCommentByPostIdController(
            @PathVariable String postId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        return new ResponseEntity<>(
                commentService.getAllCommentByPostId(postId, pageNumber, pageSize),
                HttpStatus.OK
        );
    }
}
