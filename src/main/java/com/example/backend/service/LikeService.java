package com.example.backend.service;

import com.example.backend.model.Like;
import com.example.backend.model.Post;
import com.example.backend.repository.LikeRepository;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepo;
    private final PostRepository postRepo;

    public LikeService(LikeRepository likeRepo, PostRepository postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    public String toggleLike(String postId, String userid) {
        Post post = postRepo.findById(postId).orElseThrow();
        return likeRepo.findByPostIdAndUserid(post, userid).map(existing -> {
            likeRepo.delete(existing);
            post.setLikes(post.getLikes() - 1);
            postRepo.save(post);
            return "unliked";
        }).orElseGet(() -> {
            Like like = new Like(post, userid);
            likeRepo.save(like);
            post.setLikes(post.getLikes() + 1);
            postRepo.save(post);
            return "liked";
        });
    }

    public List<Like> getLikesByUser(String userid) {
        return likeRepo.findByUserid(userid);
    }
}
