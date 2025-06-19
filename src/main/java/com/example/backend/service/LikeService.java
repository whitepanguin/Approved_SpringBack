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

    /** 좋아요한 게시글 목록 가져오기 */
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

    /** 좋아요 토글 (기존 로직 유지) */
    public String toggleLike(String postId, String userid) {
        Post post = postRepo.findById(postId).orElseThrow();

        return likeRepo.findByPostIdAndUserid(post, userid)
                .map(existing -> {
                    likeRepo.delete(existing);
                    post.setLikes(post.getLikes() - 1);
                    postRepo.save(post);
                    return "unliked";
                })
                .orElseGet(() -> {
                    Like like = new Like(post, userid);
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
