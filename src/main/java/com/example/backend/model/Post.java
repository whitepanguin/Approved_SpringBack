package com.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String userid;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String email; // 작성자 이메일


    private int views = 0;
    private int comments = 0;
    private int likes = 0;
    private String preview;

    private boolean isHot = false;
    private boolean isNotice = false;

    @Field("createdAt")
    private String createdAt;
    @Field("updatedAt")
    private String updatedAt;

    private int reports = 0; // 신고 횟수
    private boolean isReported = false; // 신고 처리 여부


    public Post() {}

    public Post(String updatedAt, String createdAt, boolean isNotice, boolean isHot, String preview, int likes, int comments, int views, List<String> tags, String category, String content, String title, String userid, String email, String id) {
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.isNotice = isNotice;
        this.isHot = isHot;
        this.preview = preview;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.tags = tags;
        this.category = category;
        this.content = content;
        this.title = title;
        this.userid = userid;
        this.email = email;
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addReport() {
        this.reports += 1;
        // views가 0일 경우 divide by zero 방지
        if (views > 0 && reports > (views / 3)) {
            this.isReported = true;
        }
    }

}
