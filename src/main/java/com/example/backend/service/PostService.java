package com.example.backend.service;

import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.LikeRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {


    @Autowired
    private LikeRepository likeRepo;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByUser(String userid) {
        return postRepository.findByUseridOrderByCreatedAtDesc(userid);
    }

    public List<Post> getPostsByEmail(String email) {
        return postRepository.findByEmailOrderByCreatedAtDesc(email);
    }
    public long getPostCountByUser(String userid) {
        return postRepository.countByUserid(userid);
    }

    public Post createPost(Post post) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 날짜가 없으면 백엔드에서 직접 설정
        if (post.getCreatedAt() == null || post.getCreatedAt().isEmpty()) {
            post.setCreatedAt(now);
        }
        post.setUpdatedAt(now);

        return postRepository.save(post);
    }

    public Optional<Post> updatePost(String id, Post newPost) {
        return postRepository.findById(id).map(post -> {
            if (newPost.getTitle() != null) post.setTitle(newPost.getTitle());
            if (newPost.getContent() != null) post.setContent(newPost.getContent());
            if (newPost.getCategory() != null) post.setCategory(newPost.getCategory());
            if (newPost.getTags() != null) post.setTags(newPost.getTags());
            if (newPost.getViews() != 0) post.setViews(newPost.getViews());
            if (newPost.getLikes() != 0) post.setLikes(newPost.getLikes());
            if (newPost.getReports() != 0) post.setReports(newPost.getReports());
            if (newPost.getPreview() != null) post.setPreview(newPost.getPreview());
            if (newPost.getUpdatedAt() != null) post.setUpdatedAt(newPost.getUpdatedAt());
            if (newPost.getStatus() != null) post.setStatus(newPost.getStatus());

            return postRepository.save(post);
        });
    }

    public boolean deletePost(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void incrementView(String id) {
        postRepository.findById(id).ifPresent(post -> {
            post.setViews(post.getViews() + 1);
            postRepository.save(post);
        });
    }


    public Map<String, Long> getCategoryCounts() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("category").count().as("count")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "posts", Map.class);

        Map<String, Long> counts = new HashMap<>();
        for (Map doc : results) {
            String category = (String) doc.get("_id");
            if (category == null || category.trim().isEmpty()) {
                category = "기타"; // 또는 "uncategorized", "없음" 등으로 설정
            }
            counts.put(category, ((Number) doc.get("count")).longValue());
        }

        return counts;
    }


    public List<Post> getSortedPosts(String category, String sort) {
        if (category != null && !category.isEmpty()) {
            if ("comments".equals(sort)) return postRepository.findByCategoryOrderByCommentsDesc(category);
            return postRepository.findByCategoryOrderByCreatedAtDesc(category);
        } else {
            if ("comments".equals(sort)) return postRepository.findAllByOrderByCommentsDesc();
            return postRepository.findAllByOrderByCreatedAtDesc();
        }
    }
    public long getPostCount() {
        return postRepository.count();
    }

    public Post reportPost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 없음"));
        post.addReport();
        return postRepository.save(post);
    }

    public List<Post> getReportedPosts() {
        return postRepository.findByIsReportedTrue();
    }

    public long getReportedPostCount() {
        return postRepository.countByIsReportedTrue();
    }
    public int getLikeCountByPostId(String postId) {
        return likeRepo.countByPostId_Id(postId);
    }
}
