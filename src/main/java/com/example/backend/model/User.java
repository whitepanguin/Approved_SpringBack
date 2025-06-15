package com.example.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;
    private String password;
    private String name;
    private String userid;
    private Long birthDate;
    private String address;
    private String profile;
    private String provider;
    private String createdAt;
    private String updatedAt;

    // 기본 생성자
    public User() {}

    public User(String email, String name, String profile, String provider, String userid) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.provider = provider;
        this.userid = userid;
    }


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public Long getBirthDate() { return birthDate; }
    public void setBirthDate(Long birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
