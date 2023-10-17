package com.example.blogpractice.services;

import com.example.blogpractice.exceptions.AppException;
import com.example.blogpractice.modals.Comment;
import com.example.blogpractice.modals.Post;
import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.CommentRequest;
import com.example.blogpractice.payloads.response.PagedResponse;
import com.example.blogpractice.respository.CommentRepository;
import com.example.blogpractice.respository.PostRepository;
import com.example.blogpractice.respository.RoleRepository;
import com.example.blogpractice.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Comment createComment(String id, CommentRequest commentRequest) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException("post not found!")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () ->  new AppException("User not Found!")
        );
       Comment comment =  new Comment();
       comment.setBody(commentRequest.getBody());
       comment.setPost(post);
       comment.setUser(user);
       comment.setName(user.getUsername());
       comment.setEmail(user.getEmail());

       return commentRepository.save(comment);
    }

    public Comment updateComment(String postId, String commentId, CommentRequest commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException("post not found!")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new AppException("Comment not found")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
//                () ->  new AppException("User not Found!")
//        );

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new AppException("Comment does not belong to this post!");
        }

        if (
                !comment.getUser().getEmail().equals(authentication.getName()) ||
                        !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ) {
            throw new AppException("You don't have permission to to update this post");
        }
        comment.setBody(commentRequest.getBody());
        return commentRepository.save(comment);
    }

    public String deleteComment(String postId, String commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException("Post not found")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new AppException("Comment not found")
        );

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new AppException("Comment does not belong to this post");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!comment.getUser().getEmail().equals(authentication.getName()) ||
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ) {
            throw new AppException("You don't have permission to delete this comment");
        }

        commentRepository.deleteById(comment.getId());
        return "Deleted successfully!";
    }

    public PagedResponse<Comment> getAllCommentByPostId(String postId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        PagedResponse<Comment> pagedResponse = new PagedResponse<>();
        pagedResponse.setLastPage(comments.isLast());
        pagedResponse.setPageNumber(comments.getNumber());
        pagedResponse.setPageSize(comments.getSize());
        pagedResponse.setContent(comments.getContent());
        pagedResponse.setTotalPages(comments.getTotalPages());
        pagedResponse.setTotalElements(comments.getTotalElements());
        return pagedResponse;
    }

}
