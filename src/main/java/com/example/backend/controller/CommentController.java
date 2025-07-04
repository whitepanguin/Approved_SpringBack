package com.example.backend.controller;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.DTO.CommentResponseDto;
import com.example.backend.model.Comment;
import com.example.backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "https://port-next-approved-front-m5mcnm8ebdc80276.sel4.cloudtype.app")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /* ----------------------------------------------------------------
       ① 게시글별 댓글 조회
       ---------------------------------------------------------------- */
    @GetMapping("/{postId}")
    public List<CommentResponseDto> getCommentsByPost(@PathVariable String postId) {   // 🔹 반환 타입 변경
        System.out.println(postId);
        return commentService.getCommentsByPost(postId);
    }

    /* ----------------------------------------------------------------
       ② 댓글 작성
       ----------------------------------------------------------------
       프론트에서 보내는 JSON 예시
       {
          "postId":  "abc123",
          "userid":  "키위짱짱맨",
          "content": "댓글 내용"
       }
       ---------------------------------------------------------------- */
    @PostMapping
    public CommentResponseDto createComment(@RequestBody CommentDTO dto) {             // 🔹 반환 타입 변경
        return commentService.createComment(dto);
    }

    /* ----------------------------------------------------------------
       ③ 특정 유저의 댓글 목록
       ---------------------------------------------------------------- */
    @GetMapping("/user/{userid}")
    public List<CommentResponseDto> getCommentsByUser(@PathVariable String userid) {   // 🔹 반환 타입 변경
        return commentService.getCommentsByUser(userid);
    }

    @GetMapping("/email/{email}")
    public List<CommentResponseDto> getCommentsByEmail(@PathVariable String email) {
        return commentService.getCommentsByEmail(email);
    }
    /* ----------------------------------------------------------------
       ④ 특정 이메일 기준 댓글 수 조회
       ---------------------------------------------------------------- */
    @GetMapping("/count/{email}")
    public Map<String, Long> getCommentCountByEmail(@PathVariable String email) {
        long count = commentService.countCommentsByEmail(email);
        return Map.of("count", count);
    }
    /* ----------------------------------------------------------------
       5 특정 이메일 기준 댓글 수 조회
       ---------------------------------------------------------------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id, @RequestParam String email) {
        try {
            commentService.deleteComment(id, email);
            return ResponseEntity.ok("댓글 삭제 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ 삭제 실패: " + e.getMessage());
        }
    }
}

