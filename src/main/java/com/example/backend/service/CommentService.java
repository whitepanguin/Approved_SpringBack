// src/main/java/com/example/backend/service/CommentService.java
package com.example.backend.service;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.DTO.CommentResponseDto;      // ★ 새 DTO
import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;
import com.example.backend.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;                      // ★ stream 변환용

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository    postRepo;

    public CommentService(CommentRepository commentRepo,
                          PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo    = postRepo;
    }

    /* ───────────────── 댓글 조회 ───────────────── */

    /** 게시글별 댓글 목록 */
//    public List<CommentResponseDto> getCommentsByPost(String postId) {
//        return commentRepo.findByPostId_IdOrderByCreatedAtAsc(postId)
//                .stream()
//                .map(this::toDto)             // ★ 모델 → DTO
//                .collect(Collectors.toList());
//    }
    public List<CommentResponseDto> getCommentsByPost(String postId) {
        List<Comment> comments = commentRepo.findByPostId_IdOrderByCreatedAtAsc(postId);

//        System.out.println("📥 가져온 댓글 수: " + comments.size());
//        for (Comment c : comments) {
//            System.out.println("📝 댓글 내용: " + c.getContent() + " / 작성일: " + c.getCreatedAt());
//        }

        return comments.stream()
                .map(comment -> {
                    CommentResponseDto dto = toDto(comment);
//                    System.out.println("➡️ DTO 변환: " + dto); // toString() 구현되어 있으면 내용 출력됨
                    return dto;
                })
                .collect(Collectors.toList());
    }


    /** 유저별 댓글 목록 */
    public List<CommentResponseDto> getCommentsByUser(String userid) {
        return commentRepo.findByUseridOrderByCreatedAtDesc(userid)
                .stream()
                .map(this::toDto)             // ★ 모델 → DTO
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByEmail(String email) {
        return commentRepo                     // ✅ 메서드만 바꾼다
                .findByEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    /** 이메일 기준 댓글 수 조회 */
    public long countCommentsByEmail(String email) {
        return commentRepo.countByEmail(email);
    }

    /* ───────────────── 댓글 저장 ───────────────── */

    /** 댓글 작성 & 저장 후 DTO 반환 */
    public CommentResponseDto createComment(CommentDTO dto) {

        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow();             // 게시글 존재 확인
        System.out.println(post);
        Comment comment = new Comment();
        comment.setPostId(post);
        comment.setUserid(dto.getUserid());
        comment.setEmail(dto.getEmail());
        comment.setContent(dto.getContent());

        Comment saved = commentRepo.save(comment);

        // 게시글에 댓글 수 +1
        post.setComments(post.getComments() + 1);
        postRepo.save(post);

        return toDto(saved);                            // ★ DTO 로 반환
    }

    /* ───────────────── 내부 변환 메서드 ───────────────── */

    /** Comment → CommentResponseDto */
    private CommentResponseDto toDto(Comment c) {
        Post p = c.getPostId();                         // null 방지
        return new CommentResponseDto(
                c.getId(),
                c.getUserid(),
                c.getContent(),
                c.getCreatedAt(),
                p != null ? p.getId()    : null,
                p != null ? p.getTitle() : "(삭제된 글)"
        );
    }
    /* ───────────────── 댓글 삭제 메서드 ───────────────── */
    public void deleteComment(String id, String email) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        commentRepo.deleteById(id);
    }



}
