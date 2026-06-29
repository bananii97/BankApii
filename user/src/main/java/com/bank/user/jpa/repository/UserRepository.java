package com.bank.user.jpa.repository;

import com.bank.exceptions.EmailNotFoundExceptions;
import com.bank.exceptions.UserNotFoundException;
import com.bank.user.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    default User findByEmailOrThrowWhenEmailNotExists(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundExceptions("Email: " + email + " not found",
                        HttpStatus.NOT_FOUND));
    }

    default User findByIdOrThrowWhenIdNotExists(Long id) {
        return findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found",
                        HttpStatus.BAD_REQUEST));
    }
}
