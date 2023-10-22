package com.example.blogpractice.respository;

import com.example.blogpractice.modals.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, String> {
    Confirmation findByToken(String token);
}
