package com.example.backend.repository;

import com.example.backend.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ResultRepository extends MongoRepository<Result, String>{
}
