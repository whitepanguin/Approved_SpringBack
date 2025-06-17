package com.example.backend.repository;

import com.example.backend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId_IdOrderByCreatedAtAsc(String postId);
    List<Comment> findByUseridOrderByCreatedAtDesc(String userid);
}
