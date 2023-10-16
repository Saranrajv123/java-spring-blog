package com.example.blogpractice.services;

import com.example.blogpractice.exceptions.AppException;
import com.example.blogpractice.modals.Post;
import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.PostRequestDTO;
import com.example.blogpractice.payloads.response.PagedResponse;
import com.example.blogpractice.respository.PostRepository;
import com.example.blogpractice.respository.UserRepository;
import com.example.blogpractice.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public PostRequestDTO createPost(PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new AppException("User not found")
        );

        postRequestDTO.setUser(user);
        Post post = modelMapper.map(postRequestDTO, Post.class);
        post = postRepository.save(post);
        return modelMapper.map(post, PostRequestDTO.class);
    }

    public PagedResponse<Post> getAllPosts(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, AppConstants.CREATED_AT);
        Page<Post> pagesPost = postRepository.findAll(pageable);
        List<Post> postsContent = pagesPost.getNumberOfElements() == 0 ? Collections.emptyList() : pagesPost.getContent();
        PagedResponse<Post> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(postsContent);
        pagedResponse.setPageNumber(pagesPost.getNumber());
        pagedResponse.setLastPage(pagesPost.isLast());
        pagedResponse.setTotalPages(pagesPost.getTotalPages());
        pagedResponse.setPageSize(pagesPost.getSize());
        pagedResponse.setTotalElements(pagesPost.getTotalElements());
        return pagedResponse;
    }

    public Post updatePost(String id, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException("Post not found!")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!post.getUser().getEmail().equals(authentication.getName()) ||
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ) {
           throw new AppException("You don't have permission to edit this post");
        }

//        Post postFromMapper = modelMapper.map(postRequestDTO, Post.class);
        post.setUser(post.getUser());
        post.setTitle(postRequestDTO.getTitle());
        post.setBody(postRequestDTO.getBody());
        return postRepository.save(post);
    }

    public Post getPost(String id) {
        return postRepository.findById(id).orElseThrow(
                () -> new AppException("Post not found")
        );
    }
}
