package com.example.backend.repository;

import com.example.backend.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ResultRepository extends MongoRepository<Result, String> {

    @Query("{ 'email': ?0 }")
    List<Result> findByEmail(String email);

    @Query("{ 'email': ?0 }")
    List<Result> findByEmailOrderByCreatedAtDesc(String email);
}