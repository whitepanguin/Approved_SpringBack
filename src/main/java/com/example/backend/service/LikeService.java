package com.example.backend.service;

import com.example.backend.model.Like;
import com.example.backend.model.Post;
import com.example.backend.repository.LikeRepository;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    /** 좋아요한 게시글 목록 -userid 기준 */
    public List<Post> getLikedPostsByUser(String userid) {
        List<Like> likes = likeRepo.findByUserid(userid);

        return likes.stream()
                .map(Like::getPostId)                       // Like → Post (연관 객체)
                .filter(Objects::nonNull)                  // ✅ null 방지
                .map(Post::getId)                          // ✅ Post → ID(String)
                .map(id -> postRepo.findById(id).orElse(null)) // ID로 실제 Post 조회
                .filter(Objects::nonNull)                  // ✅ 조회 실패한 것 제거
                .toList();
    }

    /** 좋아요한 게시글 목록 - email 기준 */
    public List<Post> getLikedPostsByEmail(String email) {
        List<Like> likes = likeRepo.findByEmail(email);
        return likes.stream()
                .map(Like::getPostId)
                .filter(Objects::nonNull)
                .map(Post::getId)
                .map(id -> postRepo.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    /** 좋아요 토글 userid, email 혼용(email우선) */
    public String toggleLike(String postId, String userid, String email) {
        Post post = postRepo.findById(postId).orElseThrow();

        // 먼저 email로 찾고, 없으면 userid로 한 번 더 찾는다
        return likeRepo.findByPostIdAndEmail(post, email)
                .or(() -> likeRepo.findByPostIdAndUserid(post, userid))
                .map(existing -> {          // 이미 있으니 해제
                    likeRepo.delete(existing);
                    post.setLikes(post.getLikes() - 1);
                    postRepo.save(post);
                    return "unliked";
                })
                .orElseGet(() -> {          // 둘 다 없으면 새로 저장
                    Like like = new Like(post, userid, email);
                    likeRepo.save(like);
                    post.setLikes(post.getLikes() + 1);
                    postRepo.save(post);
                    return "liked";
                });
    }

    /** Like 원본 데이터 목록 (필요 시 사용) */
    public List<Like> getLikesByUser(String userid) {
        return likeRepo.findByUserid(userid);
    }
}
