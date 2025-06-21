package com.example.backend.repository;

import com.example.backend.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ResultRepository extends MongoRepository<Result, String>{
    List<Result> findByEmail(String email);
}
