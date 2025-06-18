package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndBirthDate(String name, String birthDate);
    Optional<User> findByEmailAndNameAndBirthDate(String email, String name, String birthDate);
    boolean existsByUserid(String userid);
}
