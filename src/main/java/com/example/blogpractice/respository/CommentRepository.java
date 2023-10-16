package com.example.blogpractice.respository;

import com.example.blogpractice.modals.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {

}
