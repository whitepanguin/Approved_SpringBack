package com.example.backend.repository;

import com.example.backend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    // 🔧 postId는 String이므로 "_Id" 제거
    List<Comment> findByPostIdOrderByCreatedAtAsc(String postId);

    List<Comment> findByUseridOrderByCreatedAtDesc(String userid); // 예전 방식, 나중에 제거 가능
    List<Comment> findByEmailOrderByCreatedAtDesc(String email);   // ✅ 이메일 기반 댓글 조회

    long countByEmail(String email); // 통계용 (댓글 수)
}
