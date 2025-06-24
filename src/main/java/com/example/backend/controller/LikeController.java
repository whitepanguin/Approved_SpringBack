package com.example.backend.controller;

import com.example.backend.model.Like;
import com.example.backend.model.Post;
import com.example.backend.service.LikeService;
import com.example.backend.repository.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostRepository postRepo;

    public LikeController(LikeService likeService, PostRepository postRepo) {
        this.likeService = likeService;
        this.postRepo = postRepo;
    }

    @PatchMapping("/{postId}")
    public Map<String, Object> toggleLike(@PathVariable String postId,
                                          @RequestParam String email,
                                          @RequestParam String userid) {
        String result = likeService.toggleLike(postId, userid, email);
        Post post = postRepo.findById(postId).orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("liked", result.equals("liked"));
        response.put("likes", post.getLikes());
        return response;
    }

    @GetMapping("/user/{userid}")
    public List<Like> getLikesByUser(@PathVariable String userid) {
        return likeService.getLikesByUser(userid);
    }

    @GetMapping("/user/{userid}/posts")
    public List<Post> getLikedPostsByUser(@PathVariable String userid) {
        return likeService.getLikedPostsByUser(userid);
    }

    @GetMapping("/email/{email}/posts")
    public List<Post> getLikedPostsByEmail(@PathVariable String email) {
        return likeService.getLikedPostsByEmail(email);
    }
}
