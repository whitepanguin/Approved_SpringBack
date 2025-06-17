package com.example.backend.service;

import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    public List<Comment> getCommentsByPost(String postId) {
        return commentRepo.findByPostId_IdOrderByCreatedAtAsc(postId);
    }

    public Comment createComment(Comment comment, String postId) {
        // 댓글 저장
        Post post = postRepo.findById(postId).orElseThrow();
        comment.setPostId(post);
        Comment saved = commentRepo.save(comment);

        // 댓글 수 +1
        post.setComments(post.getComments() + 1);
        postRepo.save(post);

        return saved;
    }

    public List<Comment> getCommentsByUser(String userid) {
        return commentRepo.findByUseridOrderByCreatedAtDesc(userid);
    }
}
