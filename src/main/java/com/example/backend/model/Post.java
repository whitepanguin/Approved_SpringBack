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
    private String status = "답변대기";


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

    public Post(String id, String userid, String title, String content, String category, List<String> tags, String email, String status, int views, int comments, int likes, String preview, boolean isHot, boolean isNotice, String createdAt, String updatedAt, int reports, boolean isReported) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.email = email;
        this.status = status;
        this.views = views;
        this.comments = comments;
        this.likes = likes;
        this.preview = preview;
        this.isHot = isHot;
        this.isNotice = isNotice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reports = reports;
        this.isReported = isReported;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
