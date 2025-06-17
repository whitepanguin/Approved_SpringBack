package com.example.backend.controller;

import com.example.backend.model.Like;
import com.example.backend.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PatchMapping("/{postId}")
    public String toggleLike(@PathVariable String postId, @RequestParam String userid) {
        return likeService.toggleLike(postId, userid);
    }

    @GetMapping("/user/{userid}")
    public List<Like> getLikesByUser(@PathVariable String userid) {
        return likeService.getLikesByUser(userid);
    }
}
