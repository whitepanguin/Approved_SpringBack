package com.example.backend.controller;

import com.example.backend.model.Post;
import com.example.backend.service.PostService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/email/{email}")
    public List<Post> getPostsByEmail(@PathVariable String email) {
        return postService.getPostsByEmail(email);
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
    /*
async function handleReport(postId) {
  const res = await fetch(`http://localhost:8000/posts/${postId}/report`, {
    method: "PATCH",
  });
  const data = await res.json();
  alert(data.message);
}
*/
    // 신고된 게시글 리스트 반환
    @GetMapping("/reported")
    public ResponseEntity<List<Post>> getReportedPosts() {
        return ResponseEntity.ok(postService.getReportedPosts());
    }

    // 신고된 게시글 개수 반환
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
                    .body(Map.of(
                            "success", false,
                            "message", "신고된 글 수 조회 실패"
                    ));
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
