package org.example.backend.service;

import org.example.backend.model.Like;
import org.example.backend.model.Post;
import org.example.backend.repository.LikeRepository;
import org.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LikeService {

    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    /** 좋아요한 게시글 목록 - userid 기준 */
    public List<Post> getLikedPostsByUser(String userid) {
        List<Like> likes = likeRepo.findByUserid(userid);
        return likes.stream()
                .map(Like::getPostId)
                .filter(Objects::nonNull)
                .map(Post::getId)
                .map(id -> postRepo.findById(id).orElse(null))
                .filter(Objects::nonNull)
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

    /** 좋아요 토글: email 우선, 없으면 userid */
    public String toggleLike(String postId, String userid, String email) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음: " + postId));

        return likeRepo.findByPostIdAndEmail(post, email)
                .or(() -> likeRepo.findByPostIdAndUserid(post, userid))
                .map(existing -> {
                    likeRepo.delete(existing);
                    post.setLikes(post.getLikes() - 1);
                    postRepo.save(post);
                    return "unliked";
                })
                .orElseGet(() -> {
                    Like like = new Like(post, userid, email);  // ✅ userid 포함
                    likeRepo.save(like);
                    post.setLikes(post.getLikes() + 1);
                    postRepo.save(post);
                    return "liked";
                });
    }

    /** Like 원본 데이터 목록 */
    public List<Like> getLikesByUser(String userid) {
        return likeRepo.findByUserid(userid);
    }
}
