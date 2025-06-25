package com.example.backend.controller;

import com.example.backend.model.Post;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostController(PostService postService,
                          UserRepository userRepository,
                          CommentRepository commentRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
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

    @GetMapping("/email/{email}")
    public List<Post> getPostsByEmail(@PathVariable String email) {
        return postService.getPostsByEmail(email);
    }

    @GetMapping("/count/email/{email}")
    public long getPostCountByEmail(@PathVariable String email) {
        return postService.getPostCountByEmail(email);
    }

    @GetMapping("/like/email/{email}")
    public long getReceivedLikeCountByEmail(@PathVariable String email) {
        return postService.getReceivedLikeCountByEmail(email);
    }

    // 🔹 사용자별 이메일 기반 통계
    @GetMapping("/stats/email/{email}")
    public ResponseEntity<?> getUserStatsByEmail(@PathVariable String email) {
        try {
            long postCount = postService.getPostCountByEmail(email);
            long commentCount = postService.getCommentCountByUser(email);
            long likeCount = postService.getReceivedLikeCountByEmail(email);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "postCount", postCount,
                    "commentCount", commentCount,
                    "likeCount", likeCount
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "유저 통계 조회 실패"));
        }
    }

    // 🔹 전체 커뮤니티 통계 API
    @GetMapping("/stats/community")
    public ResponseEntity<?> getCommunityStats() {
        try {
            long totalUsers = userRepository.count();
            long totalPosts = postService.getPostCount();
            long totalComments = commentRepository.count();

            return ResponseEntity.ok(Map.of(
                    "totalUsers", totalUsers,
                    "totalPosts", totalPosts,
                    "totalComments", totalComments
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "커뮤니티 통계 조회 실패"));
        }
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

    @GetMapping("/PostCount")
    public ResponseEntity<?> getPostCount() {
        try {
            long count = postService.getPostCount();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "유저 수 조회 성공",
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "글 수 조회 실패"));
        }
    }

    @PatchMapping("/{id}/report")
    public ResponseEntity<?> reportPost(@PathVariable String id) {
        try {
            Post updated = postService.reportPost(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", updated.isReported() ? "해당 글은 다수의 신고로 제한되었습니다." : "신고가 접수되었습니다.",
                    "isReported", updated.isReported(),
                    "reports", updated.getReports()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/reported")
    public ResponseEntity<List<Post>> getReportedPosts() {
        return ResponseEntity.ok(postService.getReportedPosts());
    }

    @GetMapping("/reported/count")
    public ResponseEntity<?> getReportedPostCount() {
        try {
            long count = postService.getReportedPostCount();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "신고된 글 수 조회 성공",
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "신고된 글 수 조회 실패"));
        }
    }

    @GetMapping("/{id}/like-count")
    public ResponseEntity<?> getLikeCount(@PathVariable String id) {
        int count = postService.getLikeCountByPostId(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "likeCount", count
        ));
    }
}
