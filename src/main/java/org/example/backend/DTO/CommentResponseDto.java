package org.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {
    /* ====== Comment 자체 ====== */
    private String id;
    private String userid;
    private String content;
    private LocalDateTime createdAt;

    /* ====== Post 요약 정보 ====== */
    private String postId;
    private String postTitle;     // ← 모달 띄우기에 꼭 필요!
    // private String category;   // 필요 시 주석 해제
    // private String preview;    // 필요 시 주석 해제

}
