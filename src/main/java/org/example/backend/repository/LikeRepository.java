package org.example.backend.repository;

import org.example.backend.model.Like;
import org.example.backend.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    Optional<Like> findByPostIdAndUserid(Post postId, String userid);
    Optional<Like> findByPostIdAndEmail(Post postId, String email);

    List<Like> findByUserid(String userid);
    List<Like> findByEmail(String email);
    // 게시글 Id로 좋아요 수 조회
    int countByPostId_Id(String postId);



}
