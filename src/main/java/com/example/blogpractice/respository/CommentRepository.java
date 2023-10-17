package com.example.blogpractice.respository;

import com.example.blogpractice.modals.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Page<Comment> findByPostId(String id, Pageable pageable);

}
