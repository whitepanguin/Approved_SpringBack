package org.example.backend.repository;

import org.example.backend.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, String>{
    List<Result> findByEmail(String email);

    List<Result> findByEmailOrderByCreatedAtDesc(String email);
}
