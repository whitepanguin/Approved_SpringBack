package com.example.backend.repository;

import com.example.backend.model.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUseridOrderByCreatedAtDesc(String userid);
    long countByUserid(String userid);
    List<Post> findByCategory(String category);
    List<Post> findByUserid(String userid);

    List<Post> findByCategoryOrderByCommentsDesc(String category);
    List<Post> findByCategoryOrderByCreatedAtDesc(String category);
    List<Post> findAllByOrderByCommentsDesc();
    List<Post> findAllByOrderByCreatedAtDesc();

    // 신고된 게시글 리스트
    List<Post> findByIsReportedTrue();
    // 신고된 게시글 개수
    long countByIsReportedTrue();
}
