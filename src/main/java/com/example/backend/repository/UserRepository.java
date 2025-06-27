package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserid(String userid);
    Optional<User> findByNameAndBirthDate(String name, String birthDate);
    Optional<User> findByEmailAndNameAndBirthDate(String email, String name, String birthDate);
    boolean existsByUserid(String userid);
    List<User> findAllByOrderByCreatedAtDesc();

}
