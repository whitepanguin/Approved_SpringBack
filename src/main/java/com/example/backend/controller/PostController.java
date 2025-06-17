package com.example.backend.controller;

import com.example.backend.model.Post;
import com.example.backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userid}")
    public List<Post> getPostsByUser(@PathVariable String userid) {
        return postService.getPostsByUser(userid);
    }

    @GetMapping("/count/{userid}")
    public long getPostCountByUser(@PathVariable String userid) {
        return postService.getPostCountByUser(userid);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody Post post) {
        return postService.updatePost(id, post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        return postService.deletePost(id)
                ? ResponseEntity.ok().body("삭제 성공")
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/view")
    public void incrementView(@PathVariable String id) {
        postService.incrementView(id);
    }


    @GetMapping("/category-counts")
    public Map<String, Long> getCategoryCounts() {
        return postService.getCategoryCounts();
    }


}
