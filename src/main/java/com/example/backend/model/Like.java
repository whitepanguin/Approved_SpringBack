package com.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
@CompoundIndex(def = "{'postId': 1, 'userid': 1}", unique = true)
public class Like {

    @Id
    private String id;

    @DBRef
    private Post postId;

    private String userid;

    public Like() {}

    public Like(Post postId, String userid) {
        this.postId = postId;
        this.userid = userid;
    }

    public Like(String id, Post postId, String userid) {
        this.id = id;
        this.postId = postId;
        this.userid = userid;
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
}
