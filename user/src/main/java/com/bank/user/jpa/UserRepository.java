package com.bank.user.jpa;

import com.bank.exceptions.EmailNotFoundExceptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    default User findByEmailOrThrowWhenEmailNotExists(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundExceptions("Email: " + email + " not found"));
    }
}
