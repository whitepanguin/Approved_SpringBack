package org.example.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;
    private String password;
    private String name;
    private String userid;
    private String birthDate;
    private String phone;
    private String businessType;
    private String address;
    private String profile;
    private String provider;

    @Field("createdAt")
    private String createdAt;
    @Field("updatedAt")
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

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

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

    private List<String> likedPosts = new ArrayList<>();
    public List<String> getLikedPosts() { return likedPosts; }
    public void setLikedPosts(List<String> likedPosts) { this.likedPosts = likedPosts; }

}
