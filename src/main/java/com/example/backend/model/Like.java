package com.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "likes")
@CompoundIndex(def = "{'postId': 1, 'email': 1}", unique = true)
public class Like {

    @Id
    private String id;

    @DBRef
    private Post postId;

    private String userid;
    private String email;

    private LocalDateTime createdAt = LocalDateTime.now();


    public Like() {}

    public Like(Post postId, String userid, String email) {
        this.postId = postId;
        this.userid = userid;
        this.email = email;
    }


    public Like(String id, Post postId, String userid,String email) {
        this.id = id;
        this.postId = postId;
        this.userid = userid;
        this.email  = email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
