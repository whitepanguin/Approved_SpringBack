package org.example.backend.repository;

import org.example.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndBirthDate(String name, String birthDate);
    Optional<User> findByEmailAndNameAndBirthDate(String email, String name, String birthDate);
    boolean existsByUserid(String userid);
    List<User> findAllByOrderByCreatedAtDesc();

}
