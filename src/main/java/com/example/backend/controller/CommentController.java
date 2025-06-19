package com.example.backend.controller;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.DTO.CommentResponseDto;
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

    /* ----------------------------------------------------------------
       ① 게시글별 댓글 조회
       ---------------------------------------------------------------- */
    @GetMapping("/{postId}")
    public List<CommentResponseDto> getCommentsByPost(@PathVariable String postId) {   // 🔹 반환 타입 변경
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
}
