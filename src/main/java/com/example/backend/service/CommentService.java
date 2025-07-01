package com.example.backend.service;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.DTO.CommentResponseDto;
import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    /* ───────────────── 댓글 조회 ───────────────── */

    public List<CommentResponseDto> getCommentsByPost(String postId) {
        // 🔧 수정된 메서드명 반영
        List<Comment> comments = commentRepo.findByPostIdOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(comment -> {
                    CommentResponseDto dto = toDto(comment);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByUser(String userid) {
        return commentRepo.findByUseridOrderByCreatedAtDesc(userid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByEmail(String email) {
        return commentRepo
                .findByEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public long countCommentsByEmail(String email) {
        return commentRepo.countByEmail(email);
    }

    /* ───────────────── 댓글 저장 ───────────────── */

    public CommentResponseDto createComment(CommentDTO dto) {
        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow();

        Comment comment = new Comment();
        comment.setPostId(post.getId()); // ✅ 문자열로 설정
        comment.setUserid(dto.getUserid());
        comment.setEmail(dto.getEmail());
        comment.setContent(dto.getContent());

        Comment saved = commentRepo.save(comment);

        post.setComments(post.getComments() + 1);
        postRepo.save(post);

        return toDto(saved);
    }

    /* ───────────────── 내부 변환 메서드 ───────────────── */

    private CommentResponseDto toDto(Comment c) {
        String postId = c.getPostId(); // ✅ 문자열 ID만 있음
        String title = postRepo.findById(postId)
                .map(Post::getTitle)
                .orElse("(삭제된 글)");

        return new CommentResponseDto(
                c.getId(),
                c.getUserid(),
                c.getContent(),
                c.getCreatedAt(),
                postId,
                title
        );
    }

    /* ───────────────── 댓글 삭제 메서드 ───────────────── */
    public void deleteComment(String id, String email) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        String postId = comment.getPostId();

        commentRepo.deleteById(id);

        postRepo.findById(postId).ifPresent(post -> {
            int updated = Math.max(post.getComments() - 1, 0);
            post.setComments(updated);
            postRepo.save(post);
        });
    }
}
