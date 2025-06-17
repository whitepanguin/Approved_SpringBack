package com.example.backend.service;

import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import java.util.HashMap;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {




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

    public long getPostCountByUser(String userid) {
        return postRepository.countByUserid(userid);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> updatePost(String id, Post newPost) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            post.setCategory(newPost.getCategory());
            post.setTags(newPost.getTags());
            post.setPreview(newPost.getPreview());
            post.setUpdatedAt(newPost.getUpdatedAt());
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
            counts.put((String) doc.get("_id"), ((Number) doc.get("count")).longValue());
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

}
