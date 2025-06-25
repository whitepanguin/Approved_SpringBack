package org.example.backend.repository;

import org.example.backend.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUseridOrderByCreatedAtDesc(String userid);
    List<Post> findByEmailOrderByCreatedAtDesc(String email);
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
    long countByEmail(String email);

    List<Post> findByEmail(String email);

}
