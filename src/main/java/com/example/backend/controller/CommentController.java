package com.example.backend.controller;

import com.example.backend.model.Comment;
import com.example.backend.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable String postId) {
        return commentService.getCommentsByPost(postId);
    }

    @PostMapping("")
    public Comment createComment(@RequestBody Comment comment, @RequestParam String postId) {
        return commentService.createComment(comment, postId);
    }

    @GetMapping("/user/{userid}")
    public List<Comment> getCommentsByUser(@PathVariable String userid) {
        return commentService.getCommentsByUser(userid);
    }
}
