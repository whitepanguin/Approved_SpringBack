package com.example.backend.repository;

import com.example.backend.model.Like;
import com.example.backend.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String> {
    Optional<Like> findByPostIdAndUserid(Post postId, String userid);
    List<Like> findByUserid(String userid);
    // 게시글 Id로 좋아요 수 조회
    int countByPostId_Id(String postId);

}
