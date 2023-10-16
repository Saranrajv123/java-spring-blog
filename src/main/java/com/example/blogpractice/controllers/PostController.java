package com.example.blogpractice.controllers;

import com.example.blogpractice.modals.Post;
import com.example.blogpractice.payloads.PostRequestDTO;
import com.example.blogpractice.payloads.response.PagedResponse;
import com.example.blogpractice.services.PostServiceImpl;
import com.example.blogpractice.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog/post")
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @PostMapping("/public/create-post")
    public ResponseEntity<PostRequestDTO> createPostController(@RequestBody PostRequestDTO postRequestDTO) {
        return new ResponseEntity<>(
                postService.createPost(postRequestDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/admin/get-posts")
    public ResponseEntity<PagedResponse<Post>> getAllPostsController(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize
    ) {
        return new ResponseEntity<>(
                postService.getAllPosts(pageNumber, pageSize),
                HttpStatus.OK
        );
    }

    @PutMapping("/public/update-post/{id}")
    public ResponseEntity<Post> updatePostController(@PathVariable String id, @RequestBody PostRequestDTO postRequestDTO) {
        return new ResponseEntity<>(
                postService.updatePost(id, postRequestDTO),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Post> getPostController(@PathVariable String id) {
        return new ResponseEntity<>(
          postService.getPost(id),
          HttpStatus.OK
        );
    }
}
